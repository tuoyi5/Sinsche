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

    public List<ClientInfoRspUserInfo> getListClientInfoRspUserInfo() {
        return listClientInfoRspUserInfo;
    }

    public void setListClientInfoRspUserInfo(List<ClientInfoRspUserInfo> listClientInfoRspUserInfo) {
        this.listClientInfoRspUserInfo = listClientInfoRspUserInfo;
    }
}
