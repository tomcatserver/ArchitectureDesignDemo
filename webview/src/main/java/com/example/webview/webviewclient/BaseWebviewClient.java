package com.example.webview.webviewclient;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;


import com.example.base.util.YWLogUtil;
import com.example.webview.R;
import com.example.webview.WebViewCallBack;

import java.util.Map;

import static com.example.webview.BaseWebView.CONTENT_SCHEME;


public class BaseWebviewClient extends WebViewClient {

    private static final String TAG = "XXWebviewCallBack";
    public static final String SCHEME_SMS = "sms:";
    private WebViewCallBack mWebViewCallBack;
    private WebView mWebView;
    boolean mReady;
    private Map<String, String> mHeaders;
    private IWebviewTouch mWebviewTouch;

    public BaseWebviewClient(WebView webView, WebViewCallBack webViewCallBack, Map<String, String> headers, IWebviewTouch touch) {
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
        YWLogUtil.e(TAG, "webview error" + errorCode + " + " + description);
        if (mWebViewCallBack != null) {
            mWebViewCallBack.onError();
        }
    }

    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        String channel = "";
        if (!TextUtils.isEmpty(channel) && channel.equals("play.google.com")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mWebView.getContext());
            String message = mWebView.getContext().getString(R.string.ssl_error);
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = mWebView.getContext().getString(R.string.ssl_error_not_trust);
                    break;
                case SslError.SSL_EXPIRED:
                    message = mWebView.getContext().getString(R.string.ssl_error_expired);
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = mWebView.getContext().getString(R.string.ssl_error_mismatch);
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = mWebView.getContext().getString(R.string.ssl_error_not_valid);
                    break;
            }
            message += mWebView.getContext().getString(R.string.ssl_error_continue_open);

            builder.setTitle(R.string.ssl_error);
            builder.setMessage(message);
            builder.setPositiveButton(R.string.continue_open, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            handler.proceed();
        }
    }
}