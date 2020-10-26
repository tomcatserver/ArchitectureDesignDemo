package com.example.webview.jsbridge;

import android.webkit.JavascriptInterface;

import com.example.base.util.AppExecutors;
import com.example.base.util.YWLogUtil;
import com.example.webview.view.CommonWebView;
import com.tencent.smtt.sdk.ValueCallback;

/**
 * js调安卓的逻辑，统一在这个处理。
 */
public class JSCallAndroidBridge {
    private static final String TAG = JSCallAndroidBridge.class.getSimpleName();
    private CommonWebView mBaseWebView;

    public JSCallAndroidBridge(CommonWebView baseWebView) {
        mBaseWebView = baseWebView;
    }

    //js调用安卓代码
    @JavascriptInterface
    public void jsCallAndroid(final String methodName, final String param) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                YWLogUtil.e(TAG, "methodName=" + methodName + ",param=" + param);
            }
        });
    }


    //安卓调用js代码
    private void loadJS(String methodName, Object param, ValueCallback<String> valueCallback) {
        if (mBaseWebView != null) {
            mBaseWebView.loadJS(methodName, param, valueCallback);
        }
    }

    public void clearData() {
        if (mBaseWebView != null) {
            mBaseWebView = null;
        }
    }
}
