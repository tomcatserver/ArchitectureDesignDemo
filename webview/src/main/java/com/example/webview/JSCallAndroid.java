package com.example.webview;

import android.webkit.JavascriptInterface;

import com.example.base.util.AppExecutors;
import com.example.base.util.YWLogUtil;

/**
 * js调安卓的逻辑，统一在这个处理。
 */
public class JSCallAndroid {
    private static final String TAG = JSCallAndroid.class.getSimpleName();

    //js调用安卓代码
    @JavascriptInterface
    public void jsCallAndroid(final String methodName, final String param) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                YWLogUtil.e(TAG, "methodName=" + methodName + ",param=" + param);
//                dispatchEvent(methodName);
            }
        });
    }

    //js调用安卓代码
    @JavascriptInterface
    public void jumpUrl() {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                YWLogUtil.e(TAG, "jumpUrl----------=");
//                dispatchEvent(methodName);
            }
        });
    }
}
