package com.android.arvin.activity;

/**
 * Created by tuoyi on 2017/9/10 0010.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.arvin.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016/11/9.
 */

public class MeLineActivity extends Activity implements OnChartValueSelectedListener{

    private LineChart mChart;
    private TextView test_callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meline);
        init();
        initLineChart();
    }



    private void init() {
        test_callback = (TextView) findViewById(R.id.test_callback);
        mChart = (LineChart) findViewById(R.id.meline);
        mChart.setOnChartValueSelectedListener(this);
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(false);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);


        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(7,true);
        xAxis.setDrawGridLines(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormatddMM = new SimpleDateFormat("dd MMM");
            private SimpleDateFormat mFormatHHmm = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                Date date = new Date(millis);
                mFormatddMM.format(date);
                mFormatHHmm.format(date);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(mFormatddMM.format(date))
                    .append("\n").append(mFormatHHmm.format(date));
                return mFormatHHmm.format(date);
            }
        });

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setStartAtZero(true);
        yAxis.setLabelCount(4);
        mChart.getAxisRight().setEnabled(false);

        // add data
        setData(15, 0.3f);
        mChart.animateX(2500);

    }



    private void initLineChart() {
        mChart = (LineChart) findViewById(R.id.dialog_line_chart);
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        //mChart.setViewPortOffsets(0f, 0f, 0f, 0f);
        // add data
        setData(10, 0.3f);
        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        initAxis(mChart);
    }

    private void initAxis (LineChart mChart){
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(7,true);
        xAxis.setDrawGridLines(true);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        //xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f); // one hour
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(1.00f);
        leftAxis.setGranularity(0.1f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));
        //leftAxis.setStartAtZero(true);
        leftAxis.setLabelCount(6);
        mChart.getAxisRight().setEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setData(int count, float range) {

        // now in hours
        long now = TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis());
        ArrayList<Entry> values = new ArrayList<Entry>();
        float from = now;
        // count = hours
        float to = now + count;
        // increment by 1 hour
        for (float x = from; x < to; x++) {
            float y = getRandom(range, 0.2f);
            values.add(new Entry(x, y)); // add one entry per hour
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        set1.setDrawFilled(true);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
    }


    protected float getRandom(float range, float startsfrom) {
        return (float) (Math.random() * range) + startsfrom;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
