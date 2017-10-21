package me.gavin.game.tetris.util;

import android.content.Context;

/**
 * SharedPreferences 数据存储工具类
 *
 * @author gavin.xiong
 */
public class SPUtil {

    private static String PREFERENCE = "PREFERENCE";

    public static void saveString(Context context, String key, String value) {
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static String getString(Context context, String key) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getString(key, "");
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }

    public static void saveLong(Context context, String key, long value) {
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putLong(key, value)
                .apply();
    }

    public static long getLong(Context context, String key) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getLong(key, 0L);
    }

    public static void saveInt(Context context, String key, int value) {
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static int getInt(Context context, String key) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getInt(key, 0);
    }

}
