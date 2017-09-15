package com.android.arvin.DtPreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arvin on 2017/9/15 0015.
 */

public class DtSharePreference {

    public static final String DT_LOGIN_DATA = "dt_login_data";
    public static final String LOGIN_USER_NAME = "login_user_name";
    public static final String LOGIN_PASSWORD = "login_password";

    public static final String KEEP_PASSWORD = "keep_password";
    public static final String AUTO_LOGIN = "auto_login";

    public static void saveLoginData(Context context, String username, String password) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(DT_LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOGIN_USER_NAME, username);
        editor.putString(LOGIN_PASSWORD, password);
        editor.commit();
    }

    public static String getLoginUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_LOGIN_DATA, Context.MODE_PRIVATE);
        return sp.getString(LOGIN_USER_NAME, "");
    }

    public static String getLoginPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_LOGIN_DATA, Context.MODE_PRIVATE);
        return sp.getString(LOGIN_PASSWORD, "");
    }

    public static void saveKeepPassword(Context context, int keep) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(DT_LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEEP_PASSWORD, keep);
        editor.commit();
    }

    public static void saveAutoLogin(Context context, int auto) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(DT_LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(AUTO_LOGIN, auto);
        editor.commit();
    }

    public static boolean getKeepPassword(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_LOGIN_DATA, Context.MODE_PRIVATE);
        int i = sp.getInt(KEEP_PASSWORD, 0);
        return i == 1 ? true : false;
    }

    public static boolean getAutoLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_LOGIN_DATA, Context.MODE_PRIVATE);
        int i = sp.getInt(AUTO_LOGIN, 0);
        return i == 1 ? true : false;
    }
}
