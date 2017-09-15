package com.android.arvin.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.arvin.data.DeviceData;
import com.android.arvin.R;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.util.GAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by arvin on 2017/9/8 0008.
 */

public class DeviceLayout extends RelativeLayout {

    private static final String TAG = DeviceLayout.class.getSimpleName();
    private Context context;
    private View deviceLayout;

    private DeviceStatusLayout statusLayout;
    private DtContentView contentView;
    private DeviceFooterLayout footerLayout;

    private int contentViewItemInt = R.layout.content_view_item_layout;

    private ContentViewItemLayoutData itemLayoutData;
    private DeviceData deviceData;
    private List<DeviceHistoryData> deviceHistoryDataList = new ArrayList<>();

    private static class DevicdStatus {
        public static final String RUN = "run";
        public static final String WARN = "warn";
        public static final String ERROR = "error";
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public void setDeviceData(DeviceData deviceData) {
        this.deviceData = deviceData;
    }

    public List<DeviceHistoryData> getDeviceHistoryDataList() {
        return deviceHistoryDataList;
    }

    public void addDeviceHistoryData(DeviceHistoryData deviceHistoryData) {
        for (DeviceHistoryData data : deviceHistoryDataList) {
            if (data.getSubItemDataCode().equals(deviceHistoryData.getSubItemDataCode())) {
                data.setDeviceHisSubItemDataList(deviceHistoryData.getDeviceHisSubItemDataList());
            }
        }
        deviceHistoryDataList.add(deviceHistoryData);
    }

    public DeviceHistoryData getDeviceHistoryData(String itemCode){
        for (DeviceHistoryData data : deviceHistoryDataList) {
            if (data.getSubItemDataCode().equals(itemCode)) {
               return data;
            }
        }
        return null;
    }

    public void setOnClickCallBack(DeviceFooterLayout.OnClickCallBack onClickCallBack) {
        if (footerLayout != null)
            footerLayout.setOnClickCallBack(onClickCallBack);
    }

    public void setContentViewCallback(DtContentView.ContentViewCallback callback) {
        if (contentView != null)
            contentView.setContentViewCallback(callback);
    }

    public DeviceLayout(Context context, DeviceData device) {
        super(context);
        this.context = context;
        this.deviceData = device;

        if (deviceData == null) {
            Toast.makeText(context, context.getResources().getString(R.string.load_failed), Toast.LENGTH_LONG).show();
            return;
        }
        initData(device);
        initView(context);
    }

    private void initData(DeviceData device) {
        itemLayoutData = new ContentViewItemLayoutData();
        itemLayoutData.setGridDefaultShowRowCount(context.getResources().getInteger(R.integer.gridLayout_default_show_row));
        itemLayoutData.setGridColumnCount(context.getResources().getInteger(R.integer.gridLayout_column));
        itemLayoutData.setItemhight((int) context.getResources().getDimension(R.dimen.content_view_item_hight));

        int row;
        itemLayoutData.setItemSize(deviceData.getDeviceSubItemDatas().size());
        if (itemLayoutData.getItemSize() > 0) {
            row = (int) Math.ceil((double) itemLayoutData.getItemSize() / itemLayoutData.getGridColumnCount());

        } else {
            row = 0;
            Toast.makeText(context, context.getResources().getString(R.string.load_failed), Toast.LENGTH_LONG).show();
            return;
        }
        itemLayoutData.setGridRowCount(row);
        if (itemLayoutData.getGridRowCount() < itemLayoutData.getGridDefaultShowRowCount()) {
            itemLayoutData.setGridDefaultShowRowCount(itemLayoutData.getGridRowCount());
        }

    }

    private void initView(Context context) {
        this.context = context;
        RelativeLayout.LayoutParams layoutParams;
        deviceLayout = LayoutInflater.from(context).inflate(R.layout.device_layout, null);
        layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        deviceLayout.setLayoutParams(layoutParams);

        statusLayout = (DeviceStatusLayout) deviceLayout.findViewById(R.id.device_title);
        layoutParams = (RelativeLayout.LayoutParams) statusLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        statusLayout.setLayoutParams(layoutParams);
        statusLayout.setDeviceLayout(this);

        contentView = (DtContentView) deviceLayout.findViewById(R.id.device_grid);
        layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = itemLayoutData.getItemhight() * itemLayoutData.getGridDefaultShowRowCount();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        contentView.setLayoutParams(layoutParams);
        contentView.setDeviceLayout(this);
        contentView.setContentViewItemData(itemLayoutData);

        footerLayout = (DeviceFooterLayout) deviceLayout.findViewById(R.id.device_footer);
        layoutParams = (RelativeLayout.LayoutParams) footerLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        footerLayout.setLayoutParams(layoutParams);
        footerLayout.setDeviceLayout(this);

        updateDeviceLayout();
        addView(deviceLayout);
    }

    public void updateGridLayoutHight(boolean fold) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = itemLayoutData.getItemhight() * (fold ? itemLayoutData.getGridDefaultShowRowCount() : itemLayoutData.getGridRowCount());
        contentView.setLayoutParams(layoutParams);
        footerLayout.setMoreText(fold ? context.getString(R.string.view_more_project) : context.getString(R.string.view_pack_up_project));
        footerLayout.setMoreImageView(fold ? R.drawable.more : R.drawable.puck_up);
    }

    public void setSubLayoutParameter(final HashMap<String, Integer> mapping, final ArrayList<Integer> styleList) {
        if (contentView != null)
            contentView.setSubLayoutParameter(contentViewItemInt, mapping, styleList);
    }

    public void setAdapter(GAdapter gAdapter) {
        contentView.setAdapter(gAdapter);
    }

    public void updateDeviceLayout() {
        setDeviceTitle();
        setDeviceStatus();
        setFooterLayout();
    }

    private void setDeviceTitle() {
        statusLayout.setTitleText(deviceData.getDeviceName());
    }

    private void setDeviceStatus() {
        int drawable = -1;
        switch (deviceData.getDeviceRunningStatus()) {
            case DevicdStatus.RUN:
                drawable = R.drawable.device_status_normal;
                break;
            case DevicdStatus.WARN:
                drawable = R.drawable.device_status_warning;
                break;
            case DevicdStatus.ERROR:
                drawable = R.drawable.device_status_warning;
                break;
            default:
                drawable = R.drawable.device_status_normal;
        }

        statusLayout.setStatusImageView(drawable);
    }

    private void setFooterLayout() {
        footerLayout.setWaterStatusImageView(deviceData.isWaterStatus() ? R.drawable.water_status_yes : R.drawable.water_status_yes);
    }


}
