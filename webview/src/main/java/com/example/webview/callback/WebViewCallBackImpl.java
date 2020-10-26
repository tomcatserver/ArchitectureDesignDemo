package com.example.webview.callback;

import android.net.Uri;
import android.widget.ProgressBar;

import com.example.base.util.YWLogUtil;
import com.example.webview.jsbridge.AndroidCallJSBridge;
import com.example.webview.webviewclient.BaseWebViewClient;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

public class WebViewCallBackImpl implements IWebViewCallBack {
    private ProgressBar mProgressBar;
    private AndroidCallJSBridge mAndroidCallJSBridge;

    public WebViewCallBackImpl(AndroidCallJSBridge callJSBridge) {
        mAndroidCallJSBridge = callJSBridge;
    }

    @Override
    public void pageStarted(String url) {

    }

    @Override
    public void pageFinished(String url) {

    }

    @Override
    public void onReceivedError(BaseWebViewClient client, WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onShowFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {

    }

    @Override
    public void startLoadJs(WebView webView) {
        YWLogUtil.e("tag", "js注入事件开始 －－－－－");
        if (mAndroidCallJSBridge != null) {
            mAndroidCallJSBridge.startLoadJs(webView);
        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    @Override
    public boolean shouldOverrideUrlLoading(String url) {
        return false;
    }

    public void setProgress(ProgressBar progress) {
        mProgressBar = progress;
    }
}
