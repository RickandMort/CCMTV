package com.linlic.ccmtv.yx.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class JsonUtils {
    private static Gson gson = new Gson();

    @Nullable
    public static <T> T fromJson(Class<T> k, String json, String... keys) {
        if (TextUtils.isEmpty(json))
            return null;
        if (keys == null || keys.length == 0)
            return a(k, json);
        return a(k, getJsonValue(json, keys));
    }


    public static String toJson(Object o) {
        if (o == null)
            return null;
        return gson.toJson(o);
    }

    public static <T> List<T> parseList(Class<T> k, String json, String... keys) {
        List<T> list = new ArrayList<>();
        if (TextUtils.isEmpty(json))
            return list;
        if (keys != null && keys.length != 0)
            json = getJsonValue(json, keys);
        List<T> temp = fromJsonArray(json, k);
        list.addAll(temp);
        return list;
    }


    public static <T> List<T> fromJsonArray(String json, Class<T> k) {
        List<T> list = new ArrayList<>();
        if (TextUtils.isEmpty(json))
            return list;
        try {
            JSONArray array = new JSONArray(json);
            if (array.length() == 0)
                return list;
            for (int i = 0; i < array.length(); i++) {
                T t = a(k, String.valueOf(array.get(i)));
                if (t != null)
                    list.add(t);
            }
        } catch (Exception e) {

        }
        return list;
    }

    private static <T> T a(Class<T> k, String json) {
        try {
            return gson.fromJson(json, k);
        } catch (Exception e) {

        }
        return null;
    }

    public static String getJsonValue(String json, String... keys) {
        if (TextUtils.isEmpty(json))
            return "";
        if (keys == null || keys.length == 0)
            return json;
        String temp = json;
        //取string到倒数第二个key
        for (String key : keys) {
            temp = b(temp, key);
            if (TextUtils.isEmpty(temp) || temp.equalsIgnoreCase("null"))
                return "";
        }
        return temp;
    }


    public static String getDefaultValue(String json, CharSequence defaultValue, String... keys) {
        if (TextUtils.isEmpty(json))
            return String.valueOf(defaultValue);
        if (keys == null || keys.length == 0)
            return json;
        String temp = json;
        //取string到倒数第二个key
        for (String key : keys) {
            temp = b(temp, key);
            if (TextUtils.isEmpty(temp) || temp.equalsIgnoreCase("null"))
                return String.valueOf(defaultValue);
        }
        return temp;
    }

    public static String b(String json, String key) {
        try {
            JSONObject object = new JSONObject(json);
            return object.optString(key);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    /***
     *
     * @return 返回集合
     * */
    public <T> List<T> fromGson(String s, Class<T> k, @Nullable String key) {
        if (TextUtils.isEmpty(s))
            return null;
        List<T> list = new ArrayList<>();
        try {
            if (TextUtils.isEmpty(key)) {
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    T t = getT(array.getString(i), k);
                    if (t != null)
                        list.add(t);
                }
                return list;
            }
            JSONObject object = new JSONObject(s);
            if (!object.has(key)) {
                //LogUtil.log("gson转换失败不含有这个key", key);
                return null;
            }
            JSONArray array = object.getJSONArray(key);
            if (array != null && array.length() != 0) {
                for (int i = 0; i < array.length(); i++) {
                    T t = getT(array.getString(i), k);
                    if (t != null)
                        list.add(t);
                }
                return list;
            }
            return null;
        } catch (Exception e) {
            //LogUtil.log("gson转换错误", e.getMessage());
            return null;
        }
    }

    @Nullable
    private <T> T getT(String s, Class<T> k) {
        try {
            T t = gson.fromJson(s, k);
            return t;
        } catch (Exception e) {
            //LogUtil.log("Gson转换失败，不是正常json格式", e.getMessage());
        }
        return null;
    }
}
