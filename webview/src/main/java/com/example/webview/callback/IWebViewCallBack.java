package com.example.webview.callback;

import android.net.Uri;
import android.widget.ProgressBar;

import com.example.webview.webviewclient.BaseWebViewClient;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import java.io.Serializable;

/**
 * WebView回调统一处理
 * 所有涉及到WebView交互的都必须实现这个callback
 */
public interface IWebViewCallBack extends Serializable {

    void pageStarted(String url);

    void pageFinished(String url);

    void onReceivedError(BaseWebViewClient client, WebView view, int errorCode, String description, String failingUrl);

    void onShowFileChooser(ValueCallback<Uri[]> filePathCallback,
                           WebChromeClient.FileChooserParams fileChooserParams);

    void startLoadJs(WebView webView); //进行js注入，刚打开h5页面的时候可以在这个回调方法进行js注入。否则有可能注入不成功。

    ProgressBar getProgressBar(); //设置进度条

    boolean shouldOverrideUrlLoading(String url);
}
