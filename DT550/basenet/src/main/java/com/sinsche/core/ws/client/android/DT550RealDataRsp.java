package com.sinsche.core.ws.client.android;

import com.common.core.ws.client.UploadData;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDevice;

import java.util.List;

public class DT550RealDataRsp extends UploadData {

    /**
     *
     */
    private static final long serialVersionUID = -8254159955337206590L;


    private List<DT550RealDataRspDevice> listDT550RealDataRspDevice;

    public List<DT550RealDataRspDevice> getListDT550RealDataRspDevice() {
        return listDT550RealDataRspDevice;
    }

    public void setListDT550RealDataRspDevice(List<DT550RealDataRspDevice> listDT550RealDataRspDevice) {
        this.listDT550RealDataRspDevice = listDT550RealDataRspDevice;
    }
}
