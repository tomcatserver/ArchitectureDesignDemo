package com.example.network.commoninterceptor;

import com.example.base.util.YWLogUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class CommonResponseInterceptor implements Interceptor {
    public CommonResponseInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        YWLogUtil.e("tag", "CommonResponseInterceptor------requestTime= " + (System.currentTimeMillis() - requestTime));
        return chain.proceed(chain.request());
    }
}
