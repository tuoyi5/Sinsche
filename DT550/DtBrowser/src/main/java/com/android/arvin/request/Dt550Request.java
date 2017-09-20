package com.android.arvin.request;

import android.content.Context;
import android.util.Log;

import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.data.DeviceSubItemData;
import com.android.arvin.util.GAdapter;
import com.android.arvin.util.GAdapterUtil;
import com.arvin.request.request.baserequest.BaseRequest;
import com.arvin.request.request.baserequest.RequestEnum;
import com.github.mikephil.charting.data.Entry;
import com.sinsche.core.ws.client.android.DT550HisDataRsp;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;
import com.sinsche.core.ws.client.android.struct.DT550HisDataRspItem;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDevice;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDeviceItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arvin on 2017/8/23 0023.
 */

public class Dt550Request extends BaseRequest {

    private static final String TAG = Dt550Request.class.getSimpleName();

    private RequestEnum requestEnum;
    private List<ClientInfoRspUserInfo> loginList;
    private List<DT550RealDataRspDevice> currentlyDataList;
    private DT550HisDataRsp dt550HisDataRsp;

    private Map<String, DeviceData> deviceDataMap = new HashMap<>();
    private DeviceData devicedata;

    private GAdapter gAdapter;
    private DeviceHistoryData deviceHistoryData;
    private ArrayList<Entry> hisDataList = new ArrayList<>();

    public Dt550Request(Context context) {
        super();
        setContext(context);
    }

    public void setLoginList(List<ClientInfoRspUserInfo> loginList) {
        this.loginList = loginList;
    }

    public void setCurrentlyDataList(List<DT550RealDataRspDevice> currentlyDataList) {
        this.currentlyDataList = currentlyDataList;
    }

    public void setDt550HisDataRsp(DT550HisDataRsp dt550HisDataRsp) {
        this.dt550HisDataRsp = dt550HisDataRsp;
    }

    public Map<String, DeviceData> getDeviceDataMap() {
        return deviceDataMap;
    }

    public void setDeviceDataMap(Map<String, DeviceData> map) {
        this.deviceDataMap = map;
    }

    public DeviceData getDevicedata() {
        return devicedata;
    }

    public void setDevicedata(DeviceData devicedata) {
        this.devicedata = devicedata;
    }

    public DeviceHistoryData getDeviceHistoryData() {
        return deviceHistoryData;
    }

    public void setDeviceHistoryData(DeviceHistoryData deviceHistoryData) {
        this.deviceHistoryData = deviceHistoryData;
    }

    public ArrayList<Entry> getHisDataList() {
        return hisDataList;
    }

    public GAdapter getGAdapter() {
        return gAdapter;
    }


    public void setRequestEnum(RequestEnum requestEnum) {
        this.requestEnum = requestEnum;
    }

    @Override
    public void execute(final BaseRequest request) {
        if (request instanceof Dt550Request)
            switch (((Dt550Request) request).requestEnum) {
                case RequestFormLogin:


                    break;
                case RequestFormCurrentlyData:

                    for (DT550RealDataRspDevice deviceItem : currentlyDataList) {
                        List<DT550RealDataRspDeviceItem> itemList = deviceItem.getItem();
                        List<DeviceSubItemData> subItemDatas = new ArrayList<>();
                        for (DT550RealDataRspDeviceItem item : itemList) {
                            DeviceSubItemData subItemData = new DeviceSubItemData();

                            subItemData.setSubItemDataCode(item.getStrCode());
                            subItemData.setSubItemDataName(item.getStrName());
                            subItemData.setSubItemDataValue(item.getStrData());
                            subItemData.setSubItemDataUnit(item.getStrUnit());
                            subItemData.setbWaterState(item.isbWaterState());
                            subItemData.setbOverLevel(item.isbOverLevel());
                            subItemData.setSubItemDataTestTime(deviceItem.getStrTestTime());
                            subItemDatas.add(subItemData);
                        }

                        DeviceData deviceData = new DeviceData(
                                deviceItem.getStrSerial(),
                                deviceItem.getStrName(),
                                deviceItem.getStrDeviceState(),
                                deviceItem.isbWaterState(),
                                deviceItem.isbTrashWaterState(),
                                subItemDatas);

                        if (deviceDataMap != null) {
                            deviceDataMap.put(deviceData.getDeviceCode(), deviceData);
                        }
                    }

                    break;
                case RequestFormHistoricData:

                    DeviceHistoryData deviceHistoryData = new DeviceHistoryData();
                    deviceHistoryData.setDeviceCode(dt550HisDataRsp.getStrDeviceSerial());
                    deviceHistoryData.setSubItemDataCode(dt550HisDataRsp.getStrItemCode());

                    List<DT550HisDataRspItem> list = dt550HisDataRsp.getListDT550HisDataRspItem();

                    for (DT550HisDataRspItem dt550HisDataRspItem : list) {

                        DeviceHistoryData.DeviceHisSubItemData deviceHisSubItemData = deviceHistoryData.getDeviceHisSubItemData(
                                dt550HisDataRspItem.getlTestTime(), dt550HisDataRspItem.getnFormat(), dt550HisDataRspItem.getDbData());

                        deviceHistoryData.getDeviceHisSubItemDataList().add(deviceHisSubItemData);
                    }
                    this.deviceHistoryData = deviceHistoryData;

                    ArrayList<Entry> values = new ArrayList<Entry>();
                    List<DeviceHistoryData.DeviceHisSubItemData> subItemDatas = this.deviceHistoryData.getDeviceHisSubItemDataList();
                    List<Float> yScope = new ArrayList<>();
                    for (DeviceHistoryData.DeviceHisSubItemData data : subItemDatas) {
                        float x = (float) data.getTestTime();
                        float y = (float) data.getDbData();
                        values.add(new Entry(x, y));
                        yScope.add(y);
                    }

                    Collections.sort(yScope);

                    if (dt550HisDataRsp.getStrMax() == null) {
                        float max = yScope.get(yScope.size() - 1);
                        this.deviceHistoryData.setStrMax(String.valueOf(max * 1.1));
                    } else {
                        this.deviceHistoryData.setStrMax(dt550HisDataRsp.getStrMax());
                    }

                    if (dt550HisDataRsp.getStrMin() == null) {
                        this.deviceHistoryData.setStrMin(String.valueOf(yScope.get(0)));
                    } else {
                        this.deviceHistoryData.setStrMin(dt550HisDataRsp.getStrMin());
                    }

                    hisDataList.addAll(values);

                    break;

                case RequestUpdateView:
                    if (devicedata == null) {
                        return;
                    }

                    List<DeviceSubItemData> subItemDataList = devicedata.getDeviceSubItemDatas();
                    if (subItemDataList == null || subItemDataList.size() == 0) {
                        return;
                    }

                    gAdapter = new GAdapter();
                    for (DeviceSubItemData deviceSubItemData : subItemDataList) {
                        gAdapter.addObject(GAdapterUtil.objectFromTestData(request.getContext(), deviceSubItemData));
                    }
                    break;

                case RequestShowDialog:

                    break;
            }
    }
}


