package com.sinsche.core.ws.client.android.struct;

public class DT55HisDataRspItem implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4528577771450897538L;
    /**
     *
     */

    private String strTestTime;// 测试时间
    private String strData;// 数据

    public String getStrTestTime() {
        return strTestTime;
    }

    public void setStrTestTime(String strTestTime) {
        this.strTestTime = strTestTime;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }
}