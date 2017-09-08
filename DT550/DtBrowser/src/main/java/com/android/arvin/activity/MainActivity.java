package com.android.arvin.activity;

import android.support.constraint.solver.Goal;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.android.arvin.DataText.Test;
import com.android.arvin.R;
import com.android.arvin.data.GObject;
import com.android.arvin.ui.DeviceLayout;
import com.android.arvin.util.DeviceConfig;
import com.android.arvin.util.GAdapter;
import com.android.arvin.util.GAdapterUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends DtAppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();
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

    private void initActionBar(){
        actionBar.addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {
            @Override
            public void onMenuVisibilityChanged(boolean isVisible) {
                if (!isVisible) {

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    private void initView(){
        deviceLayout = (DeviceLayout) findViewById(R.id.device_layout_one);
    }

    private void initData(){
        HashMap<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put(DeviceConfig.MEASURE_ITEM_LIQUID_STATE, R.id.measure_item_liquid_state_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_NAME, R.id.measure_item_name_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_TIME,  R.id.measure_item_value_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_VALUE, R.id.measure_item_time_text);

        ArrayList<Integer> styleList = new ArrayList<>();
       /* styleList.add(R.id.measure_item_liquid_state_text);
        styleList.add(R.id.measure_item_name_text);
        styleList.add(R.id.measure_item_value_text);
        styleList.add(R.id.measure_item_time_text);*/

        GAdapter adapter = new GAdapter();
        for(int i = 0; i < 12; i ++){
            GObject object = GAdapterUtil.objectFromTestData(
                    new Test(
                            String.format(getString(R.string.lack_of_liquid), getString(R.string.lack_of_liquid_yes)),
                            getString(R.string.nitrite_nitrogen),
                            String.format(getString(R.string.concentration_unit), 0.01),
                            "1986-08-07 15:00:00"
                    ));
            adapter.addObject(object);
        }

        deviceLayout.setSubLayoutParameter(mapping, styleList);
        deviceLayout.setupGridLayout(this, true);
        Log.d(TAG, "setAdapter");
        deviceLayout.setAdapter(adapter);
    }

}
