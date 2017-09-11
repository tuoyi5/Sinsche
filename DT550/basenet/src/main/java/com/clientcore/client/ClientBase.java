package com.clientcore.client;

import android.util.Log;

import com.clientcore.client.i.ClientConnect;
import com.clientcore.client.struct.BaseStruct;
import com.clientcore.client.struct.CommandBuffer;
import com.clientcore.client.struct.Head;
import com.clientcore.client.tool.EncryptTool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ClientBase implements Runnable {

    protected String remoteIp = "172.20.10.104";
    protected int remotePort = 7011;
    protected int nTimeout = 10;

    // 注册信息
    protected boolean bReload = false;
    protected boolean bReConnect = true;

    protected int nClientID = 0;

    private boolean bStop = false;

    private String aesKey = null;

    private String strGuid = UUID.randomUUID().toString();

    private byte[] readBuffer = new byte[BaseStruct.emMaxBuff * 2];
    private byte[] readBufferTwo = new byte[BaseStruct.emMaxBuff * 2];
    private byte[] readBufferTemp = new byte[BaseStruct.emMaxBuff];

    private EncryptTool encryptTool = new EncryptTool();

    private ClientConnect connectCallback = null;

    public void setConnectCallback(ClientConnect connectCallback) {
        this.connectCallback = connectCallback;
    }

    private ConcurrentLinkedQueue<CommandBuffer> commandQueue = new ConcurrentLinkedQueue<CommandBuffer>();

    private Socket newSocket() {
        Socket sock = new Socket();
        try {
            sock.setKeepAlive(true);
            sock.setTcpNoDelay(true);
            sock.setSoTimeout(nTimeout);
        } catch (SocketException e) {
            return null;
        }

        return sock;
    }

    private int DecodeDataArray(byte[] dataArray, int nCurrentHave) {
        Head head = new Head(dataArray);
        int nSize = head.getPkglen() - head.GetSize();
        int nTotlalSize = nCurrentHave - head.GetSize();
        if (nTotlalSize < nSize) {
            return 0;
        }
        if (nSize > BaseStruct.emMaxBuff || nSize < 0) {
            return 0;
        }
        if (head.getPkglen() > dataArray.length) {
            return 0;
        }
        byte[] byteArrayData = new byte[nSize];
        System.arraycopy(dataArray, head.GetSize(), byteArrayData, 0, nSize);
        byteArrayData = head.unpackData(byteArrayData, aesKey);
        switch (head.getFunID()) {
            case BaseStruct.emFunLogin:
                if (Proto_Login_Rsp(byteArrayData)) {
                    if (connectCallback != null) {
                        connectCallback.Connected();
                    }
                    Proto_Heart_Req();
                    return head.getPkglen();
                }
            case BaseStruct.emFunHeart:
                if (Proto_Heart_Rsp(byteArrayData)) {
                    Proto_ServerTime_Req();
                    return head.getPkglen();
                }
            case BaseStruct.emFunLogout:
                if (Proto_LoginOut_Rsp(byteArrayData)) {
                    if (connectCallback != null) {
                        connectCallback.Disconnect();
                    }
                    bStop = true;
                    return head.getPkglen();
                }
            case BaseStruct.emFunServerTime:
                if (Proto_ServerTime_Rsp(byteArrayData)) {
                    return head.getPkglen();
                }
            default:
                if (connectCallback.DispatchRecvData(head.getFunID(), byteArrayData)) {
                    return head.getPkglen();
                }
                break;
        }

        return 0;
    }

    private void Proto_Login_Req() {
        byte[] chguids = new byte[64];
        byte[] chguidsTemp = BaseStruct.StringToByteArray(strGuid);
        System.arraycopy(chguidsTemp, 0, chguids, 0, chguidsTemp.length);

        Head head = new Head(BaseStruct.emFunLogin, BaseStruct.emNormal);
        byte[] byteArrayData = new byte[head.GetSize() + chguids.length];
        System.arraycopy(head.getByteArrayData(), 0, byteArrayData, 0, head.GetSize());
        System.arraycopy(chguids, 0, byteArrayData, head.GetSize(), chguids.length);
        Send(new CommandBuffer(head.packData(byteArrayData, aesKey), 0, 0, head.getFunID()));
    }

    private boolean Proto_Login_Rsp(byte[] dataArray) {
        aesKey = encryptTool.decryptByPrivateKey(BaseStruct.ByteArraytoString(dataArray, 0, dataArray.length), BaseStruct.priKey);
        return aesKey.isEmpty() == false ? true : false;
    }

    private boolean Proto_LoginOut_Rsp(byte[] dataArray) {
        if (BaseStruct.ByteArrayToInt(dataArray, 0) == 1) {
            return true;
        } else {
        }
        return false;
    }

    public void Proto_Heart_Req() {
        Head head = new Head(BaseStruct.emFunHeart, BaseStruct.emAdler32 | BaseStruct.emEncrypt);

        byte[] chguids = new byte[64];
        byte[] chguidsTemp = BaseStruct.StringToByteArray(strGuid);
        System.arraycopy(chguidsTemp, 0, chguids, 0, chguidsTemp.length);

        byte[] byteArrayData = new byte[chguids.length + head.GetSize()];
        System.arraycopy(head.getByteArrayData(), 0, byteArrayData, 0, head.GetSize());
        System.arraycopy(chguids, 0, byteArrayData, head.GetSize(), chguids.length);
        Send(new CommandBuffer(head.packData(byteArrayData, aesKey), -1, 30 * 1000, head.getFunID()));
    }

    private boolean Proto_Heart_Rsp(byte[] dataArray) {
        return true;
    }

    private void Proto_ServerTime_Req() {
        Head head = new Head(BaseStruct.emFunServerTime, BaseStruct.emAdler32 | BaseStruct.emEncrypt | BaseStruct.emZip);

        byte[] chguids = new byte[64];
        byte[] chguidsTemp = BaseStruct.StringToByteArray(strGuid);
        System.arraycopy(chguidsTemp, 0, chguids, 0, chguidsTemp.length);

        byte[] byteArrayData = new byte[chguids.length + head.GetSize()];
        System.arraycopy(head.getByteArrayData(), 0, byteArrayData, 0, head.GetSize());
        System.arraycopy(chguids, 0, byteArrayData, head.GetSize(), chguids.length);
        Send(new CommandBuffer(head.packData(byteArrayData, aesKey), 0, 0, head.getFunID()));
    }

    private boolean Proto_ServerTime_Rsp(byte[] dataArray) {
        Calendar rightNow = new GregorianCalendar(BaseStruct.getShort(dataArray, 0), BaseStruct.getShort(dataArray, 2) - 1, BaseStruct.getShort(dataArray, 6), BaseStruct.getShort(dataArray, 8),
                BaseStruct.getShort(dataArray, 10), BaseStruct.getShort(dataArray, 12));
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strTime = myFmt2.format(rightNow.getTime());
        if (strTime != null) {
            Log.i("DT550-远程服务器时间：", strTime);
            return true;
        }
        return false;
    }

    public void Send(int nFunID, int nType, byte[] bData, int nReapeat, int nDelay) {
        Head head = new Head(nFunID, nType);
        byte[] byteArrayData = new byte[bData.length + head.GetSize()];
        System.arraycopy(head.getByteArrayData(), 0, byteArrayData, 0, head.GetSize());
        System.arraycopy(bData, 0, byteArrayData, head.GetSize(), bData.length);
        Send(new CommandBuffer(head.packData(byteArrayData, aesKey), nReapeat, nDelay, nFunID));
    }

    public void Send(int nFunID, int nType, byte[] bData) {
        Send(nFunID, nType, bData, 0, 0);
    }

    private void Send(CommandBuffer command) {
        commandQueue.add(command);
    }

    public void Stop() {
        bStop = true;
    }

    @Override
    public void run() {
        int nCurrentRead = 0;
        bReload = true;
        connectCallback.Init();
        Socket sock = newSocket();
        InetSocketAddress addr = new InetSocketAddress(remoteIp, remotePort);
        while (bStop == false) {
            try {
                if (bReConnect) {
                    try {
                        if (sock.isConnected() || !sock.isClosed()) {
                            sock.shutdownInput();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (sock.isConnected() || !sock.isClosed()) {
                            sock.shutdownOutput();
                        }
                    } catch (Exception e) {
                    }
                    try {
                        if (sock.isConnected() || !sock.isClosed()) {
                            sock.close();
                        }
                    } catch (Exception e) {
                    }
                    sock = newSocket();
                    sock.connect(addr);
                    commandQueue.clear();
                    connectCallback.BeforeReConnect();
                    bReConnect = false;
                    nCurrentRead = 0;
                    bReload = true;
                }
            } catch (Exception ex) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                continue;
            }
            try {
                // remote register
                if (bReload) {
                    Proto_Login_Req();
                    bReload = false;
                }

                CommandBuffer command = null;
                if (commandQueue.isEmpty() == false) {
                    command = commandQueue.poll();
                }

                long runTime = System.currentTimeMillis();
                connectCallback.OnRun(runTime);

                if (command != null) {
                    boolean bContinueSend = false;
                    if (runTime - command.getStartTime() > command.getnRepeat()) {
                        try {
                            sock.getOutputStream().write(command.getBuffer());
                        } catch (IOException e) {
                            connectCallback.OnError();
                            bReConnect = true;
                            bReload = false;
                            continue;
                        }

                        if (command.getnTimes() > 0) {
                            command.setnTimes(command.getnTimes() - 1);
                        }

                        if (command.getnTimes() == -1 || command.getnTimes() > 0) {
                            bContinueSend = true;
                            command.setStartTime(runTime);
                        }
                    } else {
                        bContinueSend = true;
                    }
                    if (bContinueSend && command != null) {
                        commandQueue.add(command);
                    }
                    command = null;
                }
                try {
                    int nRead = sock.getInputStream().read(readBufferTemp);
                    if (nRead > 0) {
                        if (nCurrentRead + nRead > readBuffer.length) {
                            connectCallback.OnError();
                            bReConnect = true;
                            bReload = false;
                        }
                        System.arraycopy(readBufferTemp, 0, readBuffer, nCurrentRead, nRead);
                        nCurrentRead += nRead;
                    } else {
                        connectCallback.OnError();
                        bReConnect = true;
                        bReload = false;
                    }

                    while (nCurrentRead > 0) {
                        int nDecoder = DecodeDataArray(readBuffer, nCurrentRead);
                        if (nDecoder > 0) {
                            int nReaminDecoder = nCurrentRead - nDecoder;
                            if (nReaminDecoder > 0) {
                                System.arraycopy(readBuffer, nDecoder, readBufferTwo, 0, nReaminDecoder);
                                System.arraycopy(readBufferTwo, 0, readBuffer, 0, nReaminDecoder);
                            }

                            nCurrentRead -= nDecoder;

                            if (nCurrentRead < 0) {
                                connectCallback.OnError();
                                bReConnect = true;
                                bReload = false;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {
                    if (e instanceof SocketTimeoutException) {
                        continue;
                    }
                    connectCallback.OnError();
                    bReConnect = true;
                    bReload = false;
                }
            } catch (Exception ex) {
            }
        }

        Head head = new Head(BaseStruct.emFunLogout, BaseStruct.emAdler32 | BaseStruct.emEncrypt);
        byte dwClientIDs[] = BaseStruct.StringToByteArray(strGuid);
        byte[] byteArrayData = new byte[dwClientIDs.length + head.GetSize()];
        System.arraycopy(head.getByteArrayData(), 0, byteArrayData, 0, head.GetSize());
        System.arraycopy(dwClientIDs, 0, byteArrayData, head.GetSize(), dwClientIDs.length);
        CommandBuffer command = new CommandBuffer(head.packData(byteArrayData, aesKey), 1, 0, head.getFunID());

        try {
            sock.getOutputStream().write(command.getBuffer());
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        nCurrentRead = 0;

        try {
            int nRead = sock.getInputStream().read(readBufferTemp);
            if (nRead > 0) {
                System.arraycopy(readBufferTemp, 0, readBuffer, nCurrentRead, nRead);
                nCurrentRead += nRead;
            }
            if (nCurrentRead > 0) {
                DecodeDataArray(readBuffer, nCurrentRead);
            }
        } catch (SocketTimeoutException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        try {
            sock.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

        connectCallback.OnError();
        nClientID = 0;
    }
}