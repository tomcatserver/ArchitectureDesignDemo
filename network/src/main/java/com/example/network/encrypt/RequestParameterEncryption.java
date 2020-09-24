package com.example.network.encrypt;

import com.example.network.utils.DigestUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 请求参数加密
 */
public class RequestParameterEncryption {
    private static final String SIGNATURE_KEY = "signature";

    public Map<String, String> encrypt(Map<String, String> params) {
        Map<String, String> nonnullParams = new HashMap<>();
        if (params != null) {
            nonnullParams = new HashMap<>(params);
        }

        if (nonnullParams.containsKey(SIGNATURE_KEY)) {
            nonnullParams.remove(SIGNATURE_KEY);
        }

        String sec = DigestUtil.MD5;
//        VenusKey key = VenusKey.getKey();
//        String secret = key == null ? "" : MoreObjects.firstNonNull(key.getPlainKey(), "");
        String secret = "";

        Map<String, String> secParams = new HashMap<String, String>();
        secParams.putAll(nonnullParams);

//        ClientInfo clientInfo = ClientInfo.getInstance();
        secParams.put("signature_method", sec);
//        secParams.put("timestamp", key == null ? "0" : String.valueOf(key.timestamp()));
        secParams.put("timestamp", "0");
        secParams.put("trader", "androidphone");
        secParams.put("traderName", "mobile_android");

        String signature = createSignature(secParams, secret, sec);
        secParams.put(SIGNATURE_KEY, signature);

        return Collections.unmodifiableMap(secParams);
    }

    private String createSignature(Map<String, String> params,
                                   String secret,
                                   String secType) {
        if (params == null || params.size() <= 0) {
            return "";
        }

        TreeMap<String, String> sortedMap = new TreeMap<String, String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
//            sortedMap.put(entry.getKey().toLowerCase(Locale.ENGLISH),
//                    MoreObjects.firstNonNull(entry.getValue(), ""));
            sortedMap.put(entry.getKey().toLowerCase(Locale.ENGLISH),
                    entry.getValue() == null ? "" : entry.getValue());
        }

        // 先将参数以其参数名的字典序升序进行排序
        Set<Map.Entry<String, String>> entrys = sortedMap.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            basestring.append(param.getKey()).append("=").append(param.getValue());
        }
        basestring.append(secret);

        if (DigestUtil.MD5.equals(secType)) {
            return DigestUtil.md5Hex(basestring.toString());
        } else {
            return basestring.toString();
        }
    }
}
