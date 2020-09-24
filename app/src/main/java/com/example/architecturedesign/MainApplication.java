package com.example.architecturedesign;

import android.app.Application;

import com.example.network.NetworkApi;
import com.example.router.Router;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Router.initRouter(this);
        NetworkApi.init(new NetworkRequestInfo(this));
    }
}
