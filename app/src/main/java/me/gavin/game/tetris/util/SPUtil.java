package me.gavin.game.tetris.util;

import android.content.Context;

import me.gavin.game.tetris.app.App;

/**
 * SharedPreferences 数据存储工具类
 *
 * @author gavin.xiong
 */
public class SPUtil {

    private static String PREFERENCE = "PREFERENCE";

    public static void saveString(String key, String value) {
        App.get().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static String getString(String key) {
        return App.get().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getString(key, "");
    }

    public static void saveBoolean(String key, boolean value) {
        App.get().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static boolean getBoolean(String key) {
        return App.get().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }

    public static void saveLong(String key, long value) {
        App.get().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putLong(key, value)
                .apply();
    }

    public static long getLong(String key) {
        return App.get().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getLong(key, 0L);
    }

    public static void saveInt(String key, int value) {
        App.get().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static int getInt(String key) {
        return App.get().getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getInt(key, 0);
    }

}
