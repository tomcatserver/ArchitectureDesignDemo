package com.example.architecturedesign;

import android.app.Application;

import com.example.base.util.ContextUtils;
import com.example.base.util.YWLogUtil;
import com.example.network.NetworkApi;
import com.example.router.Router;
import com.example.webview.WebViewUtil;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtils.getInstance().setContext(getApplicationContext());
        Router.initRouter(this);
        NetworkApi.init(new NetworkRequestInfo(this));
        YWLogUtil.sLogEnabled = BuildConfig.DEBUG;
        WebViewUtil.initWebView(getApplicationContext());
    }
}
