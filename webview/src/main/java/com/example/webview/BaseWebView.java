package com.example.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.base.util.AppExecutors;
import com.example.base.util.GsonUtils;
import com.example.base.util.YWLogUtil;
import com.example.webview.webviewclient.BaseWebviewClient;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class BaseWebView extends WebView implements BaseWebviewClient.IWebviewTouch {
    public static final String CONTENT_SCHEME = "file:///android_asset/";
    private static final String ADD_JS = "webview_wj";
    private static final String TAG = BaseWebView.class.getSimpleName();
    private Context mContext;
    private WebViewCallBack mWebViewCallBack;
    private Map<String, String> mHeaders;
    private boolean mTouchByUser;


    public BaseWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @SuppressLint("JavascriptInterface")
    private void init(Context context) {
        WebViewSetting.getInstance().toSetting(this);
        addJavascriptInterface(this, ADD_JS);
//        CommandDispatcher.getInstance().initAidlConnect(getContext());


    }

    public WebViewCallBack getWebViewCallBack() {
        return mWebViewCallBack;
    }

    public void registerdWebViewCallBack(WebViewCallBack webViewCallBack) {
        mWebViewCallBack = webViewCallBack;
        setWebViewClient(new BaseWebviewClient(this, webViewCallBack, mHeaders, this));
    }

    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
    }

    //js调用安卓代码
    @JavascriptInterface
    public void jsCallAndroid(final String cmd, final String param) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                YWLogUtil.e(TAG, "cmd=" + cmd + ",param=" + param);
            }
        });
    }

    @Override
    public void loadUrl(@NonNull String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mHeaders == null) {
            super.loadUrl(url);
        } else {
            super.loadUrl(url, mHeaders);
        }
        resetAllStateInternal(url);
    }

    private void resetAllStateInternal(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return;
        }
        resetAllState();
    }

    public void dispatchEvent(String name) {
        Map<String, String> param = new HashMap<>(1);
        param.put("name", name);
        loadJS("dj.dispatchEvent", param);
    }

    //安卓调用js代码
    public void loadJS(String cmd, Object param) {
        String trigger = "javascript:" + cmd + "(" + GsonUtils.toJson(param) + ")";
        evaluateJavascript(trigger, null);
    }

    // 加载url时重置touch状态
    protected void resetAllState() {
        mTouchByUser = false;
    }

    @Override
    public boolean isTouchByUser() {
        return mTouchByUser;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchByUser = true;
                break;
        }
        return super.onTouchEvent(event);
    }

    protected boolean onBackHandle() {
        if (canGoBack()) {
            goBack();
            return true;
        } else {
            return false;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return onBackHandle();
        }
        return false;
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url WebView要加载的url
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    public static boolean syncCookie(String url, Map<String, String> map) {
        CookieManager cookieManager = CookieManager.getInstance();
        for (String key : map.keySet()) {
            cookieManager.setCookie(url, key + "=" + map.get(key));
        }
        String newCookie = cookieManager.getCookie(url);
        return !TextUtils.isEmpty(newCookie);
    }
}
