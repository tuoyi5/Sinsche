package com.android.arvin.Manager;

import android.content.Context;
import android.util.Log;

import com.android.arvin.data.DeviceData;
import com.android.arvin.data.DeviceHistoryData;
import com.android.arvin.interfaces.UpdateUiDataCallback;
import com.android.arvin.request.Dt550Request;
import com.android.arvin.ui.DeviceLayout;
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
import com.sinsche.core.ws.client.android.struct.DT550HisDataRspItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arvin on 2017/9/11 0011.
 */

public class DataManager implements DataCallback {

    private static final String TAG = DataManager.class.getSimpleName();
    private Context context;
    private AuthorClient authorClient;
    private RequestManager requestManager;
    private UpdateUiDataCallback updateUiDataCallback;

    public DataManager(Context context, UpdateUiDataCallback callback) {
        this.context = context;
        authorClient = new AuthorClient();
        authorClient.setDataCallback(this);
        requestManager = new RequestManager();
        updateUiDataCallback = callback;

        authorClient.Start("182.254.158.210", 7010, "BE22993A-75628875-23B3C323-09A5D5A1", "AndroidAPP", context.getCacheDir().getAbsolutePath());
    }

    @Override
    public void getClientInfoRspUserInfoList(List<ClientInfoRspUserInfo> list) {
        requestFormLogin(list);
    }

    @Override
    public void getDataRspDeviceList(List<DT550RealDataRspDevice> list) {

        requestFormCurrentlyData(list);
    }

    @Override
    public void getDT550HisDataRsp(DT550HisDataRsp dt550HisDataRsp) {

        requestFormHistoricData(dt550HisDataRsp);

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
        Log.d(TAG,  "requestFormCurrentlyData");
        Dt550Request request = new Dt550Request(context);
        request.setContext(context);
        request.setRequestEnum(RequestEnum.RequestFormCurrentlyData);
        request.setCurrentlyDataList(currentlyDataList);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {
                    updateUiDataCallback.releaseDeviceDataBack(((Dt550Request) request).getDeviceData());
                }
            }
        });
    }

    public void requestFormHistoricData(DT550HisDataRsp dt550HisDataRsp) {
        Log.d(TAG,  "requestFormHistoricData");
        Dt550Request request = new Dt550Request(context);
        request.setRequestEnum(RequestEnum.RequestFormHistoricData);
        request.setContext(context);
        request.setDt550HisDataRsp(dt550HisDataRsp);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {
                    DeviceHistoryData deviceHistoryData = ((Dt550Request) request).getDeviceHistoryData();
                    updateUiDataCallback.releaseDeviceHisDataBack(deviceHistoryData);
                }
            }
        });
    }

    public void requestUpdateView(Context context, final DeviceData deviceData) {
        Dt550Request request = new Dt550Request(context);
        request.setRequestEnum(RequestEnum.RequestUpdateView);
        request.setDeviceData(deviceData);

        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(final BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {
                    final GAdapter gAdapter = ((Dt550Request) request).getGAdapter();
                    updateUiDataCallback.getGadpterBack(((Dt550Request) request).getDeviceData().getDeviceCode(), gAdapter);
                }
            }
        });
    }

    public void requestShowLoginDialog(Context context, DeviceHistoryData historyData) {
        if (historyData == null) {
            return;
        }

        Dt550Request request = new Dt550Request(context);
        request.setRequestEnum(RequestEnum.RequestShowDialog);
        request.setDeviceHistoryData(historyData);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(final BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {
                    ArrayList<Entry> hisDataList =  ((Dt550Request) request).getHisDataList();

                }
            }
        });

    }
}
