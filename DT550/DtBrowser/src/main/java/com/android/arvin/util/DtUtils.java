package com.android.arvin.util;

import android.content.Context;

import com.android.arvin.data.DeviceHistoryData;
import com.github.mikephil.charting.data.Entry;
import com.sinsche.core.ws.client.android.DT550HisDataRsp;
import com.sinsche.core.ws.client.android.struct.DT550HisDataRspItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DtUtils {

    private static final String TAG = DtUtils.class.getSimpleName();
    private static SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat hmsFormat = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat dmhmFormat = new SimpleDateFormat("MMM dd日 HH:mm");

    public static boolean isNullOrEmpty(final String s) {
        return s == null || s.equals("");
    }

    public static int dip2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static double formatDouble(double value, int dig) {
        StringBuffer stringBuffer = new StringBuffer("0.");
        for (int i = 0; i < dig; i++) {
            stringBuffer.append("0");
        }
        DecimalFormat df = new DecimalFormat(stringBuffer.toString());

        return Double.valueOf(df.format(value));
    }

    public static String formatFloat(float value, int dig) {
        StringBuffer stringBuffer = new StringBuffer("0.");
        for (int i = 0; i < dig; i++) {
            stringBuffer.append("0");
        }
        DecimalFormat df = new DecimalFormat(stringBuffer.toString());

        return df.format(value);
    }

    public static double meanValue(Collection<DeviceHistoryData.DeviceHisSubItemData> collection) {
        double mean = 0;
        if (collection != null && collection.size() > 0) {
            for (DeviceHistoryData.DeviceHisSubItemData data : collection) {
                mean += data.getDbData();
            }
            mean = mean / collection.size();
        }
        return mean;
    }

    public static String formatHmsTime(long t) {
        String time = hmsFormat.format(new Date(t));
        return time;
    }

    public static String formatymdHmsTime(long t) {
        String time = ymdFormat.format(new Date(t));
        return time;
    }

    public static String formDmhmFormat(long t) {
        String time = dmhmFormat.format(new Date(t));
        return time;
    }


    public static void FormHistoricData(DeviceHistoryData mDeviceHistoryData, ArrayList<Entry> hisDataList, DT550HisDataRsp dt550HisDataRsp) {

        DeviceHistoryData deviceHistoryData = new DeviceHistoryData();
        deviceHistoryData.setDeviceCode(dt550HisDataRsp.getStrDeviceSerial());
        deviceHistoryData.setSubItemDataCode(dt550HisDataRsp.getStrItemCode());

        List<DT550HisDataRspItem> list = dt550HisDataRsp.getListDT550HisDataRspItem();

        for (DT550HisDataRspItem dt550HisDataRspItem : list) {

            DeviceHistoryData.DeviceHisSubItemData deviceHisSubItemData = deviceHistoryData.getDeviceHisSubItemData(
                    dt550HisDataRspItem.getlTestTime(), dt550HisDataRspItem.getnFormat(), dt550HisDataRspItem.getDbData());

            SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");
            String time = mFormat.format(new Date(dt550HisDataRspItem.getlTestTime()));

            deviceHistoryData.getDeviceHisSubItemDataList().add(deviceHisSubItemData);
        }
        mDeviceHistoryData = deviceHistoryData;

        ArrayList<Entry> values = new ArrayList<Entry>();
        List<DeviceHistoryData.DeviceHisSubItemData> subItemDatas = mDeviceHistoryData.getDeviceHisSubItemDataList();
        List<Float> yScope = new ArrayList<>();
        for (DeviceHistoryData.DeviceHisSubItemData data : subItemDatas) {
            float x = (float) data.getTestTime();
            float y = (float) data.getDbData();
            values.add(new Entry(x, y));
            yScope.add(y);
        }

        Collections.sort(yScope);

        if (dt550HisDataRsp.getStrMax() == null) {
            float max = yScope.get(yScope.size() - 1);
            mDeviceHistoryData.setStrMax(String.valueOf(max * 1.1));
        } else {
            mDeviceHistoryData.setStrMax(dt550HisDataRsp.getStrMax());
        }

        if (dt550HisDataRsp.getStrMin() == null) {
            mDeviceHistoryData.setStrMin(String.valueOf(yScope.get(0)));
        } else {
            mDeviceHistoryData.setStrMin(dt550HisDataRsp.getStrMin());
        }

        hisDataList.addAll(values);
    }


}
