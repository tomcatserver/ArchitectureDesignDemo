package com.example.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.base.BuildConfig;
import com.example.base.util.YWLogUtil;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebSettingsExtension;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;


public class WebViewSetting {
    private static final int TEXT_ROOM = 100;
    private static final int DEFAULT_FONT_SIZE = 16;
    private static final int MINI_MUM_FONT_SIZE = 10;
    private static final String TAG = WebViewSetting.class.getSimpleName();
    private static final int APP_CACHE = 1024 * 1024 * 100;
    private WebViewSetting mWebViewSetting;
    private WebSettings mWebSettings;

    public static WebViewSetting getInstance() {
        return new WebViewSetting();
    }

    public void toSetting(WebView webView) {
        settings(webView);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void settings(WebView webView) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webView.enableSlowWholeDocumentDraw();
//        }
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true); //告诉WebView启用JavaScript执行。
        mWebSettings.setSupportZoom(true); //设置WebView是否应该使用其屏幕上的缩放控件和手势支持缩放。
        mWebSettings.setBuiltInZoomControls(false); //设置WebView是否应该使用其内置的缩放机制
        if (isNetworkConnected(webView.getContext())) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //覆盖缓存的使用方式。默认缓存模式
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //覆盖缓存的使用方式。网络缓存模式
        }
//        mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        mWebSettings.setTextZoom(TEXT_ROOM);  // textZoom:100表示正常，120表示文字放大1.2倍 文字大小
        //该方法可以获取当前文字大小
//        mWebSettings.getTextZoom();
        mWebSettings.setDatabaseEnabled(true); //设置是否启用数据库存储API。
        mWebSettings.setAppCacheEnabled(true); //设置是否应该启用应用程序缓存API。

//        mWebSettings.setUserAgent(); //设置当前ua
        mWebSettings.setLoadsImagesAutomatically(true);  //有图：正常加载显示所有图片
        mWebSettings.setBlockNetworkImage(false);  //是否阻塞加载网络图片  协议http or https

        //设置当前UA
//        mWebSettings.setUserAgentString(String ua);
        //获取当前ua
//        mWebSettings.getUserAgentString();
        //IX5WebSettingsExtension.PicModel_NORMAL正常加载显示所有图片
        //IX5WebSettingsExtension.PicModel_NoPic不再显示图片（包括已加载出的图片）
        //IX5WebSettingsExtension.PicModel_NetNoPic数据网络下无图（已加载的图片正常显示）
//        webView.getSettingsExtension().setPicModel(IX5WebSettingsExtension.PicModel_NORMAL); //正常加载显示所有图片
//        webView.getSettingsExtension().setAutoRecoredAndRestoreScaleEnabled(true); //缩放比例记录与恢复是基于域名的，首先要开启对应开关功能
//        webView.getSettingsExtension().setDayOrNight(eanble); // enable:true(日间模式)，enable：false（夜间模式）
//        mWebSettings.setSaveFormData(enable); // 是否记录并提示用户填充对应form元素，内核默认是true
//        webView.setBackgroundColor(int color); //在网页未设置背景色的情况下设置网页默认背景色
//        webView.getSettingsExtension().setContentCacheEnable(true); //开启后前进后退将不再重新加载页面，默认关闭，开启方法如下：
//        webView.getSettingsExtension().setForcePinchScaleEnabled(true); //对于无法缩放的页面当用户双指缩放时会提示强制缩放，再次操作将触发缩放功能
//        webView.getSettingsExtension().setShouldTrackVisitedLinks(enable); //开启无痕enable：false，关闭无痕开启enable：true
//        webView.getSettingsExtension().setDisplayCutoutEnable(true); // 对于刘海屏机器如果webview被遮挡会自动padding


        mWebSettings.setSupportMultipleWindows(false); //WebView是否支持多个窗口
        mWebSettings.setGeolocationEnabled(false);    //可以关闭定位功能，内核默认是开启的

        mWebSettings.setAllowFileAccess(true); //允许加载本地文件html  file协议
        mWebSettings.setAllowFileAccessFromFileURLs(false); //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
        mWebSettings.setAllowUniversalAccessFromFileURLs(false); //允许混合加载 允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true); //JS自动弹窗提示 在非用户操作情况下利用window.open打开窗口被称为自动弹窗，该功能默认关闭。
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSettings.setSavePassword(false); //关闭密码保存
        mWebSettings.setSaveFormData(false); //设置WebView是否应该保存表单数据
        mWebSettings.setLoadWithOverviewMode(true); //设置WebView是否以概览模式加载页面，也就是说，缩放内容以适应屏幕的宽度
        mWebSettings.setUseWideViewPort(true); //设置WebView是否应该启用支持“viewport”HTML元标签或应该使用一个宽的viewport
        mWebSettings.setDomStorageEnabled(true); //设置是否启用DOM存储API
        mWebSettings.setNeedInitialFocus(true); //当WebView#requestFocus(int, android.graphics.Rect)被调用时，告诉WebView是否需要设置一个节点来具有焦点
        mWebSettings.setDefaultTextEncodingName("utf-8"); //设置解码html页面时使用的默认文本编码名称
        mWebSettings.setDefaultFontSize(DEFAULT_FONT_SIZE); //设置 WebView 支持的默认字体大小
        mWebSettings.setMinimumFontSize(MINI_MUM_FONT_SIZE); //设置 WebView 支持的最小字体大小，默认为 8
        mWebSettings.setGeolocationEnabled(true); //设置是否启用地理位置。


        String cacheDir = webView.getContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        YWLogUtil.e(TAG, "weview cache dir =" + cacheDir);
        //设置应用程序缓存文件的路径。
        mWebSettings.setDatabasePath(cacheDir);
        mWebSettings.setAppCachePath(cacheDir);
        mWebSettings.setAppCacheMaxSize(APP_CACHE);
        webView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
    }

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            boolean connected = activeNetworkInfo.isConnected();
            return connected;
        } else {
            return false;
        }

    }
}
