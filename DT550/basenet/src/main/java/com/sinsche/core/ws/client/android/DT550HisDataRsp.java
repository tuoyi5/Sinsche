package com.sinsche.core.ws.client.android;

import com.common.core.ws.client.UploadData;
import com.sinsche.core.ws.client.android.struct.DT550HisDataRspItem;

import java.util.List;

public class DT550HisDataRsp extends UploadData {

    /**
     *
     */
    private static final long serialVersionUID = -5019278026138862436L;

    private String strDeviceSerial;//仪器序列号
    private String strItemCode;//项目编号
    private String strMax;// 上限
    private String strMin;// 下限

    private List<DT550HisDataRspItem> listDT550HisDataRspItem = null;

    public String getStrDeviceSerial() {
        return strDeviceSerial;
    }

    public void setStrDeviceSerial(String strDeviceSerial) {
        this.strDeviceSerial = strDeviceSerial;
    }

    public String getStrItemCode() {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode) {
        this.strItemCode = strItemCode;
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

    public List<DT550HisDataRspItem> getListDT550HisDataRspItem() {
        return listDT550HisDataRspItem;
    }

    public void setListDT550HisDataRspItem(List<DT550HisDataRspItem> listDT550HisDataRspItem) {
        this.listDT550HisDataRspItem = listDT550HisDataRspItem;
    }
}
