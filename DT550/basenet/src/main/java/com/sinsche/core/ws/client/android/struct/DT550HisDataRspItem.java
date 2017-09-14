package com.sinsche.core.ws.client.android.struct;

public class DT550HisDataRspItem implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4528577771450897538L;
    /**
     *
     */

    private String strTestTime;// 测试时间
    private float fData;// 数据

    public String getStrTestTime() {
        return strTestTime;
    }

    public void setStrTestTime(String strTestTime) {
        this.strTestTime = strTestTime;
    }

    public float getfData() {
        return fData;
    }

    public void setfData(float fData) {
        this.fData = fData;
    }
}