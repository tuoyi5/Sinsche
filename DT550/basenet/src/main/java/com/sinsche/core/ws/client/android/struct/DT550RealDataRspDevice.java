package com.sinsche.core.ws.client.android.struct;

import java.util.List;

public class DT550RealDataRspDevice implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7576489375003051835L;

	private String strName;
	private String strSerial;
	private String strTestTime;
	private String strState1;
	private String strState2;
	private String strState3;
	private String strState4;

	private List<DT550RealDataRspDeviceItem> item;

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrSerial() {
		return strSerial;
	}

	public void setStrSerial(String strSerial) {
		this.strSerial = strSerial;
	}

	public String getStrTestTime() {
		return strTestTime;
	}

	public void setStrTestTime(String strTestTime) {
		this.strTestTime = strTestTime;
	}

	public String getStrState1() {
		return strState1;
	}

	public void setStrState1(String strState1) {
		this.strState1 = strState1;
	}

	public String getStrState2() {
		return strState2;
	}

	public void setStrState2(String strState2) {
		this.strState2 = strState2;
	}

	public String getStrState3() {
		return strState3;
	}

	public void setStrState3(String strState3) {
		this.strState3 = strState3;
	}

	public String getStrState4() {
		return strState4;
	}

	public void setStrState4(String strState4) {
		this.strState4 = strState4;
	}

	public List<DT550RealDataRspDeviceItem> getItem() {
		return item;
	}

	public void setItem(List<DT550RealDataRspDeviceItem> item) {
		this.item = item;
	}

}
