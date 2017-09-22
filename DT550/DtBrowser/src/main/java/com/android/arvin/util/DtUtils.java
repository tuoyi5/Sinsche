package com.android.arvin.util;

import android.app.PendingIntent;
import android.content.Context;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.style.TtsSpan;
import android.util.Log;

import com.android.arvin.Receiver.SMSReceiver;
import com.android.arvin.data.DeviceHistoryData;
import com.github.mikephil.charting.data.Entry;
import com.sinsche.core.ws.client.android.DT550HisDataRsp;
import com.sinsche.core.ws.client.android.struct.DT550HisDataRspItem;

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

    private static final String CHU = "China Unicom";
    private static final String CTC = "China Telecom";
    private static final String CMCC = " China Mobile";


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
        int digits = (int) Math.pow(10, dig);
        double b = (double) (Math.round(value * digits)) / digits;
        return b;
    }

    public static float formatFloat(float value, int dig) {
        int digits = (int) Math.pow(10, dig);
        float b = (float) (Math.round(value * digits)) / digits;
        return b;
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

    public static boolean isSimCard(final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMSI = telephonyManager.getSubscriberId();
        if (IMSI == null || IMSI.length() == 0) {
            return false;
        }
        return true;
    }

    public static String getPhoneNumber(final Context context) {
        if (!isSimCard(context)) {
            return null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String number = telephonyManager.getLine1Number();
        return number;
    }

    public static String getSimOperator(final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telephonyManager.getSimOperator();
        if (operator != null) {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                return CMCC;//移动
            } else if (operator.equals("46001")) {
                return CHU;//联通
            } else if (operator.equals("46003")) {
                return CTC;//电信
            }
        }
        return null;
    }

    public static void sendMessage(String simOperator, Context context, final SMSReceiver smsReceiver) {
        Log.i(TAG, "sendMessage: " + simOperator);
        switch (simOperator) {
            case CMCC:
                smsReceiver.setNumberAddress("10086");
                sendMessage("10086", "cx", context);
                break;
            case CHU:
                smsReceiver.setNumberAddress("10010");
                sendMessage("10010", "cx", context);
                break;
            case CTC:
                smsReceiver.setNumberAddress("10001");
                sendMessage("10001", "501", context);
                break;
        }
    }

    public static void sendMessage(String number, String message, Context context) {
        SMSCore smsCore = new SMSCore();
        smsCore.SendSMS2(number, message, context);
    }


}
