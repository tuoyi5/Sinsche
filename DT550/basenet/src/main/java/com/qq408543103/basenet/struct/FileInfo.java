package com.qq408543103.basenet.struct;

import java.io.Serializable;

/**
 *
 */
public class FileInfo implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6297457835923943323L;
	public int nLevel;
	public int dwTatalFileSize;
	public int nLocalID;
	public String chMD5;
	public String chClientFileName;
	public String chClientFilePath;
	public String chClientSerialFrom;
	public String chClientSerialTo;

	// add by cc at 2016-12-06 for hold data
	public byte[] bData;

	// add by cc at 2016-12-07 for server to know which project to send at
	// thread decoder
	public String projectName;
}
