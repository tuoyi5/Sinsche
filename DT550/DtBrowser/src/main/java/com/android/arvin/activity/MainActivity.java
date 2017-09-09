package com.android.arvin.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import com.android.arvin.DataText.Test;
import com.android.arvin.R;
import com.android.arvin.data.GObject;
import com.android.arvin.ui.ContentItemView;
import com.android.arvin.ui.DeviceFooterLayout;
import com.android.arvin.ui.DeviceLayout;
import com.android.arvin.ui.DtContentView;
import com.android.arvin.util.DeviceConfig;
import com.android.arvin.util.GAdapter;
import com.android.arvin.util.GAdapterUtil;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends DtAppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    final static String TAG = MainActivity.class.getSimpleName();
    //private DeviceStorehouseLayout deviceStorehouseLayout;
    private DeviceLayout deviceLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSupportActionBarWithCustomBackFunction();
        initActionBar();
        initView();
        initData();
    }

    private void initActionBar() {
        actionBar.addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {
            @Override
            public void onMenuVisibilityChanged(boolean isVisible) {
                if (!isVisible) {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    private void initView() {
        deviceLayout = (DeviceLayout) findViewById(R.id.device_layout_one);

        deviceLayout.setUpdateUiCallback(new DeviceLayout.UpdateUiCallback() {
            @Override
            public void updateGridLayoutHight(int row) {
                //deviceLayout.updateGridLayoutHight(row);
            }
        });

        deviceLayout.setOnClickCallBack(new DeviceFooterLayout.OnClickCallBack() {
            @Override
            public void onClickCallBack(boolean fold) {
                deviceLayout.updateGridLayoutHight(fold);
            }
        });

        deviceLayout.setContentViewCallback(new DtContentView.ContentViewCallback(){

            @Override
            public void uiLoading() {
                setAdapter();
            }

            @Override
            public void beforeSetupData(ContentItemView view, GObject object) {

            }

            @Override
            public void onItemSelected(ContentItemView view) {

            }

            @Override
            public void onItemClick(ContentItemView view) {
                Log.d(TAG, "onItemClick");
            }

            @Override
            public boolean onItemLongClick(ContentItemView view) {
                return false;
            }
        });
    }

    private void initData() {
        HashMap<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put(DeviceConfig.MEASURE_ITEM_LIQUID_STATE, R.id.measure_item_liquid_state_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_NAME, R.id.measure_item_name_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_VALUE, R.id.measure_item_value_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_TIME, R.id.measure_item_time_text);

        ArrayList<Integer> styleList = new ArrayList<>();
       /* styleList.add(R.id.measure_item_liquid_state_text);
        styleList.add(R.id.measure_item_name_text);
        styleList.add(R.id.measure_item_value_text);
        styleList.add(R.id.measure_item_time_text);*/


        deviceLayout.setSubLayoutParameter(mapping, styleList);
        deviceLayout.setupGridLayout(this, true);


    }

    public void setAdapter() {
        GAdapter adapter = new GAdapter();
        for (int i = 0; i < 12; i++) {
            GObject object = GAdapterUtil.objectFromTestData(
                    new Test(
                            String.format(getString(R.string.lack_of_liquid), getString(R.string.lack_of_liquid_yes)),
                            getString(R.string.nitrite_nitrogen),
                            String.format(getString(R.string.concentration_unit), 0.01f),
                            "2000-08-07 15:00:99"
                    ));
            adapter.addObject(object);
        }
        deviceLayout.setAdapter(adapter);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Log.d(TAG, " MotionEvent.ACTION_UP");


                break;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {


        }
    }
}
