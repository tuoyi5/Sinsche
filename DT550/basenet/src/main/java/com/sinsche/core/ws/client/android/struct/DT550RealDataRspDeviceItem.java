package com.sinsche.core.ws.client.android.struct;

public class DT550RealDataRspDeviceItem implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6589583346470822719L;

    private String strCode;// 项目列号
    private String strName;// 名字
    private String strData;// 数据
    private String strUnit;// 单位
    private boolean bWaterState;// 试剂状态，true,缺液。false,不缺
    private boolean bOverLevel;//是否超标，true，超标 false，不超标。

    public String getStrCode() {
        return strCode;
    }

    public void setStrCode(String strCode) {
        this.strCode = strCode;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public String getStrUnit() {
        return strUnit;
    }

    public void setStrUnit(String strUnit) {
        this.strUnit = strUnit;
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
}