package com.android.arvin.data;

/**
 * Created by tuoyi on 2017/9/13 0013.
 */

public class DeviceSubItemData {

    private String subItemDataCode;// 项目列号
    private String subItemDataName;// 名字
    private String subItemDataData;// 数据
    private String subItemDataWaterState;// 试剂状态
    private String subItemDataMax;// 上限
    private String subItemDataMin;// 下限
    private String subItemDataUnit;// 下限
    private boolean bOverLevel;// 是否超标
    private int subItemDataWaterTextBg = -1;
    private String subItemDataTestTime;


    public DeviceSubItemData(){

    }
    public DeviceSubItemData(
            String subItemDataCode,
            String subItemDataName,
            String subItemDataData,
            String subItemDataWaterState,
            String subItemDataMax,
            String subItemDataMin,
            String subItemDataUnit,
            boolean bOverLevel) {
        this.subItemDataCode = subItemDataCode;
        this.subItemDataName = subItemDataName;
        this.subItemDataData = subItemDataData;
        this.subItemDataWaterState = subItemDataWaterState;
        this.subItemDataMax = subItemDataMax;
        this.subItemDataMin = subItemDataMin;
        this.subItemDataUnit = subItemDataUnit;
        this.bOverLevel = bOverLevel;
    }

    public String getSubItemDataCode() {
        return subItemDataCode;
    }

    public void setSubItemDataCode(String subItemDataCode) {
        this.subItemDataCode = subItemDataCode;
    }

    public String getSubItemDataName() {
        return subItemDataName;
    }

    public void setSubItemDataName(String subItemDataName) {
        this.subItemDataName = subItemDataName;
    }

    public String getSubItemData() {
        return subItemDataData;
    }

    public void setSubItemData(String subItemDataData) {
        this.subItemDataData = subItemDataData;
    }

    public String getSubItemDataWaterState() {
        return subItemDataWaterState;
    }

    public void setSubItemDataWaterState(String subItemDataWaterState) {
        this.subItemDataWaterState = subItemDataWaterState;
    }

    public String getSubItemDataMax() {
        return subItemDataMax;
    }

    public void setSubItemDataMax(String subItemDataMax) {
        this.subItemDataMax = subItemDataMax;
    }

    public String getSubItemDataMin() {
        return subItemDataMin;
    }

    public void setSubItemDataMin(String subItemDataMin) {
        this.subItemDataMin = subItemDataMin;
    }

    public String getSubItemDataUnit() {
        return subItemDataUnit;
    }

    public void setSubItemDataUnit(String subItemDataUnit) {
        this.subItemDataUnit = subItemDataUnit;
    }

    public boolean isbOverLevel() {
        return bOverLevel;
    }

    public void setbOverLevel(boolean bOverLevel) {
        this.bOverLevel = bOverLevel;
    }

    public int getSubItemDataWaterTextBg() {
        return subItemDataWaterTextBg;
    }

    public void setSubItemDataWaterTextBg(int subItemDataWaterTextBg) {
        this.subItemDataWaterTextBg = subItemDataWaterTextBg;
    }

    public String getSubItemDataTestTime() {
        return subItemDataTestTime;
    }

    public void setSubItemDataTestTime(String subItemDataTestTime) {
        this.subItemDataTestTime = subItemDataTestTime;
    }
}
