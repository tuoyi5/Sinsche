package com.sinsche.core.ws.client.android.struct;

import java.util.List;

public class DT550RealDataRspDevice implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7576489375003051835L;

    private String strName;//仪器名称
    private String strSerial;//序列号
    private String strTestTime;//测试时间
    private String strDeviceState;//仪器状态
    private boolean bWaterState;// 水样状态
    private boolean bTrashWaterState;//废液状态

    private List<DT550RealDataRspDeviceItem> item;

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public String getStrSerial() {
        return strSerial;
    }

    public void setStrSerial(String strSerial) {
        this.strSerial = strSerial;
    }

    public String getStrTestTime() {
        return strTestTime;
    }

    public void setStrTestTime(String strTestTime) {
        this.strTestTime = strTestTime;
    }

    public String getStrDeviceState() {
        return strDeviceState;
    }

    public void setStrDeviceState(String strDeviceState) {
        this.strDeviceState = strDeviceState;
    }

    public boolean isbWaterState() {
        return bWaterState;
    }

    public void setbWaterState(boolean bWaterState) {
        this.bWaterState = bWaterState;
    }

    public boolean isbTrashWaterState() {
        return bTrashWaterState;
    }

    public void setbTrashWaterState(boolean bTrashWaterState) {
        this.bTrashWaterState = bTrashWaterState;
    }

    public List<DT550RealDataRspDeviceItem> getItem() {
        return item;
    }

    public void setItem(List<DT550RealDataRspDeviceItem> item) {
        this.item = item;
    }
}
