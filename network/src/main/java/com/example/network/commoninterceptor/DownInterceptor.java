package com.example.network.commoninterceptor;

import com.example.network.body.FileResponseBody;
import com.example.network.inter.IDownFileProgress;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownInterceptor implements Interceptor {
    private IDownFileProgress mProgress;

    public DownInterceptor(IDownFileProgress progress) {
        mProgress = progress;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originResponse = chain.proceed(chain.request());
        return originResponse.newBuilder().body(new FileResponseBody(originResponse, mProgress)).build();
    }
}
