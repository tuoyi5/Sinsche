package com.android.arvin.data;

/**
 * Created by tuoyi on 2017/9/13 0013.
 */

public class DeviceSubItemData {


    private String SubItemDataCode;
    private String SubItemDataName;
    private String SubItemDataValue;
    private String SubItemDataUnit;
    private boolean bWaterState;
    private boolean bOverLevel;

    public String subItemDataTestTime;

    public DeviceSubItemData() {

    }

    public String getSubItemDataCode() {
        return SubItemDataCode;
    }

    public void setSubItemDataCode(String subItemDataCode) {
        SubItemDataCode = subItemDataCode;
    }

    public String getSubItemDataName() {
        return SubItemDataName;
    }

    public void setSubItemDataName(String subItemDataName) {
        SubItemDataName = subItemDataName;
    }

    public String getSubItemDataValue() {
        return SubItemDataValue;
    }

    public void setSubItemDataValue(String subItemDataValue) {
        SubItemDataValue = subItemDataValue;
    }

    public String getSubItemDataUnit() {
        return SubItemDataUnit;
    }

    public void setSubItemDataUnit(String subItemDataUnit) {
        SubItemDataUnit = subItemDataUnit;
    }

    public boolean isbWaterState() {
        return bWaterState;
    }

    public void setbWaterState(boolean bWaterState) {
        this.bWaterState = bWaterState;
    }

    public boolean isbOverLevel() {
        return bOverLevel;
    }

    public void setbOverLevel(boolean bOverLevel) {
        this.bOverLevel = bOverLevel;
    }

    public String getSubItemDataTestTime() {
        return subItemDataTestTime;
    }

    public void setSubItemDataTestTime(String subItemDataTestTime) {
        this.subItemDataTestTime = subItemDataTestTime;
    }
}
