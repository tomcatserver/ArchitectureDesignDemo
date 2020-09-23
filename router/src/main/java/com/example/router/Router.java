package com.example.router;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

public class Router {
    //主页面模块的path路径，通过该路径跳转到主页面模块（首页、找药、发现、购物车、我的）
    public static final String HOME_PAGE_PATH = "/home/MainActivity";

    public static void initRouter(Application application) {
        //初始化ARouter框架
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application);
    }
}
