package com.sinsche.core.ws.client.android.struct;

public class DT550RealDataRspDeviceItem implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -6589583346470822719L;

	private String strCode;// 项目列号
	private String strName;// 名字
	private String strData;// 数据
	private String strWaterState;// 试剂状态
	private String strMax;// 上限
	private String strMin;// 下限
	private String strUnit;// 下限
	private boolean bOverLevel;// 是否超标

	public String getStrCode() {
		return strCode;
	}

	public void setStrCode(String strCode) {
		this.strCode = strCode;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrData() {
		return strData;
	}

	public void setStrData(String strData) {
		this.strData = strData;
	}

	public String getStrWaterState() {
		return strWaterState;
	}

	public void setStrWaterState(String strWaterState) {
		this.strWaterState = strWaterState;
	}

	public String getStrMax() {
		return strMax;
	}

	public void setStrMax(String strMax) {
		this.strMax = strMax;
	}

	public String getStrMin() {
		return strMin;
	}

	public void setStrMin(String strMin) {
		this.strMin = strMin;
	}

	public boolean isbOverLevel() {
		return bOverLevel;
	}

	public void setbOverLevel(boolean bOverLevel) {
		this.bOverLevel = bOverLevel;
	}

	public String getStrUnit() {
		return strUnit;
	}

	public void setStrUnit(String strUnit) {
		this.strUnit = strUnit;
	}

}