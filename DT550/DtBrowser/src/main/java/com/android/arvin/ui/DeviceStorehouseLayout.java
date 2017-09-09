package com.android.arvin.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.arvin.R;
import com.android.arvin.util.GAdapter;
import com.android.arvin.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arvin on 2017/9/9 0009.
 */

public class DeviceStorehouseLayout extends LinearLayout {

    private Context context;
    private View deviceStoreHouseLayout;
    private DeviceLayout deviceLayout_one, deviceLayout_two;

    public DeviceStorehouseLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        RelativeLayout.LayoutParams layoutParams;
        LayoutInflater mInflater = LayoutInflater.from(context);
        deviceStoreHouseLayout = mInflater.inflate(R.layout.device_storehouse_layout, null);

        deviceLayout_one = (DeviceLayout) deviceStoreHouseLayout .findViewById(R.id.device_layout_one);
        deviceLayout_one.setUpdateUiCallback(new DeviceLayout.UpdateUiCallback() {
            @Override
            public void updateGridLayoutHight(int row) {
                //deviceLayout_one.updateGridLayoutHight(row);
            }
        });

        deviceLayout_two =  (DeviceLayout) deviceStoreHouseLayout .findViewById(R.id.device_layout_two);
        deviceLayout_two.setUpdateUiCallback(new DeviceLayout.UpdateUiCallback() {
            @Override
            public void updateGridLayoutHight(int row) {

            }
        });
    }

    public void setSubLayoutParameter(final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList){
        deviceLayout_one.setSubLayoutParameter(mapping, styleList);
    }

    public void setupGridLayout(Context context, boolean forceRefillContentGrid) {
        deviceLayout_one.setupGridLayout(context,forceRefillContentGrid);
    }

    public void setAdapter(GAdapter gAdapter){
        deviceLayout_one.setAdapter(gAdapter);
    }
}
