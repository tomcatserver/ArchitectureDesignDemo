package com.example.network.commoninterceptor;

import com.example.network.INetworkRequiredInfo;
import com.example.network.encrypt.RequestEncrypt;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonRequestInterceptor implements Interceptor {
    private INetworkRequiredInfo mINetworkRequiredInfo;
    private RequestEncrypt mRequestEncrypt;

    public CommonRequestInterceptor(INetworkRequiredInfo info) {
        mINetworkRequiredInfo = info;
        mRequestEncrypt = new RequestEncrypt();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
//        Request.Builder builder = chain.request().newBuilder();
//        builder.addHeader("os", "android");
//        builder.addHeader("appVersion", this.mINetworkRequiredInfo.getAppVersionCode());
//        builder.addHeader("appName", this.mINetworkRequiredInfo.getAppVersionName());

        Request.Builder builder = null;
        try {
            builder = mRequestEncrypt.encrypt(chain.request()).newBuilder();
            return chain.proceed(builder.build());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
