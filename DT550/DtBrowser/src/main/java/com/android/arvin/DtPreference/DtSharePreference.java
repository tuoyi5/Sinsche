package com.android.arvin.DtPreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arvin on 2017/9/15 0015.
 */

public class DtSharePreference {

    private static final String DT_LOGIN_DATA = "dt_login_data";
    private static final String LOGIN_USER_NAME = "login_user_name";
    private static final String LOGIN_PASSWORD = "login_password";

    private static final String KEEP_PASSWORD = "keep_password";
    private static final String AUTO_LOGIN = "auto_login";


    private static final String DT_SERVER_DATA = "dt_server_data";
    private static final String SERVER_IP = "server_ip";
    private static final String SERVER_PORT = "server_port";

    private static final String DT_ClIENT_DATA = "dt_client_data";
    private static final String CLIENT_SERIAL = "client_serial";
    private static final String CLIENT_NAME = "client_name";

    private static final String DT_USER_DATA = "dt_user_data";
    private static final String PHONE_NUM = "phone_num";

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
        int i = sp.getInt(AUTO_LOGIN, 1);
        return i == 1 ? true : false;
    }


    public static void saveServerData(Context context, String serverIP, String serverPort) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(DT_SERVER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(SERVER_IP, serverIP);
        editor.putString(SERVER_PORT, serverPort);
        editor.commit();
    }

    public static String getServerIP(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_SERVER_DATA, Context.MODE_PRIVATE);
        return sp.getString(SERVER_IP, "");
    }

    public static String getServerPort(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_SERVER_DATA, Context.MODE_PRIVATE);
        return sp.getString(SERVER_PORT, "");
    }

    public static void saveClientData(Context context, String serverIP, String serverPort) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(DT_ClIENT_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(CLIENT_SERIAL, serverIP);
        editor.putString(CLIENT_NAME, serverPort);
        editor.commit();
    }

    public static String getClientName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_ClIENT_DATA, Context.MODE_PRIVATE);
        return sp.getString(CLIENT_NAME, "");
    }

    public static String getClientSerial(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_ClIENT_DATA, Context.MODE_PRIVATE);
        return sp.getString(CLIENT_SERIAL, "");
    }

    public static void saveUserData(Context context, String telephoneNumber) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(DT_USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(PHONE_NUM, telephoneNumber);
        editor.commit();
    }


    public static String getPhoneNum(Context context) {
        SharedPreferences sp = context.getSharedPreferences(DT_USER_DATA, Context.MODE_PRIVATE);
        return sp.getString(PHONE_NUM, "");
    }

}
