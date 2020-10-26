package com.example.webview.util;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.GeolocationPermissions;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebIconDatabase;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个处理webview初始化、cookie同步、删除缓存清除等。
 */
public class WebViewUtil {
    public static void initWebView(Context context) {
        initTbsSettings(); //设置开启优化方案
//        initUserStrategy(context);
    }

    private static void initTbsSettings() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
    }

    //    private static void initUserStrategy(final Context context) {
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
//        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
//            public Map<String, String> onCrashHandleStart(
//                    int crashType,
//                    String errorType,
//                    String errorMessage,
//                    String errorStack) {
//                //当app crash时为了能定位内核问题需要将内核版本号和内核辅助信息一起上报，获取内核版本号和辅助信息的接口如下：
//                //返回内核版本号
//                int version = QbSdk.getTbsVersion(context);
//                //返回内核辅助信息
//                String message = WebView.getCrashExtraMessage(context);
//                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//                String x5CrashInfo = com.tencent.smtt.sdk.WebView.getCrashExtraMessage(context);
//                x5CrashInfo += String.format("&version=%d&message=%s", version, message);
//                map.put("x5crashInfo", x5CrashInfo);
//                return map;
//            }
//
//            @Override
//            public byte[] onCrashHandleStart2GetExtraDatas(
//                    int crashType,
//                    String errorType,
//                    String errorMessage,
//                    String errorStack) {
//                try {
//                    return "Extra data.".getBytes(StandardCharsets.UTF_8);
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        });
////        APPID
//        CrashReport.initCrashReport(context, null, BuildConfig.DEBUG, strategy);
//    }

    /**
     * 缓存清除
     */
    public static void clearData(Context context) {
        if (context == null) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        //清除cookie
        CookieManager.getInstance().removeAllCookies(null);
        //清除storage相关缓存
        WebStorage.getInstance().deleteAllData();
        //清除用户密码信息
        WebViewDatabase.getInstance(applicationContext).clearUsernamePassword();
        //清除httpauth信息
        WebViewDatabase.getInstance(applicationContext).clearHttpAuthUsernamePassword();
        //清除表单数据
        WebViewDatabase.getInstance(applicationContext).clearFormData();
        //清除页面icon图标信息
        WebIconDatabase.getInstance().removeAllIcons();
        //删除地理位置授权，也可以删除某个域名的授权（参考接口类）
        GeolocationPermissions.getInstance().clearAll();

        //一次性删除所有缓存
        QbSdk.clearAllWebViewCache(applicationContext, true);
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
            cookieManager.setAcceptCookie(true);
            cookieManager.flush();
        }
        String newCookie = cookieManager.getCookie(url);
        return !TextUtils.isEmpty(newCookie);
    }

    public static void clearWebView(WebView webView) {
        if (webView == null) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        clearData(webView.getContext());
        webView.stopLoading();
        if (webView.getHandler() != null) {
            webView.getHandler().removeCallbacksAndMessages(null);
        }
        webView.removeAllViews();
        ViewGroup mViewGroup = null;
        if ((mViewGroup = ((ViewGroup) webView.getParent())) != null) {
            mViewGroup.removeView(webView);
        }
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.setTag(null);
        webView.clearHistory();
        webView.destroy();
        webView = null;
    }
}
