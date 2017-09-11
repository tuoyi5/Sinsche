package com.sinsche.core.ws.client.android;

import com.common.core.ws.client.UploadData;
import com.sinsche.core.ws.client.android.struct.DT55HisDataRspItem;

import java.util.List;

public class DT550HisDataRsp extends UploadData {

    /**
     *
     */
    private static final long serialVersionUID = -5019278026138862436L;

    private String strDeviceSerial;//仪器序列号
    private String strItemCode;//项目编号

    private List<DT55HisDataRspItem> listDT55HisDataRspItem = null;

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

    public List<DT55HisDataRspItem> getListDT55HisDataRspItem() {
        return listDT55HisDataRspItem;
    }

    public void setListDT55HisDataRspItem(List<DT55HisDataRspItem> listDT55HisDataRspItem) {
        this.listDT55HisDataRspItem = listDT55HisDataRspItem;
    }
}
