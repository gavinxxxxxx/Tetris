package me.gavin.game.tetris.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * JsonUtil for Gson
 *
 * @author gavin.xiong 2016/12/6
 * @Expose 过滤字段：serialize和deserialize。默认都是true。 excludeFieldsWithoutExposeAnnotation 后有效
 */
public class JsonUtil {

    private static Gson gson = new Gson();


    public static <T> T toObj(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * List<Object> list = JsonUtil.toList(json, new TypeToken<ArrayList<Object>>(){});
     */
    public static <T> List<T> toList(String json, TypeToken typeToken) {
        try {
            return gson.fromJson(json, typeToken.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Object[] objects = JsonUtil.toArray(json1, User[].class);
     */
    public static <T> T[] toArray(String json, Class<T[]> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
