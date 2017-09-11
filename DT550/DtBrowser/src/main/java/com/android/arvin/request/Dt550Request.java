package com.android.arvin.request;

import com.arvin.request.request.baserequest.BaseRequest;
import com.arvin.request.request.baserequest.RequestEnum;

/**
 * Created by arvin on 2017/8/23 0023.
 */

public class Dt550Request extends BaseRequest {

    private RequestEnum requestEnum;


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

                    break;
                case RequestFormHistoricData:

                    break;
            }
    }
}


