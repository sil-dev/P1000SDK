package com.example.youcloudp1000sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shankar.savant on 21-01-2017.
 */
public class Session {

    public static String getString(Context context, String key) {
        SharedPreferences pref;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode

        return pref.getString(key, "");
    }
    public static int getInt(Context context, String key) {
        SharedPreferences pref;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode
        return pref.getInt(key,0);
    }
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences pref;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode
        return pref.getBoolean(key,false);
    }

    public static int setInt(Context context, String key, int value) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode
        editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
        return 1;
    }
    public static String setString(Context context, String key, String value) {   SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode
        editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
        return "";
    }
    public static boolean setBoolean(Context context, String key, boolean value) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode
        editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return true;
    }

    //Android Log
    public static String appendLog(Context context, String value) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode
        editor = pref.edit();

        value = getLog(context)+"\n"+value;

        editor.putString("MyLog", value);
        editor.commit();
        return "";
    }
    public static String getLog(Context context) {
        SharedPreferences pref;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode

        return pref.getString("MyLog", "");
    }
    public static String clearLog(Context context) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        pref = context
                .getSharedPreferences(Constants.sessionName, Context.MODE_APPEND); // 0 - for private
        // mode
        editor = pref.edit();

        editor.putString("MyLog", "");
        editor.commit();
        return "";
    }
}
