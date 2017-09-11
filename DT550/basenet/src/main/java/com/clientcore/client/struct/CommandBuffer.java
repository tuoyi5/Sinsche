package com.clientcore.client.struct;

public class CommandBuffer {
	private byte[] buffer = null;
	private int nTimes = -1;
	private int nRepeat = 0;
	private int nFunID = 0;
	private long startTime = 0;

	public CommandBuffer(byte[] buffer, int nTimes, int nRepeat, int nFunID) {
		this.nFunID = nFunID;
		this.buffer = buffer;
		this.nTimes = nTimes;
		this.nRepeat = nRepeat;
		startTime = System.currentTimeMillis();
	}

	public void setnRepeat(int nRepeat) {
		this.nRepeat = nRepeat;
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public int getnTimes() {
		return nTimes;
	}

	public void setnTimes(int nTimes) {
		this.nTimes = nTimes;
	}

	public int getnRepeat() {
		return nRepeat;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public int getnFunID() {
		return nFunID;
	}

}
