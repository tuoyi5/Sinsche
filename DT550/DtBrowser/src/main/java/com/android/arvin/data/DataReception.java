package com.android.arvin.data;

import android.content.Context;

import com.android.arvin.request.Dt550Request;
import com.arvin.request.request.baserequest.BaseCallback;
import com.arvin.request.request.baserequest.BaseRequest;
import com.arvin.request.request.baserequest.RequestEnum;
import com.arvin.request.request.baserequest.RequestManager;
import com.qq408543103.basenet.AuthorClient;
import com.qq408543103.basenet.interfaces.DataCallback;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDevice;
import com.sinsche.core.ws.client.android.struct.DT55HisDataRspItem;

import java.util.List;

/**
 * Created by arvin on 2017/9/11 0011.
 */

public class DataReception implements DataCallback {

    private Context context;
    private AuthorClient authorClient;
    private RequestManager requestManager;

    private List<ClientInfoRspUserInfo> loginList;
    private List<DT550RealDataRspDevice> currentlyDataList;
    private List<DT55HisDataRspItem> historicDataList;

    public DataReception(Context context) {
        this.context = context;
        authorClient = new AuthorClient();
        authorClient.setDataCallback(this);
        requestManager = new RequestManager();
    }

    @Override
    public void getClientInfoRspUserInfoList(List<ClientInfoRspUserInfo> list) {
        loginList = list;
    }

    @Override
    public void getDataRspDeviceList(List<DT550RealDataRspDevice> list) {
        currentlyDataList = list;
    }

    @Override
    public void getListDT55HisDataRspItem(List<DT55HisDataRspItem> list) {
        historicDataList = list;
    }


    public void requestFormLogin() {
        Dt550Request request = new Dt550Request();
        request.setContext(context);
        request.setRequestEnum(RequestEnum.RequestFormLogin);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {

                }
            }
        });
    }

    public void requestFormCurrentlyData() {
        Dt550Request request = new Dt550Request();
        request.setContext(context);
        request.setRequestEnum(RequestEnum.RequestFormCurrentlyData);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {

                }
            }
        });
    }

    public void requestFormHistoricData() {
        Dt550Request request = new Dt550Request();
        request.setContext(context);
        request.setRequestEnum(RequestEnum.RequestFormHistoricData);
        requestManager.submitRequest(request, new BaseCallback() {
            @Override
            public void done(BaseRequest request, Exception e) {
                if (request instanceof Dt550Request) {

                }
            }
        });
    }
}

