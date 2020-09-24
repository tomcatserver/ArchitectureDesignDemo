package com.example.network.utils;

import android.util.ArrayMap;

import java.util.HashMap;
import java.util.Map;

public class BaseParameterUtil {
    public static Map<String, String> baseParam() {
        Map basicParams = new ArrayMap();
        basicParams.put("nettype", "WiFi"); // 网络环境
//        basicParams.put("deviceCode", Device.uuid); // 设备号
        basicParams.put("deviceCode", "00000000-34aa-7b67-0000-000000000000"); // 设备号
        basicParams.put("phoneType", "android,Redmi Note 4X,24,7.0"); // 设备类型
        basicParams.put("clientAppVersion", "6.0.6"); // app版本号
        basicParams.put("clientAppVersionCode", "624"); // app版本code
        basicParams.put("clientSystem", "android,Redmi Note 4X,24,7.0"); // 客户端系统类型
        basicParams.put("clientVersion", android.os.Build.VERSION.RELEASE); // 客户端系统版本
        return basicParams;
    }

    public static Map<String, String> publicParams() {
        Map<String, String> publicParams = new HashMap<>();
        publicParams.put("province", String.valueOf(1));
        publicParams.put("provinceId", String.valueOf(1));
        publicParams.put("provinceName", "上海市");
        publicParams.put("cityName", "上海市");
        publicParams.put("locateProvinceId", String.valueOf(1));
        publicParams.put("locateCityName", "上海市");
        publicParams.put("abId", "7893400781");
        return publicParams;
    }
}
