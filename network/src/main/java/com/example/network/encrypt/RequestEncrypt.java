package com.example.network.encrypt;

import com.example.network.utils.BaseParameterUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestEncrypt {
    private RequestParameterEncryption mRequestParameterEncryption;

    public RequestEncrypt() {
        mRequestParameterEncryption = new RequestParameterEncryption();
    }

    public Request encrypt(Request request) throws IllegalAccessException {
        Request rq = null;
        if ("Get".equalsIgnoreCase(request.method())) {
            rq = handleGet(request);
        } else if ("post".equalsIgnoreCase(request.method())) {
            rq = handlePost(request);
        } else {
            throw new IllegalAccessException("Not supported HTTP method: " + request.method());
        }
        return addCommonHeads(rq);
    }

    private Request addCommonHeads(Request request) {
        Request.Builder builder = request.newBuilder();
        Gson gson = new Gson();
        String baseParam = gson.toJson(BaseParameterUtil.baseParam());
//        String baseParam = "{\"clientAppVersion\":\"6.0.6\",\"clientAppVersionCode\":624,\"clientSystem\":\"android,Redmi Note 4X,24,7.0\",\"clientVersion\":\"7.0\",\"deviceCode\":\"00000000-34aa-7b67-0000-000000000000\",\"nettype\":\"WiFi\",\"phoneType\":\"android,Redmi Note 4X,24,7.0\",\"trader\":\"androidphone\",\"traderName\":\"mobile_android\"}";
        builder.addHeader(
                "clientInfo", baseParam);

        builder.addHeader("userToken", "");

        builder.addHeader("provinceId", String.valueOf(1));

//        for (Map.Entry<String, String> item : Venus.getUserHeaders().entrySet()) {
//            builder.addHeader(item.getKey(), item.getValue());
//        }
        return builder.build();

    }

    private Request handlePost(Request request) {
        RequestBody requestBody = request.body();
        Map<String, String> encryptParams =
                mRequestParameterEncryption.encrypt(parse(requestBody));

        if (requestBody instanceof MultipartBody) {
            StringBuilder stringBuilder = new StringBuilder(request.url().url().toString());
            Request.Builder builder = request.newBuilder();
            if (!stringBuilder.toString().contains("?")) {
                stringBuilder.append("?");
            } else {
                stringBuilder.append("&");
            }

            for (Map.Entry<String, String> entry : encryptParams.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            HttpUrl newUrl = HttpUrl.parse(stringBuilder.toString());
            return builder.url(newUrl).build();
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : encryptParams.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();
        return request.newBuilder()
                .post(body)
                .header("Content-Length", String.valueOf(body.contentLength()))
                .header("Content-Type", body.contentType().toString())
//                .header("Content-Type", "application/json;charset=UTF-8")
                .build();
    }

    private Request handleGet(Request request) {
//        mRequestParameterEncryption.encrypt(HttpUrl.parse(request.url().toString()));
        return request;
    }

    private Map<String, String> parse(RequestBody body) {
        Map<String, String> params = new HashMap<String, String>(BaseParameterUtil.publicParams());

        if (body instanceof FormBody) {
            FormBody formBody = (FormBody) body;
            for (int i = 0; i < formBody.size(); i++) {
                params.put(formBody.name(i), formBody.value(i));
            }
            return Collections.unmodifiableMap(params);
        } else if (body instanceof MultipartBody) {
            return params;
        } else {
            try {
                if (body != null && body.contentLength() > 0) {
                    throw new IllegalStateException("Should use FormUrlEncoded");
                } else {
                    return Collections.unmodifiableMap(params);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

    }
}
