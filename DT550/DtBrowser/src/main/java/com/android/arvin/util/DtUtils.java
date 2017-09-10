package com.android.arvin.util;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.util.Log;

import com.android.arvin.DataText.DeviceTest;
import com.android.arvin.DataText.SubItemTest;
import com.android.arvin.R;

import java.util.ArrayList;

public class DtUtils {

    private static final String TAG = DtUtils.class.getSimpleName();

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

    public static DeviceTest getTestData(Context context, int i) {
        DeviceTest deviceTest = new DeviceTest();
        deviceTest.setDeviceName(context.getResources().getString(R.string.dt_device_name) + ": " + i);
        deviceTest.setDeviceRunningStatus(i % 2 == 0);
        deviceTest.setWaterStatus(i % 2 == 1);
        ArrayList<SubItemTest> arrayList = new ArrayList<>();

        for (int j = 0; j < 6 + i; j++) {
            SubItemTest subItemTest;
            if (j % 4 == 0) {
                subItemTest = new SubItemTest(
                        R.drawable.liquid_state_text_yes_bg,
                        String.format(context.getString(R.string.lack_of_liquid), context.getString(R.string.lack_of_liquid_yes)),
                        context.getString(R.string.nitrite_nitrogen),
                        String.format(context.getString(R.string.concentration_unit), 0.01f),
                        String.format(context.getString(R.string.measuring_time), "2017-09-10 15:00:20"));
            } else if (j % 4 == 1) {
                subItemTest = new SubItemTest(
                        R.drawable.liquid_state_text_no_bg,
                        String.format(context.getString(R.string.lack_of_liquid), context.getString(R.string.lack_of_liquid_no)),
                        context.getString(R.string.ambient_temperature),
                        String.format(context.getString(R.string.temperature_unit), 12.5f),
                        String.format(context.getString(R.string.measuring_time), "2017-09-10 15:00:20"));

            } else if (j % 4 == 2) {
                subItemTest = new SubItemTest(
                        R.drawable.liquid_state_text_yes_bg,
                        String.format(context.getString(R.string.lack_of_liquid), context.getString(R.string.lack_of_liquid_yes)),
                        context.getString(R.string.total_dissolved_solids),
                        String.format(context.getString(R.string.tds_unit), 112.35f),
                        String.format(context.getString(R.string.measuring_time), "2017-09-10 15:00:20"));

            } else {
                subItemTest = new SubItemTest(
                        R.drawable.liquid_state_text_no_bg,
                        String.format(context.getString(R.string.lack_of_liquid), context.getString(R.string.lack_of_liquid_no)),
                        context.getString(R.string.turbidity),
                        String.format(context.getString(R.string.turbidity_unit), 0.32f),
                        String.format(context.getString(R.string.measuring_time), "2017-09-10 15:00:20"));
            }

            arrayList.add(subItemTest);
        }
        deviceTest.setSubItemTestList(arrayList);


        return deviceTest;
    }

}
