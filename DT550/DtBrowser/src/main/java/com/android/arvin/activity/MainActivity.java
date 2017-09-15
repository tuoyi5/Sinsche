package com.android.arvin.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.arvin.data.DeviceData;
import com.android.arvin.R;
import com.android.arvin.Manager.DeviceManager;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.data.GObject;
import com.android.arvin.interfaces.UpdateDeviceLayouDataCallback;
import com.android.arvin.ui.ContentItemView;
import com.android.arvin.ui.DeviceFooterLayout;
import com.android.arvin.ui.DeviceLayout;
import com.android.arvin.ui.Dialog.DeviceDialog;
import com.android.arvin.ui.DtContentView;
import com.android.arvin.util.DeviceConfig;
import com.android.arvin.util.GAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends DtAppCompatActivity implements UpdateDeviceLayouDataCallback {

    final static String TAG = MainActivity.class.getSimpleName();

    final private Context context = this;
    private Map<String, DeviceLayout> deviceMap = new HashMap<String, DeviceLayout>();
    private ScrollView device_scrollView;
    private LinearLayout deviceFatherFayout;

    //测试数据加载
    private DeviceManager deviceManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSupportActionBarWithCustomBackFunction();
        initActionBar();
        initView();

        deviceManager = new DeviceManager(this, this);
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
        device_scrollView = (ScrollView) findViewById(R.id.device_scrollView);
        deviceFatherFayout = (LinearLayout) findViewById(R.id.device_father_layout);
    }

    private void initSubView(DeviceLayout deviceLayout) {
        HashMap<String, Integer> mapping = new HashMap<String, Integer>();
        mapping.put(DeviceConfig.MEASURE_ITEM_LIQUID_STATE, R.id.measure_item_liquid_state_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_NAME, R.id.measure_item_name_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_VALUE, R.id.measure_item_value_text);
        mapping.put(DeviceConfig.MEASURE_ITEM_TIME, R.id.measure_item_time_text);
        ArrayList<Integer> styleList = new ArrayList<>();
        deviceLayout.setSubLayoutParameter(mapping, styleList);
    }

    private void showLoginDialog(String deviceCode, String itemCode) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        DeviceDialog dialog = DeviceDialog.instance();
        dialog.setDeviceCode(deviceCode);
        dialog.setItemCode(itemCode);
        dialog.setDeviceManager(deviceManager);
        dialog.show(ft, "hisData");
    }


    private void addDeviceView(final DeviceData deviceData) {
        final DeviceLayout deviceLayout = new DeviceLayout(this, deviceData);
        deviceMap.put(deviceData.getDeviceCode(), deviceLayout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        deviceLayout.setLayoutParams(layoutParams);

        if (deviceFatherFayout == null) return;

        deviceFatherFayout.addView(deviceLayout);

        deviceLayout.setOnClickCallBack(new DeviceFooterLayout.OnClickCallBack() {
            @Override
            public void onClickCallBack(final DeviceLayout device, final boolean fold) {

                if (device != null) {
                    device.updateGridLayoutHight(fold);
                }

                if (fold) {
                    //需要重新确定定位的位置.
                    // device_scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            }
        });

        deviceLayout.setContentViewCallback(new DtContentView.ContentViewCallback() {

            @Override
            public void uiLoading(DeviceLayout device) {
                device.updateDeviceLayout();
                if (deviceManager != null)
                    deviceManager.requestUpdateView(context, device.getDeviceData());
            }

            @Override
            public void beforeSetupData(ContentItemView view, GObject object) {

            }

            @Override
            public void onItemClick(DeviceLayout device, ContentItemView view) {
                String subItemCode = view.getDataObject().getString(DeviceConfig.MEASURE_ITEM_CODE);
                showLoginDialog(device.getDeviceData().getDeviceCode(), subItemCode);
            }

            @Override
            public boolean onItemLongClick(ContentItemView view) {
                return false;
            }
        });

        initSubView(deviceLayout);
    }

    private void updateDeviceDataView(final DeviceLayout deviceLayout, final DeviceData deviceData) {
        deviceLayout.setDeviceData(deviceData);
        deviceLayout.updateDeviceLayout();
        if (deviceManager != null)
            deviceManager.requestUpdateView(context, deviceLayout.getDeviceData());
    }



    @Override
    public void releaseDeviceDataBack(DeviceData deviceData) {
        DeviceLayout deviceLayout = deviceMap.get(deviceData.getDeviceCode());
        if (deviceLayout != null) {
            updateDeviceDataView(deviceLayout, deviceData);
        } else {
            addDeviceView(deviceData);
        }
    }

    @Override
    public void releaseDeviceHisDataBack(final DeviceHistoryData historyData) {
        String deviceCode = historyData.getDeviceCode();
        final DeviceLayout deviceLayout = deviceMap.get(deviceCode);
        if (deviceLayout != null) {
            deviceLayout.addDeviceHistoryData(historyData);
        }
    }

    @Override
    public void getGadpterBack(String deviceCode, GAdapter gAdapter) {
        DeviceLayout deviceLayout = deviceMap.get(deviceCode);
        deviceLayout.setAdapter(gAdapter);
    }
}
