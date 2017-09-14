package com.android.arvin.util;

import android.content.Context;

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

}
