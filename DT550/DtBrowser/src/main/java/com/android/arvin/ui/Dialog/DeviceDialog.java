package com.android.arvin.ui.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tuoyi on 2017/9/9 0009.
 */

public class DeviceDialog extends DialogFragment implements UpdateDialogCallback {

    private static final String TAG = DeviceDialog.class.getSimpleName();
    private Context context;
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
    private float valueMix = 0.00f;
    private float valueMax = 0.00f;
    private int xDataSize = 0;

    private static final int showColumn = 6;
    private static final int dataColumn = showColumn * 4;

    public static DeviceDialog instance(Context context) {
        Bundle args = new Bundle();
        DeviceDialog deviceDialog = new DeviceDialog();
        deviceDialog.setArguments(args);
        context = context;
        return deviceDialog;
    }

    public DeviceDialog(){

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

    public float getValueMix() {
        return valueMix;
    }

    public void setValueMix(float valueMix) {
        this.valueMix = valueMix;
    }

    public float getValueMax() {
        return valueMax;
    }

    public void setValueMax(float valueMax) {
        this.valueMax = valueMax;
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
        dialogPageMax.setValueImage(R.drawable.max_value);

        dialogPageMean = (DialogPage) view.findViewById(R.id.dialog_page_mean);
        dialogPageMean.setValueTitleText(getContext().getResources().getString(R.string.dialog_page_title_mean));
        dialogPageMean.setValueImage(R.drawable.mean_value);

        dialogPageMin = (DialogPage) view.findViewById(R.id.dialog_page_min);
        dialogPageMin.setValueTitleText(getContext().getResources().getString(R.string.dialog_page_title_min));
        dialogPageMin.setValueImage(R.drawable.min_value);
        dialogPageMin.setSepLineViewVisibility(false);

        upateDataView(itemCode);
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

    private void setData() {
        // create a dataset and give it a type
        LineDataSet lineDataSet = new LineDataSet(hisDataEntrys, "");

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setValueTextColor(ColorTemplate.getHoloBlue());
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setFillAlpha(65);
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setDrawCircleHole(false);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        // create a data object with the datasets
        LineData data = new LineData(lineDataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mLineChart.setData(data);
        initAxis(mLineChart);
        mLineChart.invalidate();
        Legend l = mLineChart.getLegend();
        l.setEnabled(false);
        mLineChart.animateX(1500);
    }

    private void initAxis(LineChart mLineChart) {
        XAxis xAxis = mLineChart.getXAxis();
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

        float f = 0.0f;
        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        f = (float) (valueMix / 1.1);
        leftAxis.setAxisMinimum(f);
        leftAxis.setAxisMaximum(valueMax);
        f = (valueMax - valueMix) / 6;
        leftAxis.setGranularity(f);
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
                updateDatTitle(devideData.getDeviceName(), deviceSubItemData.getSubItemDataName());
            }
        }

        updatePageView(valueMax, valueMix);
    }

    private void updateDatTitle(String title, String itemName) {
        DeviceTitleText.setText(title);
        itemNameText.setText(itemName);
    }

    private void updatePageView(float max, float min) {
        int mom = 2;
        if (hisDataRspItems != null && hisDataRspItems.size() > 0) {
            mom = hisDataRspItems.get(0).getnFormat();
        }
        dialogPageMax.setValueText(String.valueOf(DtUtils.formatFloat(max, mom)));
        dialogPageMean.setValueText(String.valueOf(DtUtils.formatDouble(DtUtils.meanValue(hisDataRspItems), mom)));
        dialogPageMin.setValueText(String.valueOf(DtUtils.formatFloat(min, mom)));
    }


    @Override
    public void releaseEntrys(DeviceHistoryData hisData, Collection<Entry> collection) {
        if (collection instanceof ArrayList) {
            hisDataEntrys = (ArrayList<Entry>) collection;
        }

        valueMix = Float.valueOf(hisData.getStrMin());
        valueMax = Float.valueOf(hisData.getStrMax());
        xDataSize = hisDataEntrys.size();
        hisDataRspItems = hisData.getDeviceHisSubItemDataList();
        loadText.setVisibility(View.GONE);
        mLineChart.setVisibility(View.VISIBLE);
        updatePageView(valueMax, valueMix);
        setData();
    }
}

