package com.clientcore.client.struct;

import com.clientcore.client.tool.Adler32;
import com.clientcore.client.tool.Zlib;
import com.clientcore.client.tool.EncryptTool;

public class Head extends BaseStruct {
    private int stx;
    private int pkglen;

    private int nFunID;
    private int nType;
    private int nADLER32;
    private int dwTime;

    public Head(int nFunID, int nType) {
        this.stx = emStx;
        this.pkglen = emMaxBuff;
        this.nFunID = nFunID;
        this.nType = nType;
        this.dwTime = (int) System.currentTimeMillis();
    }

    public Head(byte[] dataArray) {
        byte[] stxs = new byte[4];
        System.arraycopy(dataArray, 0, stxs, 0, stxs.length);
        byte[] pkglens = new byte[4];
        System.arraycopy(dataArray, stxs.length, pkglens, 0, pkglens.length);
        byte[] nFunIDs = new byte[4];
        System.arraycopy(dataArray, stxs.length + pkglens.length, nFunIDs, 0, nFunIDs.length);
        byte[] nTypes = new byte[4];
        System.arraycopy(dataArray, stxs.length + pkglens.length + nFunIDs.length, nTypes, 0, nTypes.length);
        byte[] nADLER32s = new byte[4];
        System.arraycopy(dataArray, stxs.length + pkglens.length + nFunIDs.length + nTypes.length, nADLER32s, 0, nADLER32s.length);
        byte[] dwTimes = new byte[4];
        System.arraycopy(dataArray, stxs.length + pkglens.length + nFunIDs.length + nTypes.length + nADLER32s.length, dwTimes, 0, dwTimes.length);

        stx = ByteArrayToInt(stxs, 0);
        pkglen = ByteArrayToInt(pkglens, 0);
        nFunID = ByteArrayToInt(nFunIDs, 0);
        nType = ByteArrayToInt(nTypes, 0);
        nADLER32 = ByteArrayToInt(nADLER32s, 0);
        dwTime = ByteArrayToInt(dwTimes, 0);

    }

    public int LoadADLER32(byte[] dataArray) {
        Adler32 checksum = new Adler32();
        return checksum.update(dataArray);
    }

    public int getPkglen() {
        return pkglen;
    }

    public void setPkglen(int pkglen) {
        this.pkglen = pkglen;
    }

    public int getFunID() {
        return nFunID;
    }

    public int GetSize() {
        return 4 * 4 + 2 * 4;
    }

    public byte[] getByteArrayData() {
        // TODO Auto-generated method stub
        byte[] stxs = IntToByteArray(stx);
        System.arraycopy(stxs, 0, byteArrayData, 0, stxs.length);
        byte[] pkglens = IntToByteArray(pkglen);
        System.arraycopy(pkglens, 0, byteArrayData, stxs.length, pkglens.length);
        byte[] nFunIDs = IntToByteArray(nFunID);
        System.arraycopy(nFunIDs, 0, byteArrayData, pkglens.length + stxs.length, nFunIDs.length);
        byte[] nTypes = IntToByteArray(nType);
        System.arraycopy(nTypes, 0, byteArrayData, pkglens.length + stxs.length + nFunIDs.length, nTypes.length);
        byte[] nADLER32s = IntToByteArray(nADLER32);
        System.arraycopy(nADLER32s, 0, byteArrayData, pkglens.length + stxs.length + nFunIDs.length + nTypes.length, nADLER32s.length);
        byte[] dwTimes = IntToByteArray(dwTime);
        System.arraycopy(dwTimes, 0, byteArrayData, pkglens.length + stxs.length + nFunIDs.length + nTypes.length + nADLER32s.length, dwTimes.length);
        return byteArrayData;
    }

    /**
     * @param dataArray login: 24+64=head + uuid
     * @param password
     * @return
     */
    public byte[] packData(byte[] dataArray, String password) {
        Head head = new Head(dataArray);
        byte[] dataArrayReal = new byte[dataArray.length - head.GetSize()];
        System.arraycopy(dataArray, head.GetSize(), dataArrayReal, 0, dataArrayReal.length);
        if ((nType & emNormal) != emNormal) {
            if ((nType & emZip) == emZip) {
                byte[] dataArrayRealTemp = Zlib.compress(dataArrayReal);
                if (dataArrayRealTemp != null) {
                    dataArrayReal = dataArrayRealTemp;
                }
            }
            if ((nType & emEncrypt) == emEncrypt) {
                try {
                    EncryptTool encryptTool = new EncryptTool();
                    byte[] dataArrayRealTemp = encryptTool.EncodeAES(dataArrayReal, password);
                    if (dataArrayRealTemp != null) {
                        dataArrayReal = dataArrayRealTemp;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if ((nType & emAdler32) == emAdler32) {
                head.nADLER32 = LoadADLER32(dataArrayReal);
            }

        }
        head.setPkglen(dataArrayReal.length + head.GetSize());
        byte[] dataArrayRealTemp = new byte[head.getPkglen()];
        System.arraycopy(head.getByteArrayData(), 0, dataArrayRealTemp, 0, head.GetSize());
        System.arraycopy(dataArrayReal, 0, dataArrayRealTemp, head.GetSize(), dataArrayReal.length);
        return dataArrayRealTemp;
    }

    public byte[] unpackData(byte[] dataArray, String password) {
        byte[] dataArrayTemp = null;
        if ((nType & emNormal) != emNormal) {
            if ((nType & emAdler32) == emAdler32) {
                if (LoadADLER32(dataArray) != nADLER32) {
                    return null;
                }
            }

            if ((nType & emEncrypt) == emEncrypt) {
                try {
                    EncryptTool encryptTool = new EncryptTool();
                    dataArrayTemp = encryptTool.DecodeAES(dataArray, password);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    return null;
                }
            }
            if ((nType & emZip) == emZip) {
                if (dataArrayTemp != null) {
                    dataArrayTemp = Zlib.decompress(dataArrayTemp, emMaxBuff);
                } else {
                    dataArrayTemp = Zlib.decompress(dataArray, emMaxBuff);
                }
            }
        } else {
            dataArrayTemp = dataArray;
        }
        return dataArrayTemp;
    }

}
