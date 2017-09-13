package com.android.arvin.data;

import java.util.List;

/**
 * Created by tuoyi on 2017/9/10 0010.
 */

public class DeviceData {

    private String deviceCode;
    private String deviceName;
    private String deviceRunningStatus;
    private String waterStatus;
    private List<DeviceSubItemData> deviceSubItemDatas;

    public DeviceData(String deviceCode,
                      String deviceName,
                      String deviceRunningStatus,
                      String waterStatus,
                      List<DeviceSubItemData> deviceSubItemDatas) {
        this.deviceCode = deviceCode;
        this.deviceName = deviceName;
        this.deviceRunningStatus = deviceRunningStatus;
        this.waterStatus = waterStatus;
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

    public String getWaterStatus() {
        return waterStatus;
    }

    public void setWaterStatus(String waterStatus) {
        this.waterStatus = waterStatus;
    }

    public List<DeviceSubItemData> getDeviceSubItemDatas() {
        return deviceSubItemDatas;
    }

    public void setDeviceSubItemDatas(List<DeviceSubItemData> deviceSubItemDatas) {
        this.deviceSubItemDatas = deviceSubItemDatas;
    }
}
