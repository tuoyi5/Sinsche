package com.sinsche.core.ws.client.android;

import com.common.core.ws.client.UploadData;

public class ClientInfoReq  extends UploadData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -447330814584211529L;

	private String strClientName;

	public String getStrClientName() {
		return strClientName;
	}

	public void setStrClientName(String strClientName) {
		this.strClientName = strClientName;
	}
}
