package com.example.network;

import com.example.base.util.AppExecutors;
import com.example.network.inter.IDownFileToDisk;
import com.example.network.utils.ResponseBodyToDiskUtil;

import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.ResponseBody;

public class CommonNetworkApi extends NetworkApi {
    private static volatile CommonNetworkApi sInstance;

    public static CommonNetworkApi getInstance() {
        if (sInstance == null) {
            synchronized (CommonNetworkApi.class) {
                if (sInstance == null) {
                    sInstance = new CommonNetworkApi();
                }
            }
        }
        return sInstance;
    }

    public static <T> T getService(Class<T> service) {
        return getInstance().getRetrofit(service).create(service);
    }


    protected <T> Function<T, T> getAppErrorHandler() {
        return new Function<T, T>() {
            @Override
            public T apply(T response) throws Exception {
                //response中code码不会0 出现错误
//                if (response instanceof TecentBaseResponse && ((TecentBaseResponse) response).showapiResCode != 0) {
//                    ExceptionHandle.ServerException exception = new ExceptionHandle.ServerException();
//                    exception.code = ((TecentBaseResponse) response).showapiResCode;
//                    exception.message = ((TecentBaseResponse) response).showapiResError != null ? ((TecentBaseResponse) response).showapiResError : "";
//                    throw exception;
//                }
                return response;
            }
        };
    }

    @Override
    protected Interceptor getInterceptor() {
//        return new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                String timeStr = TecentUtil.getTimeStr();
//                Request.Builder builder = chain.request().newBuilder();
//                builder.addHeader("Source", "source");
//                builder.addHeader("Authorization", TecentUtil.getAuthorization(timeStr));
//                builder.addHeader("Date", timeStr);
//                return chain.proceed(builder.build());
//            }
//        };
        return null;
    }

    @Override
    public String getFormal() {
//        return "http://service-o5ikp40z-1255468759.ap-shanghai.apigateway.myqcloud.com/";
//        return "https://gateway.fangkuaiyi.com";
//        return "https://timgsa.baidu.com/";
//        return "https://eaifjfe.com/";
        return "https://eaifjfe.com/";
    }

    public static void writeResponseBodyToDisk(ResponseBody responseBody, IDownFileToDisk downFileToDisk) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final String path = ResponseBodyToDiskUtil.writeResponseBodyToDisk(responseBody);
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (downFileToDisk != null) {
                            downFileToDisk.onResult(path);
                        }
                    }
                });

            }
        });
    }

    @Override
    public String getTest() {
        return "https://eaifjfe.com/";
    }
}
