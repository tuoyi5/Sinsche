package com.common.core.ws.client;

import java.util.UUID;

public class UploadData implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -153598357282634022L;

    public UploadData() {
        proName = "DT550";
        sign = UUID.randomUUID().toString();
    }

    private String seriaNum;
    private String proName;
    // add by cc at 2016/10/11 for request track between client and server
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getSeriaNum() {
        return seriaNum;
    }

    public void setSeriaNum(String seriaNum) {
        this.seriaNum = seriaNum;
    }

}
