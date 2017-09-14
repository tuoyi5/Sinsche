package com.android.arvin.Manager;

import android.content.Context;

import com.android.arvin.interfaces.UpdateUiDataCallback;
import com.android.arvin.request.Dt550Request;
import com.android.arvin.ui.DeviceLayout;
import com.arvin.request.request.baserequest.BaseCallback;
import com.arvin.request.request.baserequest.BaseRequest;
import com.arvin.request.request.baserequest.RequestEnum;
import com.arvin.request.request.baserequest.RequestManager;
import com.qq408543103.basenet.AuthorClient;
import com.qq408543103.basenet.interfaces.DataCallback;
import com.sinsche.core.ws.client.android.DT550HisDataRsp;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDevice;
import com.sinsche.core.ws.client.android.struct.DT550HisDataRspItem;

import java.util.List;

/**
 * Created by arvin on 2017/9/11 0011.
 */

public class DataReception implements DataCallback {

    private Context context;
    private AuthorClient authorClient;
    private RequestManager requestManager;
    private UpdateUiDataCallback updateUiDataCallback;

    public DataReception(Context context, UpdateUiDataCallback callback) {
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
        Dt550Request request = new Dt550Request();
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
        Dt550Request request = new Dt550Request();
        request.setContext(context);
        request.setRequestEnum(RequestEnum.RequestFormCurrentlyData);
        request.setCurrentlyDataList(currentlyDataList);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {
                    updateUiDataCallback.updateDeviceView(((Dt550Request) request).getDeviceData());
                }
            }
        });
    }

    public void requestFormHistoricData(DT550HisDataRsp dt550HisDataRsp) {
        Dt550Request request = new Dt550Request();
        request.setContext(context);
        request.setRequestEnum(RequestEnum.RequestFormHistoricData);
        request.setDt550HisDataRsp(dt550HisDataRsp);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {

                }
            }
        });
    }
}

