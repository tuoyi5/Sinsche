package com.android.arvin.DataText;

import java.util.List;

/**
 * Created by tuoyi on 2017/9/10 0010.
 */

public class DeviceTest {

    private String deviceName;
    private boolean deviceRunningStatus;
    private boolean waterStatus;
    private List<SubItemTest> subItemTestList;

    public DeviceTest(){

    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isDeviceRunningStatus() {
        return deviceRunningStatus;
    }

    public void setDeviceRunningStatus(boolean deviceRunningStatus) {
        this.deviceRunningStatus = deviceRunningStatus;
    }

    public boolean isWaterStatus() {
        return waterStatus;
    }

    public void setWaterStatus(boolean waterStatus) {
        this.waterStatus = waterStatus;
    }

    public List<SubItemTest> getSubItemTestList() {
        return subItemTestList;
    }

    public void setSubItemTestList(List<SubItemTest> subItemTestList) {
        this.subItemTestList = subItemTestList;
    }
}
