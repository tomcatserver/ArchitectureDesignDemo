package com.example.webview;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 安卓调js的逻辑，统一在这个处理。
 */
public class AndroidCallJSManager implements Serializable {
    private BaseWebView mBaseWebView;

    public void dispatchEvent(String name) {
        Map<String, String> param = new HashMap<>(1);
        param.put("name", name);
        param.put("sex", "nv");
        param.put("age", String.valueOf(60));
        param.put("time", String.valueOf(System.currentTimeMillis()));
        param.put("content", "android call js");
        if (mBaseWebView != null) {
            mBaseWebView.loadJS("dispatchEvent", param, null);
        }
    }

    public void setWebview(BaseWebView webView) {
        mBaseWebView = webView;
    }

    public void clearData() {
        if (mBaseWebView != null) {
            mBaseWebView = null;
        }
    }
}
