package com.example.webview.jsbridge;

import com.example.webview.view.CommonWebView;
import com.tencent.smtt.sdk.WebView;

import java.util.HashMap;
import java.util.Map;

/**
 * 安卓调js的逻辑，统一在这个处理。
 */
public class AndroidCallJSBridge {
    private CommonWebView mWebView;

    public AndroidCallJSBridge() {

    }

    //安卓调用js代码
    public void dispatchEvent(String name) {
        Map<String, String> param = new HashMap<>(1);
        param.put("name", name);
        param.put("sex", "nv");
        param.put("age", String.valueOf(60));
        param.put("time", String.valueOf(System.currentTimeMillis()));
        param.put("content", "android call js");
        if (mWebView != null) {
            mWebView.loadJS("dispatchEvent", param, null);
        }
    }

    public void clearData() {
        if (mWebView != null) {
            mWebView = null;
        }
    }

    /**
     * 打开h5页面后,要等这个方法执行了，注入js代码才能生效。只要有相关的需求，都写到这个方法里面。
     */
    public void startLoadJs(WebView webView) {
        mWebView = (CommonWebView) webView;
//        dispatchEvent("sdkdkdk");
    }
}
