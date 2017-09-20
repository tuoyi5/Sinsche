package com.android.arvin.Manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.interfaces.UpdateDeviceLayouDataCallback;
import com.android.arvin.interfaces.UpdateDialogCallback;
import com.android.arvin.request.Dt550Request;
import com.android.arvin.util.GAdapter;
import com.arvin.request.request.baserequest.BaseCallback;
import com.arvin.request.request.baserequest.BaseRequest;
import com.arvin.request.request.baserequest.RequestEnum;
import com.arvin.request.request.baserequest.RequestManager;
import com.github.mikephil.charting.data.Entry;
import com.qq408543103.basenet.AuthorClient;
import com.qq408543103.basenet.interfaces.DataCallback;
import com.sinsche.core.ws.client.android.DT550HisDataRsp;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvin on 2017/9/11 0011.
 */

public class DeviceManager implements DataCallback {

    private static final String TAG = DeviceManager.class.getSimpleName();
    private static final int REQUEST_REAL_TIME_DATA = 0xFFFFFF1;
    private static final int REQUEST_REAL_TIME_DATA_TIME_DELAY = 60 * 1000;
    private Context context;
    private AuthorClient authorClient;
    private RequestManager requestManager;

    private UpdateDialogCallback updateDialogCallback;

    private List<ClientInfoRspUserInfo> clientInfoRspUserInfoList;
    private List<DT550RealDataRspDevice> dt550RealDataRspDeviceList;
    private DT550HisDataRsp dt550HisDataRsp;

    private static List<UpdateDeviceLayouDataCallback> updateDeviceLayouDataCallbacks = new ArrayList<>();
    private static DeviceManager deviceManager;

    public static DeviceManager instantiation(Context context, UpdateDeviceLayouDataCallback callback) {
        if (deviceManager == null) {
            deviceManager = new DeviceManager(context, callback);
        } else {
            if (deviceManager.getContext() != context) {
                deviceManager.setContext(context);
                updateDeviceLayouDataCallbacks.add(callback);
            }
        }

        return deviceManager;
    }


    public DeviceManager(Context context, UpdateDeviceLayouDataCallback callback) {
        this.context = context;
        authorClient = new AuthorClient();
        authorClient.setDataCallback(this);
        requestManager = new RequestManager();
        updateDeviceLayouDataCallbacks.add(callback);

        authorClient.Start("182.254.158.210", 7010, "BE22993A-75628875-23B3C323-09A5D5A1", "AndroidAPP", context.getCacheDir().getAbsolutePath());
        requestRealTimeData();
    }

    public void stop() {
        if (authorClient != null) {
            authorClient.Stop();
            authorClient = null;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_REAL_TIME_DATA:
                    requestRealTimeData();
                    break;
            }
        }
    };

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUpdateDialogCallback(UpdateDialogCallback updateDialogCallback) {
        this.updateDialogCallback = updateDialogCallback;
    }

    public void setUpdateDeviceLayouDataCallback(UpdateDeviceLayouDataCallback updateDeviceLayouDataCallback) {
        this.updateDeviceLayouDataCallbacks.add(updateDeviceLayouDataCallback);
    }

    public List<DT550RealDataRspDevice> getDt550RealDataRspDeviceList() {
        return dt550RealDataRspDeviceList;
    }

    @Override
    public void getClientInfoRspUserInfoList(List<ClientInfoRspUserInfo> list) {
        //requestFormLogin(list);
        clientInfoRspUserInfoList = list;
        for (UpdateDeviceLayouDataCallback callback : updateDeviceLayouDataCallbacks) {
            if (callback != null) {
                callback.releaseLoginData(list);
            }
        }
    }

    @Override
    public void getDataRspDeviceList(List<DT550RealDataRspDevice> list) {
        dt550RealDataRspDeviceList = list;
        requestFormCurrentlyData(list);
    }

    @Override
    public void getDT550HisDataRsp(DT550HisDataRsp dt550HisDataRsp) {
        this.dt550HisDataRsp = dt550HisDataRsp;
        requestFormHistoricData(dt550HisDataRsp);

    }

    public void requestRealTimeData() {
        authorClient.RequestRealTimeData();
        handler.removeMessages(REQUEST_REAL_TIME_DATA);
        handler.sendEmptyMessageDelayed(REQUEST_REAL_TIME_DATA, REQUEST_REAL_TIME_DATA_TIME_DELAY);
    }


    public void requestFormLogin(List<ClientInfoRspUserInfo> loginList) {
        Dt550Request request = new Dt550Request(context);
        request.setContext(context);
        request.setRequestEnum(RequestEnum.RequestFormLogin);
        request.setLoginList(loginList);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {

                }
            }
        });
    }

    public void requestFormCurrentlyData(List<DT550RealDataRspDevice> currentlyDataList) {
        Dt550Request request = new Dt550Request(context);
        request.setContext(context);
        request.setRequestEnum(RequestEnum.RequestFormCurrentlyData);
        request.setCurrentlyDataList(currentlyDataList);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {

                    for (UpdateDeviceLayouDataCallback callback : updateDeviceLayouDataCallbacks) {
                        if (callback != null) {
                            callback.releaseDeviceDataBack(((Dt550Request) request).getDeviceDataMap());
                        }
                    }
                }
            }
        });
    }

    private void requestFormHistoricData(DT550HisDataRsp dt550HisDataRsp) {
        Dt550Request request = new Dt550Request(context);
        request.setRequestEnum(RequestEnum.RequestFormHistoricData);
        request.setContext(context);
        request.setDt550HisDataRsp(dt550HisDataRsp);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {
                    DeviceHistoryData deviceHistoryData = ((Dt550Request) request).getDeviceHistoryData();


                    for (UpdateDeviceLayouDataCallback callback : updateDeviceLayouDataCallbacks) {
                        if (callback != null) {
                            callback.releaseDeviceHisDataBack(deviceHistoryData);
                        }
                    }

                    if (updateDialogCallback != null) {
                        ArrayList<Entry> entries = ((Dt550Request) request).getHisDataList();

                        updateDialogCallback.releaseEntrys(deviceHistoryData, entries);
                    }

                }
            }
        });
    }

    public void requestUpdateView(Context context, final DeviceData deviceData) {
        Dt550Request request = new Dt550Request(context);
        request.setRequestEnum(RequestEnum.RequestUpdateView);
        request.setDevicedata(deviceData);

        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(final BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {
                    final GAdapter gAdapter = ((Dt550Request) request).getGAdapter();

                    for (UpdateDeviceLayouDataCallback callback : updateDeviceLayouDataCallbacks) {
                        if (callback != null) {
                            callback.getGadpterBack(((Dt550Request) request).getDevicedata().getDeviceCode(), gAdapter);
                        }
                    }

                }
            }
        });
    }

    public void requestUpdateDialog(String deviceCode, String itemCode) {
        if (deviceCode == null || itemCode == null) {
            return;
        }
        authorClient.RequestHisData(deviceCode, itemCode);
    }
}
