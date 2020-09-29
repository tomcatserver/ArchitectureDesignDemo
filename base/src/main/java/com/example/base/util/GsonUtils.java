
package com.example.base.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final Gson LOCAL_GSON = createLocalGson();
    private static final Gson REMOTE_GSON = createRemoteGson();

    private static GsonBuilder createLocalGsonBuilder() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setDateFormat(DATE_FORMAT);
        return gsonBuilder;
    }

    private static Gson createLocalGson() {
        return createLocalGsonBuilder().create();
    }

    private static Gson createRemoteGson() {
        return createLocalGsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public static Gson getLocalGson() {
        return LOCAL_GSON;
    }

    public static <T> T fromLocalJson(String json, Class<T> clazz) throws JsonSyntaxException {
        try {
            return LOCAL_GSON.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static <T> T fromLocalJson(String json, Type typeOfT) {
        return LOCAL_GSON.fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return LOCAL_GSON.toJson(src);
    }

    public static String toRemoteJson(Object src) {
        return REMOTE_GSON.toJson(src);
    }
}
