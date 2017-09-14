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
    private int nFormat;//数据格式化显示的位数
    private double dbData;// 数据

    public String getStrTestTime() {
        return strTestTime;
    }

    public void setStrTestTime(String strTestTime) {
        this.strTestTime = strTestTime;
    }

    public double getDbData() {
        return dbData;
    }

    public void setDbData(double dbData) {
        this.dbData = dbData;
    }



    public int getnFormat() {
        return nFormat;
    }

    public void setnFormat(int nFormat) {
        this.nFormat = nFormat;
    }
}