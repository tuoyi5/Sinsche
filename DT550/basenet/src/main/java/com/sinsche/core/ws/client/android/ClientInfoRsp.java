package com.sinsche.core.ws.client.android;

import com.common.core.ws.client.UploadData;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;

import java.util.List;

public class ClientInfoRsp extends UploadData {

    /**
     *
     */
    private static final long serialVersionUID = -2004699317121227982L;

    private List<ClientInfoRspUserInfo> listClientInfoRspUserInfo;

    private String base64DT550RealDataRsp;

    public List<ClientInfoRspUserInfo> getListClientInfoRspUserInfo() {
        return listClientInfoRspUserInfo;
    }

    public void setListClientInfoRspUserInfo(List<ClientInfoRspUserInfo> listClientInfoRspUserInfo) {
        this.listClientInfoRspUserInfo = listClientInfoRspUserInfo;
    }

    public String getBase64DT550RealDataRsp() {
        return base64DT550RealDataRsp;
    }

    public void setBase64DT550RealDataRsp(String base64DT550RealDataRsp) {
        this.base64DT550RealDataRsp = base64DT550RealDataRsp;
    }
}
