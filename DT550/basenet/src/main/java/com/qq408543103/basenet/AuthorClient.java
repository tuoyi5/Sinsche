package com.qq408543103.basenet;

import android.util.Log;

import com.clientcore.client.ClientBase;
import com.clientcore.client.i.ClientConnect;
import com.clientcore.client.struct.BaseStruct;
import com.clientcore.client.tool.EncryptTool;
import com.qq408543103.basenet.struct.AuthorClientLevel;
import com.qq408543103.basenet.struct.FileInfo;
import com.sinsche.core.ws.client.android.ClientInfoReq;
import com.sinsche.core.ws.client.android.ClientInfoRsp;
import com.sinsche.core.ws.client.android.DT550HisDataReq;
import com.sinsche.core.ws.client.android.DT550HisDataRsp;
import com.sinsche.core.ws.client.android.DT550RealDataReq;
import com.sinsche.core.ws.client.android.DT550RealDataRsp;
import com.sinsche.core.ws.client.android.struct.ClientInfoRspUserInfo;
import com.sinsche.core.ws.client.android.struct.DT550RealDataRspDevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;


public class AuthorClient extends ClientBase implements ClientConnect {

    private static final int emClientTypeCLient = 2;

    private static final int emSendLogin = 10000;
    // 上传文件的命令
    private static final int emSendFileUploadData = 10002;
    // 下载文件的命令
    private static final int emSendFileDownloadData = 10004;
    // 带文件优先级的 上传下载
    private static final int emSendFileUploadLevel = 10009;
    private static final int emSendFileDownloadLevel = 10010;

    private int nCurrentDownloadNum = 0;
    private final int nMaxDownloadNum = 5;
    private final int nMaxUpload = 1;
    private int nCurrentUpload = 0;

    private String strBasePath = "";
    private String strRemoteDownloadPath;
    private String strRemoteUploadPath;

    private String strClientName = null;
    private String strClientSerial = null;

    private Map<Integer, FileInfo> mapDownload = new ConcurrentHashMap();
    private Map<Integer, FileInfo> mapUpload = new ConcurrentHashMap();
    private PriorityBlockingQueue<FileInfo> queueUpload = new PriorityBlockingQueue(100, idComparator);

    private long lastDownload = System.currentTimeMillis();

    private boolean bRemoteInitOk = false;

    private int emClientType = emClientTypeCLient;

    private volatile int uploadOrder = 1; // upload order in upload queue

    private String strLicence = "";

    private EncryptTool encryptTool = new EncryptTool();

    private static Comparator<FileInfo> idComparator = new Comparator<FileInfo>() {
        @Override
        public int compare(FileInfo c1, FileInfo c2) {
            return (c1.nLevel - c2.nLevel);
        }
    };

    public void Start(String strIP, int nPort, String strClientSerial, String strClientName, String strBasePath) {
        this.remoteIp = strIP;
        this.remotePort = nPort;

        this.strClientSerial = strClientSerial;
        this.strClientName = strClientName;
        this.strBasePath = strBasePath;

        setConnectCallback(this);

        new Thread(this).start();
    }

    @Override
    public void Connected() {
        // TODO Auto-generated method stub
        if (strClientSerial != null) {
            LoginInfo_Req();
        }
    }

    @Override
    public void Disconnect() {
        // TODO Auto-generated method stub
        bReConnect = true;
    }

    @Override
    public boolean DispatchRecvData(int nFunc, byte[] dataArray) {
        switch (nFunc) {
            case emSendLogin:
                return LoginInfo_Rsp(dataArray);// 客户端注册信息
            case emSendFileUploadLevel:
                return UploadInfo_Rsp(dataArray);
            case emSendFileDownloadLevel:
                return DownloadInfoLevel_Rsp(dataArray);
            case emSendFileDownloadData:
                return DownloadData_Rsp(dataArray);// 下载报表数据
            case emSendFileUploadData:
                return UploadData_Rsp(dataArray);
        }
        return false;
    }

    private void LoginInfo_Req() {
        byte emClient[] = BaseStruct.IntToByteArray(emClientType);

        byte chClientCode[] = new byte[64];
        byte[] chClientCodeTemp = BaseStruct.StringToByteArray(strClientSerial);
        System.arraycopy(chClientCodeTemp, 0, chClientCode, 0, chClientCodeTemp.length);

        byte chClientName[] = new byte[128];
        byte[] chClientNameTemp = BaseStruct.StringToByteArray(strClientName);
        System.arraycopy(chClientNameTemp, 0, chClientName, 0, chClientNameTemp.length);

        byte[] byteArrayData = new byte[chClientCode.length + chClientName.length + emClient.length];

        System.arraycopy(chClientCode, 0, byteArrayData, 0, chClientCode.length);
        System.arraycopy(chClientName, 0, byteArrayData, chClientCode.length, chClientName.length);
        System.arraycopy(emClient, 0, byteArrayData, chClientCode.length + chClientName.length, emClient.length);

        Send(emSendLogin, BaseStruct.emAdler32 | BaseStruct.emEncrypt | BaseStruct.emZip, byteArrayData);
    }

    private boolean LoginInfo_Rsp(byte[] dataArray) {
        nClientID = BaseStruct.ByteArrayToInt(dataArray, 0);
        if (nClientID > 0) {
            strClientSerial = BaseStruct.ByteArraytoString(dataArray, 4, 64);
            strLicence = BaseStruct.ByteArraytoString(dataArray, 68, 4096);
            bRemoteInitOk = Decoder();
        }

        if (bRemoteInitOk) {
            LoadClientInfo();
        }
        return bRemoteInitOk;
    }

    private boolean Decoder() {
        Map<String, String> tempMap = new HashMap();
        if (strLicence != null) {
            String sendDatas[] = strLicence.split("\r\n");
            for (int i = 0; i < sendDatas.length; i++) {
                switch (i) {
                    case 0:
                        if (sendDatas[0].equals("0") == false) {
                            return false;
                        }
                        break;
                    default:
                        int nFlag = sendDatas[i].indexOf(":");
                        if (nFlag == -1) {
                            continue;
                        }
                        String proName = sendDatas[i].substring(0, nFlag);
                        tempMap.put(proName, proName);
                        break;
                }
            }
        }
        return tempMap.isEmpty() == false ? true : false;
    }

    // 客户端上传文件命令文件信息请求数据包
    private boolean UploadInfoLevel_Req(FileInfo fileInfo) {
        if (bRemoteInitOk == false) {
            return false;
        }

        if (mapUpload.get(fileInfo.nLocalID) == null) {
            mapUpload.put(fileInfo.nLocalID, fileInfo);

            byte nLevel[] = BaseStruct.IntToByteArray(fileInfo.nLevel);
            byte nLocalID[] = BaseStruct.IntToByteArray(fileInfo.nLocalID);
            byte dwTatalFileSize[] = BaseStruct.IntToByteArray(fileInfo.dwTatalFileSize);

            byte chClientSerialFrom[] = new byte[64];
            byte[] chClientSerialFromTemp = BaseStruct.StringToByteArray(fileInfo.chClientSerialFrom);
            System.arraycopy(chClientSerialFromTemp, 0, chClientSerialFrom, 0, chClientSerialFromTemp.length);

            byte chClientSerialTo[] = new byte[64];
            byte[] chClientSerialToTemp = BaseStruct.StringToByteArray(fileInfo.chClientSerialTo);
            System.arraycopy(chClientSerialToTemp, 0, chClientSerialTo, 0, chClientSerialToTemp.length);

            byte chMD5[] = new byte[128];
            byte[] chMD5Temp = BaseStruct.StringToByteArray(fileInfo.chMD5);
            System.arraycopy(chMD5Temp, 0, chMD5, 0, chMD5Temp.length);

            byte chClientFileName[] = new byte[512];
            byte[] chClientFileNameTemp = BaseStruct.StringToByteArray(fileInfo.chClientFileName);
            System.arraycopy(chClientFileNameTemp, 0, chClientFileName, 0, chClientFileNameTemp.length);

            byte[] byteArrayData = new byte[nLevel.length + nLocalID.length + dwTatalFileSize.length
                    + chClientSerialFrom.length + chClientSerialTo.length + chMD5.length + chClientFileName.length];
            System.arraycopy(nLevel, 0, byteArrayData, 0, nLevel.length);
            System.arraycopy(nLocalID, 0, byteArrayData, nLevel.length, nLocalID.length);
            System.arraycopy(dwTatalFileSize, 0, byteArrayData, nLevel.length + nLocalID.length,
                    dwTatalFileSize.length);
            System.arraycopy(chClientSerialFrom, 0, byteArrayData,
                    nLevel.length + nLocalID.length + dwTatalFileSize.length, chClientSerialFrom.length);
            System.arraycopy(chClientSerialTo, 0, byteArrayData,
                    nLevel.length + nLocalID.length + dwTatalFileSize.length + chClientSerialFrom.length,
                    chClientSerialTo.length);
            System.arraycopy(chMD5, 0, byteArrayData, nLevel.length + nLocalID.length + dwTatalFileSize.length
                    + chClientSerialFrom.length + chClientSerialTo.length, chMD5.length);
            System.arraycopy(chClientFileName,
                    0, byteArrayData, nLevel.length + nLocalID.length + dwTatalFileSize.length
                            + chClientSerialFrom.length + chClientSerialTo.length + chMD5.length,
                    chClientFileName.length);

            Send(emSendFileUploadLevel, BaseStruct.emAdler32 | BaseStruct.emEncrypt | BaseStruct.emZip, byteArrayData);
            return true;
        }

        return false;
    }

    // 客户端上传文件命令文件信息响应数据包
    private boolean UploadInfo_Rsp(byte[] dataArray) {
        int nLoadID = BaseStruct.ByteArrayToInt(dataArray, 0);
        int nLocalID = BaseStruct.ByteArrayToInt(dataArray, 4);
        if (nLocalID == nLoadID) {
            FileInfo fileInfo = mapUpload.get(nLocalID);
            if (fileInfo != null) {
                UploadData_Req(nLoadID, 0);
            }
        } else {
            mapUpload.remove(nLocalID);

        }
        return true;
    }

    // 客户端上传文件命令文件数据请求数据包
    private boolean UploadData_Req(int nLoadID, int nOffset) {
        if (bRemoteInitOk == false) {
            return false;
        }
        FileInfo fileInfo = mapUpload.get(nLoadID);
        if (fileInfo == null) {
            return false;
        }
        int dwFileSize = BaseStruct.emMaxBuff - BaseStruct.lenHead - 3 * 4;
        byte[] chFileUploadData = new byte[dwFileSize];
        dwFileSize = chFileUploadData.length < (fileInfo.bData.length - nOffset) ? chFileUploadData.length
                : (fileInfo.bData.length - nOffset);
        System.arraycopy(fileInfo.bData, nOffset, chFileUploadData, 0, dwFileSize);

        byte byLoadID[] = BaseStruct.IntToByteArray(nLoadID);
        byte byOffset[] = BaseStruct.IntToByteArray(nOffset);
        byte byFileSize[] = BaseStruct.IntToByteArray(dwFileSize);

        byte[] byteArrayData = new byte[byLoadID.length + byOffset.length + byFileSize.length
                + chFileUploadData.length];
        System.arraycopy(byLoadID, 0, byteArrayData, 0, byLoadID.length);
        System.arraycopy(byOffset, 0, byteArrayData, byLoadID.length, byLoadID.length);
        System.arraycopy(byFileSize, 0, byteArrayData, byLoadID.length + byLoadID.length, byFileSize.length);
        System.arraycopy(chFileUploadData, 0, byteArrayData, byLoadID.length + byLoadID.length + byFileSize.length,
                dwFileSize);

        Send(emSendFileUploadData, BaseStruct.emAdler32 | BaseStruct.emEncrypt | BaseStruct.emZip, byteArrayData);
        return true;
    }

    // 客户端上传文件命令文件数据响应数据包
    private boolean UploadData_Rsp(byte[] dataArray) {
        int nLoadID = BaseStruct.ByteArrayToInt(dataArray, 0);
        int nOffset = BaseStruct.ByteArrayToInt(dataArray, 4);
        FileInfo fileInfo = mapUpload.get(nLoadID);
        if (fileInfo != null) {
            if (nOffset != fileInfo.dwTatalFileSize) {
                UploadData_Req(nLoadID, nOffset);
            } else {
                mapUpload.remove(nLoadID);
                File file = new File(fileInfo.chClientFilePath);
                if (file.exists()) {
                    file.delete();
                }
                nCurrentUpload--;
            }
        }
        return true;
    }

    // 客户端下载文件命令文件信息请求数据包
    private boolean DownloadInfo_Req() {
        if (bRemoteInitOk == false) {
            return false;
        }

        byte chClientSerial[] = new byte[64];
        byte[] chClientSerialTemp = BaseStruct.StringToByteArray(strClientSerial);
        System.arraycopy(chClientSerialTemp, 0, chClientSerial, 0, chClientSerialTemp.length);
        Send(emSendFileDownloadLevel, BaseStruct.emAdler32 | BaseStruct.emEncrypt | BaseStruct.emZip, chClientSerial);
        return true;
    }

    public boolean DownloadInfoLevel_Rsp(byte[] dataArray) {
        int nLoadID = BaseStruct.ByteArrayToInt(dataArray, 0);
        if (nLoadID > 0) {
            if (mapDownload.get(nLoadID) == null) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.nLevel = BaseStruct.ByteArrayToInt(dataArray, 4);
                fileInfo.dwTatalFileSize = BaseStruct.ByteArrayToInt(dataArray, 8);
                fileInfo.chMD5 = BaseStruct.ByteArraytoString(dataArray, 12, 128);
                String strName = BaseStruct.ByteArraytoString(dataArray, 12 + 128, 512);
                String[] strNames = strName.split("_");
                if (strNames.length == 5) {
                    fileInfo.projectName = strNames[1];
                }
                fileInfo.chClientFileName = strName;
                fileInfo.chClientFilePath = strRemoteDownloadPath + fileInfo.chClientFileName;
                if (fileInfo.dwTatalFileSize > 0) {
                    nCurrentDownloadNum++;
                    fileInfo.bData = new byte[fileInfo.dwTatalFileSize];
                    mapDownload.put(nLoadID, fileInfo);
                    DownloadData_Req(nLoadID, 0);
                }
            }
        } else {

        }
        return true;
    }

    // 客户端下载文件命令文件数据请求数据包
    private boolean DownloadData_Req(int nLoadID, int nOffset) {
        if (bRemoteInitOk == false) {
            return false;
        }
        byte byLoadID[] = BaseStruct.IntToByteArray(nLoadID);
        byte byOffset[] = BaseStruct.IntToByteArray(nOffset);

        byte[] byteArrayData = new byte[byLoadID.length + byOffset.length];

        System.arraycopy(byLoadID, 0, byteArrayData, 0, byLoadID.length);
        System.arraycopy(byOffset, 0, byteArrayData, byLoadID.length, byOffset.length);

        Send(emSendFileDownloadData, BaseStruct.emAdler32 | BaseStruct.emEncrypt | BaseStruct.emZip, byteArrayData);
        return true;
    }

    // 客户端下载文件命令文件数据响应数据包
    private boolean DownloadData_Rsp(byte[] dataArray) {
        int nLoadID = BaseStruct.ByteArrayToInt(dataArray, 0);
        int nOffset = BaseStruct.ByteArrayToInt(dataArray, 4);
        int dwFileSize = BaseStruct.ByteArrayToInt(dataArray, 8);
        FileInfo fileInfo = mapDownload.get(nLoadID);
        if (fileInfo != null) {
            System.arraycopy(dataArray, 12, fileInfo.bData, nOffset, dwFileSize);
            if (fileInfo.dwTatalFileSize == nOffset + dwFileSize) {
                String strMD5 = encryptTool.MD5(fileInfo.bData);
                if (fileInfo.chMD5.equals(strMD5)) {
                    Object object = encryptTool.Base64StrToObj(new String(fileInfo.bData));
                    if (object != null) {
                        if (object instanceof ClientInfoRsp) {
                            //此处已经解析到数据，用于用户登录时，判断用户名密码。
                            List<ClientInfoRspUserInfo> list = ((ClientInfoRsp) object).getListClientInfoRspUserInfo();
                            Log.d("Author登陆用户数：", list == null ? "0" : new Integer(list.size()).toString());
                            //用户密码放入内存中，用HashMap在前台验证。
                        } else if (object instanceof DT550RealDataRsp) {
                            //此处已经解析到数据，填充测试界面。
                            List<DT550RealDataRspDevice> listDT550RealDataRspDevice = ((DT550RealDataRsp) object).getListDT550RealDataRspDevice();
                            Log.d("DT550实时数据：", new Integer(listDT550RealDataRspDevice.size()).toString());
                        } else if (object instanceof DT550HisDataRsp) {
                            DT550HisDataRsp dt550HisDataRsp = (DT550HisDataRsp) object;
                            //此处已经解析到数据，填充历史界面。
                            Log.d("DT550历史数据：", dt550HisDataRsp.getStrDeviceSerial() + "-" + dt550HisDataRsp.getStrItemCode());
                        }
                        nCurrentDownloadNum--;
                        mapDownload.remove(nLoadID);

                        DownloadInfo_Req();
                    }
                } else {
                    DownloadData_Req(nLoadID, dwFileSize + nOffset);
                }
            }
        }
        return true;
    }

    @Override
    public void Init() {
        // TODO Auto-generated method stub
        strRemoteDownloadPath = strBasePath + ("serialize/to/");
        strRemoteUploadPath = strBasePath + ("serialize/from/");
        new File(strRemoteDownloadPath).mkdirs();
        new File(strRemoteUploadPath).mkdirs();
    }

    @Override
    public void BeforeReConnect() {
        // TODO Auto-generated method stub
        nCurrentDownloadNum = 0;
        nCurrentUpload = 0;
        mapDownload.clear();
    }

    @Override
    public void OnError() {
        // TODO Auto-generated method stub
        bRemoteInitOk = false;
    }

    /**
     * app 下为了保证实时性，app获取数据的刷新间隔为5秒。
     *
     * @param runTime
     */
    @Override
    public void OnRun(long runTime) {
        // TODO Auto-generated method stub
        if (runTime - lastDownload > 5 * 1000) {
            if (nCurrentDownloadNum < nMaxDownloadNum) {
                DownloadInfo_Req();
                if (bRemoteInitOk) {
                    lastDownload = runTime;
                }
            }
        }
        if (nCurrentUpload < nMaxUpload) {
            if (queueUpload.size() > 0) {
                FileInfo fileInfo = queueUpload.peek();
                if (UploadInfoLevel_Req(fileInfo)) {
                    queueUpload.poll();
                    nCurrentUpload++;
                }
            }
        }
    }

    private boolean WriteFile(String proName, int nLevel, String strData) {
        // 此处序列化文件，写入到发送数据库，发送线程开始发送文件
        String[] strDatas = strData.split(",");
        if (strDatas.length == 2) {
            String[] myArr = strDatas[1].split("_");
            if (myArr.length == 2) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.nLevel = nLevel;
                fileInfo.nLocalID = uploadOrder++;

                fileInfo.chClientSerialFrom = strClientSerial;
                fileInfo.chClientSerialTo = strDatas[0];

                fileInfo.chClientFileName = fileInfo.nLevel + "_" + proName + "_" + fileInfo.chClientSerialFrom + "_"
                        + fileInfo.chClientSerialTo + "_" + myArr[0] + ".dat";// uuid
                fileInfo.chClientFilePath = strRemoteUploadPath + fileInfo.chClientFileName;

                fileInfo.bData = myArr[1].getBytes();
                fileInfo.chMD5 = encryptTool.MD5(fileInfo.bData);
                fileInfo.dwTatalFileSize = fileInfo.bData.length;

                FileOutputStream fos = null;
                ObjectOutputStream os = null;
                try {
                    fos = new FileOutputStream(fileInfo.chClientFilePath);
                    os = new ObjectOutputStream(fos);
                    os.writeObject(fileInfo);
                    fos.flush();
                    os.flush();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                    }
                }

                return queueUpload.add(fileInfo);
            }
        }
        return false;
    }

    /**
     * 返回客户端下面，所有的设备信息。
     */
    public void LoadClientInfo() {
        ClientInfoReq clientInfoReq = new ClientInfoReq();
        WriteFile("Author", AuthorClientLevel.upd_dwn_level_5, strClientSerial + "," + clientInfoReq.getSign() + "_" + encryptTool.objToBase64Str(clientInfoReq));
    }

    /**
     * 去当前实时数据。app启动之后加定时器每隔1分钟刷新一次。
     */
    public void RequestRealTimeData() {
        DT550RealDataReq dt550RealDataReq = new DT550RealDataReq();
        WriteFile("DT550", AuthorClientLevel.upd_dwn_level_5, strClientSerial + "," + dt550RealDataReq.getSign() + "_" + encryptTool.objToBase64Str(dt550RealDataReq));
    }

    /**
     * 测试界面上单击具体的项目，获取历史记录。
     *
     * @param strDevice
     * @param strItem
     */
    public void RequestHisData(String strDevice, String strItem) {
        DT550HisDataReq dt550HisDataReq = new DT550HisDataReq();
        dt550HisDataReq.setStrDeviceSerial(strDevice);
        dt550HisDataReq.setStrItemCode(strItem);
        WriteFile("DT550", AuthorClientLevel.upd_dwn_level_5, strClientSerial + "," + dt550HisDataReq.getSign() + "_" + encryptTool.objToBase64Str(dt550HisDataReq));
    }
}
