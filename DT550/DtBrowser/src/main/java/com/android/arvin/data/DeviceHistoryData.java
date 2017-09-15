package com.android.arvin.data;

import com.sinsche.core.ws.client.android.struct.DT550HisDataRspItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuoyi on 2017/9/14 0014.
 */

public class DeviceHistoryData {

    private String deviceCode;
    private String SubItemDataCode;//项目编号
    private String strMax;// 上限
    private String strMin;// 下限
    private List<DeviceHisSubItemData> deviceHisSubItemDataList = new ArrayList<>();

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getSubItemDataCode() {
        return SubItemDataCode;
    }

    public void setSubItemDataCode(String subItemDataCode) {
        SubItemDataCode = subItemDataCode;
    }

    public String getStrMax() {
        return strMax;
    }

    public void setStrMax(String strMax) {
        this.strMax = strMax;
    }

    public String getStrMin() {
        return strMin;
    }

    public void setStrMin(String strMin) {
        this.strMin = strMin;
    }

    public List<DeviceHisSubItemData> getDeviceHisSubItemDataList() {
        return deviceHisSubItemDataList;
    }

    public void setDeviceHisSubItemDataList(List<DeviceHisSubItemData> deviceHisSubItemDataList) {
        this.deviceHisSubItemDataList.clear();
        this.deviceHisSubItemDataList.addAll(deviceHisSubItemDataList);
    }

    public DeviceHisSubItemData getDeviceHisSubItemData(String strTestTime, int nFormat, double dbData) {
        return new DeviceHisSubItemData(strTestTime, nFormat, dbData);
    }

    public class DeviceHisSubItemData {
        private String strTestTime;// 测试时间
        private int nFormat;//数据格式化显示的位数
        private double dbData;// 数据

        public DeviceHisSubItemData(String strTestTime, int nFormat, double dbData) {
            this.strTestTime = strTestTime;
            this.nFormat = nFormat;
            this.dbData = dbData;
        }

        public String getStrTestTime() {
            return strTestTime;
        }

        public void setStrTestTime(String strTestTime) {
            this.strTestTime = strTestTime;
        }

        public int getnFormat() {
            return nFormat;
        }

        public void setnFormat(int nFormat) {
            this.nFormat = nFormat;
        }

        public double getDbData() {
            return dbData;
        }

        public void setDbData(double dbData) {
            this.dbData = dbData;
        }
    }
}
