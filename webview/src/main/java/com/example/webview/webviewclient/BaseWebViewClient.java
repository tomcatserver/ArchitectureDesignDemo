package com.example.webview.webviewclient;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;


import com.example.base.util.YWLogUtil;
import com.example.webview.callback.IWebViewCallBack;
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Map;

import static com.example.webview.view.CommonWebView.CONTENT_SCHEME;


public class BaseWebViewClient extends WebViewClient {

    private static final String TAG = "XXWebviewCallBack";
    public static final String SCHEME_SMS = "sms:";
    private IWebViewCallBack mWebViewCallBack;
    private WebView mWebView;
    boolean mReady;
    private Map<String, String> mHeaders;
    private IWebviewTouch mWebviewTouch;
    public BaseWebViewClient(WebView webView, IWebViewCallBack webViewCallBack, Map<String, String> headers, IWebviewTouch touch) {
        this.mWebViewCallBack = webViewCallBack;
        this.mWebView = webView;
        this.mHeaders = headers;
        this.mWebviewTouch = touch;
    }

    public boolean ismReady() {
        return this.mReady;
    }

    public interface IWebviewTouch {
        boolean isTouchByUser();
    }

    /**
     * url重定向会执行此方法以及点击页面某些链接也会执行此方法
     *
     * @return true:表示当前url已经加载完成，即使url还会重定向都不会再进行加载 false 表示此url默认由系统处理，该重定向还是重定向，直到加载完成
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        YWLogUtil.e(TAG, "shouldOverrideUrlLoading url: " + url);
        // 当前链接的重定向, 通过是否发生过点击行为来判断
        if (!mWebviewTouch.isTouchByUser()) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        // 如果链接跟当前链接一样，表示刷新
        if (mWebView.getUrl().equals(url)) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        if (handleLinked(url)) {
            return true;
        }
        // 控制页面中点开新的链接在当前webView中打开
        view.loadUrl(url, mHeaders);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        YWLogUtil.e(TAG, "shouldOverrideUrlLoading url: " + request.getUrl());
        // 当前链接的重定向
        if (!mWebviewTouch.isTouchByUser()) {
            return super.shouldOverrideUrlLoading(view, request);
        }
        // 如果链接跟当前链接一样，表示刷新
        if (mWebView.getUrl().equals(request.getUrl().toString())) {
            return super.shouldOverrideUrlLoading(view, request);
        }
        if (handleLinked(request.getUrl().toString())) {
            return true;
        }
        // 控制页面中点开新的链接在当前webView中打开
        view.loadUrl(request.getUrl().toString(), mHeaders);
        return true;
    }

    /**
     * 支持电话、短信、邮件、地图跳转，跳转的都是手机系统自带的应用
     */
    private boolean handleLinked(String url) {
        if (url.startsWith(WebView.SCHEME_TEL)
                || url.startsWith(SCHEME_SMS)
                || url.startsWith(WebView.SCHEME_MAILTO)
                || url.startsWith(WebView.SCHEME_GEO)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mWebView.getContext().startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
                ignored.printStackTrace();
            }
            return true;
        }
        if (mWebViewCallBack != null) {
            return mWebViewCallBack.shouldOverrideUrlLoading(url);
        }
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        YWLogUtil.e(TAG, "onPageFinished url:" + url);
        if (!TextUtils.isEmpty(url) && url.startsWith(CONTENT_SCHEME)) {
            mReady = true;
        }
        if (mWebViewCallBack != null) {
            mWebViewCallBack.pageFinished(url);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        YWLogUtil.e(TAG, "onPageStarted url: " + url);
        if (mWebViewCallBack != null) {
            mWebViewCallBack.pageStarted(url);
        }
    }

    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return shouldInterceptRequest(view, request.getUrl().toString());
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return null;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        YWLogUtil.e(TAG, "webview" + " error" + errorCode + " + " + description);
        if (mWebViewCallBack != null) {
            mWebViewCallBack.onReceivedError(this, view, errorCode, description, failingUrl);
        }
    }

    @Override
    public void onReceivedSslError(WebView webView, final SslErrorHandler handler, com.tencent.smtt.export.external.interfaces.SslError error) {
        handler.proceed();
    }

    /**
     * HttpAuth密码记录及使用 当访问页面需要输入用户名和密码时会回调对应client接口.
     *
     * @param webView
     * @param handler
     * @param host
     * @param realm
     */
    @Override
    public void onReceivedHttpAuthRequest(WebView webView,
                                          HttpAuthHandler handler,
                                          String host,
                                          String realm) {
        super.onReceivedHttpAuthRequest(webView, handler, host, realm);
        //首先判断是否可以重复使用对应用户名和密码如果可以则获取已保存的密码（获取成功后直接使用）
        //如果不允许重复使用用户名和密码或者未保存用户名和密码则需要提示用户输入
        //用户输入用户名和密码后可以将对应数据保存
        //获取密码接口
//        mWebView.getHttpAuthUsernamePassword(host, realm);
//保存密码接口
//        mWebView.setHttpAuthUsernamePassword( host,  realm, String username, String password)
    }

    @Override
    public void onFormResubmission(WebView view, Message dontResend, Message resend) {
        //解决有表单form提交的时候，webview在调用goBack()方法时，不起作用
        resend.sendToTarget();
    }
}