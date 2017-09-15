package com.android.arvin.ui.Dialog;

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
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.interfaces.UpdateDialogCallback;
import com.android.arvin.util.AnimationUtils;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by tuoyi on 2017/9/9 0009.
 */

public class DeviceDialog extends DialogFragment implements UpdateDialogCallback {

    private static final String TAG = DeviceDialog.class.getSimpleName();
    private TextView itemNameText;
    private ImageView dialogCloseImage;
    private DialogPage dialogPageMax, dialogPageMean, dialogPageMin;

    private DeviceManager deviceManager;
    private LineChart mChart;
    private ArrayList<Entry> hisDataEntrys;
    private String deviceCode, itemCode;
    private float valueMix = 0.00f;
    private float valueMax = 0.00f;

    public static DeviceDialog instance() {
        Bundle args = new Bundle();
        DeviceDialog deviceDialog = new DeviceDialog();
        deviceDialog.setArguments(args);
        return deviceDialog;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
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
        deviceManager.requestUpdateDialog(deviceCode, itemCode);
        return super.show(transaction, tag);

    }

    private void initView(View view) {
        itemNameText = (TextView) view.findViewById(R.id.dialog_item_name);
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
    }

    private void setItemNameText(String string) {
        itemNameText.setText(string);
    }

    private void initLineChart(View view) {
        mChart = (LineChart) view.findViewById(R.id.dialog_line_chart);
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setBackgroundResource(R.color.dialogLineChartBackgroung);
    }

    private void initAxis(LineChart mChart) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(7, true);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(R.color.dialogLinChartText);
        xAxis.setGranularity(1f); // one hour
        xAxis.setYOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(valueMax);
        leftAxis.setGranularity(valueMix);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(R.color.dialogLinChartText);
        leftAxis.setLabelCount(6);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
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
        mChart.setData(data);
        mChart.animateX(1500);
    }

    @Override
    public void releaseEntrys(DeviceHistoryData hisData, Collection<Entry> collection) {
        Log.d(TAG, "releaseEntrys");
        if (collection instanceof ArrayList) {
            hisDataEntrys = (ArrayList<Entry>) collection;
        }

        Log.d(TAG, "hisData.getStrMin() : " + hisData.getStrMin());
        Log.d(TAG, "hisData.getStrMax() : " + hisData.getStrMax());

        valueMix = Float.valueOf(hisData.getStrMin());
        valueMax = Float.valueOf(hisData.getStrMax());

        setData();
        mChart.invalidate();
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        initAxis(mChart);

    }
}

