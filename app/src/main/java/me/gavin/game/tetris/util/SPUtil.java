package me.gavin.game.tetris.util;

import android.content.Context;

/**
 * SharedPreferences 数据存储工具类
 *
 * @author gavin.xiong
 */
public class SPUtil {

    private static String PREFERENCE = "PREFERENCE";

    /**
     * 存储字符串数据类型
     */
    public static void saveString(Context context, String key, String value) {
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    /**
     * 返回String类型数据，默认是空字符串；
     */
    public static String getString(Context context, String key) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getString(key, "");
    }

    /**
     * 存储boolean数据类型
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    /**
     * 返回boolean类型数据，默认是false；
     */
    public static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }

}
