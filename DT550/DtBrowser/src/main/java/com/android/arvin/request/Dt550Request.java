package com.android.arvin.request;

import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceSubItemData;
import com.android.arvin.R;
import com.arvin.request.request.baserequest.BaseRequest;
import com.arvin.request.request.baserequest.RequestEnum;
import com.sinsche.core.ws.client.android.DT550HisDataRsp;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDevice;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDeviceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvin on 2017/8/23 0023.
 */

public class Dt550Request extends BaseRequest {

    private RequestEnum requestEnum;

    private List<ClientInfoRspUserInfo> loginList;
    private List<DT550RealDataRspDevice> currentlyDataList;
    private DT550HisDataRsp dt550HisDataRsp;

    private DeviceData deviceData;

    public void setLoginList(List<ClientInfoRspUserInfo> loginList) {
        this.loginList = loginList;
    }

    public void setCurrentlyDataList(List<DT550RealDataRspDevice> currentlyDataList) {
        this.currentlyDataList = currentlyDataList;
    }

    public void setDt550HisDataRsp(DT550HisDataRsp dt550HisDataRsp) {
        this.dt550HisDataRsp = dt550HisDataRsp;
    }

    public DeviceData getDeviceData() {
        return deviceData;
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
                            subItemData.setSubItemDataName( item.getStrName());
                            subItemData.setSubItemData( item.getStrData());
                            subItemData.setSubItemDataWaterState(  item.getStrWaterState());
                            subItemData.setSubItemDataMax(item.getStrMax());
                             subItemData.setSubItemDataMin(item.getStrMin());
                            subItemData.setSubItemDataUnit( item.getStrUnit());
                            subItemData.setbOverLevel(item.isbOverLevel());

                            if (item.getStrWaterState().equals("")) {
                                subItemData.setSubItemDataWaterTextBg(R.drawable.liquid_state_text_yes_bg);
                            } else {
                                subItemData.setSubItemDataWaterTextBg(R.drawable.liquid_state_text_no_bg);
                            }
                            subItemData.setSubItemDataTestTime(deviceItem.getStrTestTime());
                            subItemDatas.add(subItemData);
                        }

                        deviceData = new DeviceData(
                                deviceItem.getStrSerial(),
                                deviceItem.getStrName(),
                                deviceItem.getStrState1(),
                                deviceItem.getStrState4(),
                                subItemDatas);
                    }

                    break;
                case RequestFormHistoricData:

                    break;
            }
    }
}


