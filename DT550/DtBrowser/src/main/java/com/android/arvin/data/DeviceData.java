package com.android.arvin.data;

import java.util.List;

/**
 * Created by tuoyi on 2017/9/10 0010.
 */

public class DeviceData {

    private String deviceCode;
    private String deviceName;
    private String deviceRunningStatus;
    private boolean waterStatus;
    private boolean bTrashWaterState;
    private List<DeviceSubItemData> deviceSubItemDatas;

    public DeviceData(String deviceCode,
                      String deviceName,
                      String deviceRunningStatus,
                      boolean waterStatus,
                      boolean bTrashWaterState,
                      List<DeviceSubItemData> deviceSubItemDatas) {
        this.deviceCode = deviceCode;
        this.deviceName = deviceName;
        this.deviceRunningStatus = deviceRunningStatus;
        this.waterStatus = waterStatus;
        this.bTrashWaterState = bTrashWaterState;
        this.deviceSubItemDatas = deviceSubItemDatas;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceRunningStatus() {
        return deviceRunningStatus;
    }

    public void setDeviceRunningStatus(String deviceRunningStatus) {
        this.deviceRunningStatus = deviceRunningStatus;
    }

    public boolean isWaterStatus() {
        return waterStatus;
    }

    public void setWaterStatus(boolean waterStatus) {
        this.waterStatus = waterStatus;
    }

    public boolean isbTrashWaterState() {
        return bTrashWaterState;
    }

    public void setbTrashWaterState(boolean bTrashWaterState) {
        this.bTrashWaterState = bTrashWaterState;
    }

    public List<DeviceSubItemData> getDeviceSubItemDatas() {
        return deviceSubItemDatas;
    }

    public void setDeviceSubItemDatas(List<DeviceSubItemData> deviceSubItemDatas) {
        this.deviceSubItemDatas.clear();
        this.deviceSubItemDatas.addAll(deviceSubItemDatas);
    }
}
