package com.clientcore.client.i;

public interface ClientConnect {
	void Connected();

	void Disconnect();

	void Init();

	void BeforeReConnect();

	void OnError();

	void OnRun(long runTime);

	boolean DispatchRecvData(int nFunc, byte[] dataArray);
}
