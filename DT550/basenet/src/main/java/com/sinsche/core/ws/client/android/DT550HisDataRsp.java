package com.sinsche.core.ws.client.android;

import com.common.core.ws.client.UploadData;

public class DT550HisDataRsp extends UploadData {

    /**
     *
     */
    private static final long serialVersionUID = -5019278026138862436L;

    private String strDeviceSerial;
    private String strItemCode;

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
}
