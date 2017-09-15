package com.android.arvin.interfaces;

import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.util.GAdapter;
import com.github.mikephil.charting.data.Entry;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvin on 2017/9/11 0011.
 */

public interface UpdateDeviceLayouDataCallback {

    public void releaseLoginData(List<ClientInfoRspUserInfo> list);

    public void releaseDeviceDataBack(DeviceData deviceData);

    public void releaseDeviceHisDataBack(DeviceHistoryData historyData);

    public void getGadpterBack(String deviceCode, GAdapter gAdapter);
}

