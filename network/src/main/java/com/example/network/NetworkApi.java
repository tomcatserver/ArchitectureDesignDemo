package com.example.network;

import android.util.ArrayMap;

import com.example.network.commoninterceptor.CommonRequestInterceptor;
import com.example.network.commoninterceptor.CommonResponseInterceptor;
import com.example.network.commoninterceptor.DownInterceptor;
import com.example.network.environment.EnvironmentActivity;
import com.example.network.environment.IEnvironment;
import com.example.network.errorhandler.HttpErrorHandler;
import com.example.network.inter.IDownFileProgress;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NetworkApi implements IEnvironment {
    private static final int CACHE_SIZE = 100 * 1024 * 1024;
    private static final int CONNECT_TIME_OUT = 20;
    private static final int READ_TIME_OUT = 20;
    private static final int WRITE_TIME_OUT = 20;

    private static INetworkRequiredInfo sINetworkRequiredInfo;
    private static Map<String, Retrofit> sRetrofitMap = new ArrayMap<>();
    private String mBaseUrl;
    private OkHttpClient mOkHttpClient;
    private static boolean sIsFormal = true;

    public NetworkApi() {
        if (!sIsFormal) {
            mBaseUrl = getTest();
        }
        mBaseUrl = getFormal();
    }

    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        sINetworkRequiredInfo = networkRequiredInfo;
        sIsFormal = EnvironmentActivity.isOfficialEnvironment(sINetworkRequiredInfo.getApplicationContext());
    }

    protected Retrofit getRetrofit(Class service) {
        String name = mBaseUrl + service.getName();
        if (sRetrofitMap.get(name) != null) {
            return sRetrofitMap.get(name);
        }
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(mBaseUrl);
        builder.client(getOKHttpClient());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        sRetrofitMap.put(name, retrofit);
        return retrofit;
    }

    private OkHttpClient getOKHttpClient() {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (getInterceptor() != null) {
                builder.addInterceptor(getInterceptor());
            }
            builder.cache(new Cache(sINetworkRequiredInfo.getApplicationContext().getCacheDir(), CACHE_SIZE));
            builder.addInterceptor(new CommonRequestInterceptor(sINetworkRequiredInfo));
            builder.addInterceptor(new CommonResponseInterceptor());
            builder.addInterceptor(new DownInterceptor(getDownFileProgress()));
            if (sINetworkRequiredInfo != null && sINetworkRequiredInfo.isDebug()) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);
            }
            builder.connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
            builder.readTimeout(READ_TIME_OUT, TimeUnit.SECONDS);
            builder.writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS);

            //允许失败重试
            builder.retryOnConnectionFailure(true);

            mOkHttpClient = builder.build();
//            handleSSLVerifier(mOkHttpClient);
        }
        return mOkHttpClient;
    }

    protected abstract IDownFileProgress getDownFileProgress();

    /**
     * 忽略本地证书验证
     *
     * @param okHttpClient
     */
    private void handleSSLVerifier(OkHttpClient okHttpClient) {
        OkHttpClient sClient = okHttpClient;
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }
            }, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier hv1 = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        String workerClassName = "okhttp3.OkHttpClient";
        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(sClient, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactory");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(sClient, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable observable = upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(getAppErrorHandler())
                        .onErrorResumeNext(new HttpErrorHandler<T>());
                observable.subscribe(observer);
                return observable;
            }
        };
    }

    protected abstract <T> Function<? super T, ?> getAppErrorHandler();


    protected abstract Interceptor getInterceptor();
}
