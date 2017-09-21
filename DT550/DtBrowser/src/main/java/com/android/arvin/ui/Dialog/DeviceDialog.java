package com.android.arvin.ui.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.arvin.Manager.DeviceManager;
import com.android.arvin.R;
import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.data.DeviceSubItemData;
import com.android.arvin.interfaces.UpdateDialogCallback;
import com.android.arvin.ui.custom.MyMarkerView;
import com.android.arvin.util.AnimationUtils;
import com.android.arvin.util.DtUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tuoyi on 2017/9/9 0009.
 */

public class DeviceDialog extends DialogFragment implements UpdateDialogCallback {

    private static final String TAG = DeviceDialog.class.getSimpleName();
    private TextView DeviceTitleText;
    private TextView itemNameText;
    private TextView loadText;
    private ImageView dialogCloseImage;
    private DialogPage dialogPageMax, dialogPageMean, dialogPageMin;

    private DeviceManager deviceManager;

    private DeviceData devideData;
    private DeviceSubItemData deviceSubItemData;

    private LineChart mLineChart;
    private ArrayList<Entry> hisDataEntrys;
    private List<DeviceHistoryData.DeviceHisSubItemData> hisDataRspItems;
    private String deviceCode, itemCode;

    private int xDataSize;

    private float valueMix = 0.00f;
    private float valueMax = 0.00f;

    private float maximumInSet = 0.00f;
    private float minimumInSet = 0.00f;

    private int digitsDisplay = 2;

    private static final int showColumn = 6;
    private static final int dataColumn = showColumn * 4;
    private static final float yRangeMultiple = 1.1f;
    private static final int version = Build.VERSION.SDK_INT;

    public static DeviceDialog instance(Context context) {
        Bundle args = new Bundle();
        DeviceDialog deviceDialog = new DeviceDialog();
        deviceDialog.setArguments(args);
        return deviceDialog;
    }

    public DeviceDialog() {

    }

    public DeviceData getDevideData() {
        return devideData;
    }

    public void setDevideData(DeviceData devideData) {
        this.devideData = devideData;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    public void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialogLayoutBackground
        window.setBackgroundDrawableResource(R.color.dialogLayoutBackground);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_layout, container, false);
        AnimationUtils.slideToUp(view);
        initView(view);
        initLineChart(view);

        if (deviceManager != null) {
            deviceManager.setUpdateDialogCallback(this);
        }
        return view;
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        deviceManager.requestUpdateDialog(devideData.getDeviceCode(), itemCode);

        return super.show(transaction, tag);

    }

    private void initView(View view) {
        DeviceTitleText = (TextView) view.findViewById(R.id.dialog_title_text);
        itemNameText = (TextView) view.findViewById(R.id.dialog_item_name);
        loadText = (TextView) view.findViewById(R.id.loading_text);
        dialogCloseImage = (ImageView) view.findViewById(R.id.dialog_close_image);
        dialogCloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceDialog.this.dismiss();
            }
        });

        dialogPageMax = (DialogPage) view.findViewById(R.id.dialog_page_max);
        dialogPageMax.setValueTitleText(getContext().getResources().getString(R.string.dialog_page_title_max));
        dialogPageMax.setValueTextColor(getPageMaxTextColor());
        dialogPageMax.setValueImage(R.drawable.max_value);

        dialogPageMean = (DialogPage) view.findViewById(R.id.dialog_page_mean);
        dialogPageMean.setValueTitleText(getContext().getResources().getString(R.string.dialog_page_title_mean));
        dialogPageMean.setValueTextColor(getPageMeanTextColor());
        dialogPageMean.setValueImage(R.drawable.mean_value);

        dialogPageMin = (DialogPage) view.findViewById(R.id.dialog_page_min);
        dialogPageMin.setValueTitleText(getContext().getResources().getString(R.string.dialog_page_title_min));
        dialogPageMin.setValueTextColor(getPageMinTextColor());
        dialogPageMin.setValueImage(R.drawable.min_value);
        dialogPageMin.setSepLineViewVisibility(false);

        upateDataView(itemCode);
    }

    private int getPageMaxTextColor() {
        if (version >= 23) {
            return ContextCompat.getColor(getContext(), R.color.dialogPageMaxTextColor);
        } else {
            return getContext().getResources().getColor(R.color.dialogPageMaxTextColor);
        }
    }

    private int getPageMeanTextColor() {
        if (version >= 23) {
            return ContextCompat.getColor(getContext(), R.color.dialogPageMeanTextColor);
        } else {
            return getContext().getResources().getColor(R.color.dialogPageMeanTextColor);
        }
    }

    private int getPageMinTextColor() {
        if (version >= 23) {
            return ContextCompat.getColor(getContext(), R.color.dialogPageMinTextColor);
        } else {
            return getContext().getResources().getColor(R.color.dialogPageMinTextColor);
        }
    }

    private void setItemNameText(String string) {
        itemNameText.setText(string);
    }

    private void initLineChart(View view) {
        mLineChart = (LineChart) view.findViewById(R.id.dialog_line_chart);
        mLineChart.setDragDecelerationFrictionCoef(0.9f);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setTouchEnabled(true); //触摸
        mLineChart.setDragEnabled(false); //拖拽
        mLineChart.setScaleXEnabled(false); //缩放 仅x轴
        mLineChart.setScaleYEnabled(false); //缩放 仅y轴
        mLineChart.setDoubleTapToZoomEnabled(false);//双击放大
        mLineChart.setDrawGridBackground(false);
        mLineChart.setHighlightPerDragEnabled(true);
        mLineChart.setBackgroundResource(R.color.dialogLineChartBackgroung);
        mLineChart.setScaleYEnabled(false);
        mLineChart.setPinchZoom(true);
        // mLineChart.zoom(4.0f,1.0f,0f,0f);
        mLineChart.setVisibleXRangeMaximum(7);
        mLineChart.setVisibleXRangeMinimum(4);
        mLineChart.fitScreen();

        MyMarkerView mv = new MyMarkerView(this.getContext(), R.layout.custom_marker_view);
        mv.setChartView(mLineChart);
        mLineChart.setMarker(mv);
    }

    private void updateData() {
        LineDataSet lineDataSet;
        if (mLineChart.getData() != null &&
                mLineChart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(hisDataEntrys);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            lineDataSet = new LineDataSet(hisDataEntrys, "");
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //lineDataSet.enableDashedLine(10f, 5f, 0f); //横线为虚线
            //lineDataSet.enableDashedHighlightLine(10f, 5f, 0f); //竖线为虚线
            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            lineDataSet.setColor(ColorTemplate.getHoloBlue());//显示颜色
            //lineDataSet.setCircleColor(Color.WHITE);// 圆点的颜色
            //lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));//高亮线颜色
            lineDataSet.setValueTextColor(ColorTemplate.getHoloBlue());
            lineDataSet.setLineWidth(1.5f);//线宽
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawValues(false);
            // lineDataSet.setFillAlpha(65);
            // lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setDrawFilled(true);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
                lineDataSet.setFillDrawable(drawable);
            } else {
                lineDataSet.setFillColor(Color.BLACK);
            }

            // create a data object with the datasets
            LineData data = new LineData(lineDataSet);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            mLineChart.setData(data);
        }

        initAxis(mLineChart);
        mLineChart.animateX(1500);
        Legend l = mLineChart.getLegend();
        l.setEnabled(false);

        mLineChart.invalidate();
    }

    private void initAxis(LineChart mLineChart) {
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);//横标线为虚线
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.enableGridDashedLine(10f, 10f, 0);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(R.color.dialogLinChartText);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f); // one hour
        xAxis.setLabelCount(showColumn, true);
        xAxis.setTextSize(6);

        xAxis.setValueFormatter(new IAxisValueFormatter() {

            int i = 0;
            int size = hisDataRspItems.size();
            int mon = size / (dataColumn);
            final String[] xData = new String[dataColumn];

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int id = mon * i;
                StringBuffer time = new StringBuffer();
                if (id < size) {
                    long millis = hisDataRspItems.get(id).getTestTime();
                    time.append(DtUtils.formDmhmFormat(millis));
                    if (xData[i % dataColumn] == null) {
                        xData[i % dataColumn] = time.toString();
                    }
                }
                return xData[i++ % dataColumn];
            }
        });

        LimitLine ll1 = new LimitLine(valueMax, getLimit(R.string.upper_limit, valueMax));
        ll1.setLineWidth(1f);
        ll1.setLineColor(Color.RED);
        //ll1.enableDashedLine(10f, 10f, 0f); //虚线
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        ll1.setTextSize(10f);
        ll1.setTextColor(Color.RED);

        LimitLine ll2 = new LimitLine(valueMix, getLimit(R.string.lower_limit, valueMix));
        ll2.setLineWidth(1f);
        ll2.setLineColor(Color.GREEN);
        //ll2.enableDashedLine(10f, 10f, 0f);//虚线
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(10f);
        ll2.setTextColor(Color.GREEN);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.enableGridDashedLine(10f, 10f, 0f); //竖标线为虚线
        // reset all limit lines to avoid overlapping lines
        leftAxis.removeAllLimitLines();

        if (valueMix != 0 && valueMax != 0) {
            leftAxis.addLimitLine(ll1);
            leftAxis.addLimitLine(ll2);
        }

        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        //设备图标范围

        float min = minimumInSet * yRangeMultiple;
        float max = maximumInSet * yRangeMultiple;
        leftAxis.setAxisMinimum(DtUtils.formatFloat(0, digitsDisplay));
        leftAxis.setAxisMaximum(DtUtils.formatFloat(max, digitsDisplay));
        leftAxis.setGranularity(DtUtils.formatFloat((max) / 6, digitsDisplay));

        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(R.color.dialogLinChartText);
        leftAxis.setLabelCount(6);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void upateDataView(String itemCode) {
        List<DeviceSubItemData> subItemDatas = devideData.getDeviceSubItemDatas();
        for (DeviceSubItemData subItemData : subItemDatas) {
            if (subItemData.getSubItemDataCode().equals(itemCode)) {
                deviceSubItemData = subItemData;
                updateDatTitle(devideData.getDeviceName(), getSubItemTitle());
            }
        }

        updatePageView(maximumInSet, minimumInSet);
    }

    private String getSubItemTitle() {
        StringBuilder builder = new StringBuilder();
        builder.append(deviceSubItemData.getSubItemDataName());
        if (!DtUtils.isNullOrEmpty(deviceSubItemData.getSubItemDataUnit())) {
            builder.append("(")
                    .append(deviceSubItemData.getSubItemDataUnit())
                    .append(")");
        }
        return builder.toString();
    }

    private String getLimit(int id, float value) {
        return String.format(getContext().getString(id), DtUtils.formatFloat(value, digitsDisplay));
    }

    private void updateDatTitle(String title, String itemName) {
        DeviceTitleText.setText(title);
        itemNameText.setText(itemName);
    }

    private void updatePageView(float max, float min) {
        dialogPageMax.setValueText(String.valueOf(DtUtils.formatFloat(max, digitsDisplay)));
        dialogPageMean.setValueText(String.valueOf(DtUtils.formatDouble(DtUtils.meanValue(hisDataRspItems), digitsDisplay)));
        dialogPageMin.setValueText(String.valueOf(DtUtils.formatFloat(min, digitsDisplay)));
    }


    @Override
    public void releaseEntrys(DeviceHistoryData hisData, Collection<Entry> collection) {
        if (collection instanceof ArrayList) {
            hisDataEntrys = (ArrayList<Entry>) collection;
        }

        if (hisData.getStrMin() != null && hisData.getStrMax() != null) {
            valueMix = Float.valueOf(hisData.getStrMin());
            valueMax = Float.valueOf(hisData.getStrMax());
        }

        maximumInSet = hisData.getMaximumInSet();
        minimumInSet = hisData.getMinimumInSet();

        xDataSize = hisDataEntrys.size();
        hisDataRspItems = hisData.getDeviceHisSubItemDataList();
        if (hisDataRspItems != null && hisDataRspItems.size() > 0) {
            digitsDisplay = hisDataRspItems.get(0).getnFormat();
        }
        loadText.setVisibility(View.GONE);
        mLineChart.setVisibility(View.VISIBLE);
        updatePageView(maximumInSet, minimumInSet);
        updateData();
    }
}

