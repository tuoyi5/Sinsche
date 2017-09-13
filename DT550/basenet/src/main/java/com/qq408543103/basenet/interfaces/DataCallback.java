package com.qq408543103.basenet.interfaces;

import com.sinsche.core.ws.client.android.DT550HisDataRsp;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDevice;
import com.sinsche.core.ws.client.android.struct.DT550HisDataRspItem;

import java.util.List;

/**
 * Created by arvin on 2017/9/11 0011.
 */

public interface DataCallback {

    public void getClientInfoRspUserInfoList(List<ClientInfoRspUserInfo> list);

    public void getDataRspDeviceList(List<DT550RealDataRspDevice> list);

    public void getDT550HisDataRsp(DT550HisDataRsp dt550HisDataRsp);
}

