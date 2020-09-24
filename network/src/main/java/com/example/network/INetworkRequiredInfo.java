package com.example.network;

import android.app.Application;

/**
 * 定义网络需要的基本数据获取
 */
public interface INetworkRequiredInfo {
    String getAppVersionName();

    String getAppVersionCode();

    boolean isDebug();

    Application getApplicationContext();
}
