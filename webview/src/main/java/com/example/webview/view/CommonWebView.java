package com.example.webview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.base.util.GsonUtils;
import com.example.base.util.YWLogUtil;
import com.example.webview.state.PageScrollState;
import com.example.webview.callback.IWebViewCallBack;
import com.example.webview.setting.WebViewSetting;
import com.example.webview.jsbridge.JSCallAndroidBridge;
import com.example.webview.webviewclient.BaseWebChomeClient;
import com.example.webview.webviewclient.BaseWebViewClient;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.extension.proxy.ProxyWebChromeClientExtension;
import com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension;
import com.tencent.smtt.export.external.interfaces.IX5WebViewBase;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import java.util.Map;

/**
 * 通用WebView控件
 */
public class CommonWebView extends WebView implements BaseWebViewClient.IWebviewTouch {
    public static final String CONTENT_SCHEME = "file:///android_asset/";
    private static final String ADD_JS = "h5_android";
    private static final String TAG = CommonWebView.class.getSimpleName();
    private IWebViewCallBack mWebViewCallBack;
    private Map<String, String> mHeaders;
    private boolean mTouchByUser;


    public CommonWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CommonWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WebViewSetting.getInstance().toSetting(this);
        JSCallAndroidBridge jsObject = new JSCallAndroidBridge(this);
//        js注入对象
        addJavascriptInterface(jsObject, ADD_JS);
    }

    //安卓调用js代码 js执行
    public void loadJS(String methodName, Object param, ValueCallback<String> valueCallback) {
        String js = String.format("javascript:" + methodName + "('%s')", GsonUtils.toJson(param));
        YWLogUtil.e(TAG, "loadJS----=" + js);
        evaluateJavascript(js, valueCallback);
    }

    public void removeJavascriptInterface() {
        removeJavascriptInterface(ADD_JS);
    }


    public IWebViewCallBack getWebViewCallBack() {
        return mWebViewCallBack;
    }

    public void registerdWebViewCallBack(IWebViewCallBack webViewCallBack) {
        mWebViewCallBack = webViewCallBack;
        setWebViewClient(new BaseWebViewClient(this, webViewCallBack, mHeaders, this));
        setWebChromeClient(new BaseWebChomeClient(webViewCallBack));
    }


    public void setHeaders(Map<String, String> mHeaders) {
        this.mHeaders = mHeaders;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return onBackHandle();
        }
        return false;
    }

    /**
     * 截屏，截取webview可视区域
     * bitmap 绘制用的bitmap
     * drawCursor 是否画光标---保留暂未使用
     * drawScrollbar是否截取滚动条---保留暂未使用.
     * drawTitleBar是否截取标题栏---保留暂未使用
     * drawWithBuffer是否使用buffer---保留暂未使用
     * scaleX----x方向缩放比例
     * scaleY----y方向缩放比例
     * callback截图完成后的回调，如果设置为null将为同步调用，否则是异步调用
     */
    public void snapshotVisible(Bitmap bitmap,
                                boolean drawCursor,
                                boolean drawScrollbars,
                                boolean drawTitleBar,
                                boolean drawWithBuffer,
                                float scaleX,
                                float scaleY,
                                Runnable callback) {
        IX5WebViewExtension x5WebViewExtension = getX5WebViewExtension();
        if (x5WebViewExtension != null) {
            x5WebViewExtension.snapshotVisible(bitmap, drawCursor, drawScrollbars, drawTitleBar, drawWithBuffer, scaleX, scaleY, callback);
        }
    }

    /**
     * 注：以上Canvas需要使用bitmap类型的Canvas
     * <p>
     * 截整页，截取整个webview
     * 绘制canvas
     * drawScrollbar是否截取滚动条---保留暂未使用
     * drawTitleBar是否截取标题栏---保留暂未使用
     */
    public void snapshotWholePage(Canvas canvas,
                                  boolean drawScrollbars,
                                  boolean drawTitleBar) {
        IX5WebViewExtension x5WebViewExtension = getX5WebViewExtension();
        if (x5WebViewExtension != null) {
            x5WebViewExtension.snapshotWholePage(canvas, drawScrollbars, drawTitleBar);
        }
    }

    public void setWebChromeClientExtension() {
        setWebChromeClientExtension(new ProxyWebChromeClientExtension() {
            @Override
            public void openFileChooser(android.webkit.ValueCallback<Uri[]> uploadFile,
                                        String acceptType,
                                        String captureType) {
                super.openFileChooser(uploadFile, acceptType, captureType);
                //保存对应的valuecallback供选择后使用
                //通过startActivityForResult启动文件选择窗口或自定义文件选择
            }

            /**前进后退结束回调
             *页面前进后退切换完成事件通知，目前step无实际赋值，此接口只是一个完成通知
             */
            @Override
            public void onBackforwardFinished(int step) {
                super.onBackforwardFinished(step);
            }

            /* 密码保存未关闭时，内核默认有密码保存提升框，如需要重写密码提示可以按照如下接口进行处理：
             * callback：处理后的回调；
             * schemePlusHost：域名；
             * username：用户名；
             * password：密码；
             * nameElement：用户名输入框名称；
             * passwordElement：密码输入框名称；
             * isReplace：是否是替换操作
             */
            @Override
            public boolean onSavePassword(android.webkit.ValueCallback<String> valueCallback, String schemePlusHost, String username, String password, String nameElement, String passwordElement, boolean isReplace) {
                //这里可以弹窗提示用户
                //这里调用将会保存用户名和密码，如果只保存用户名可以将密码置为null，如果两者均不存在则不需要调用该接口
                getX5WebViewExtension().sendRememberMsg(schemePlusHost, username, password, nameElement, passwordElement);
                //处理完后需要回调该接口，执行了保存操作参数为true，否则为false
                valueCallback.onReceiveValue("true");
                //这里要返回true，否则内核会提示用户保存密码
                return true;
//                return super.onSavePassword(callback, schemePlusHost, username, password, nameElement, passwordElement, isReplace);
            }
        });
    }

    public void setWebViewClientExtension() {
        //设置自动弹窗被拦截时提醒（回调接口）
//        getSettingsExtension().setJavaScriptOpenWindowsBlockedNotifyEnabled(true);

        /**
         * 设置滚动条样式
         */
//竖直快速滑块，设置null可去除
//        getX5WebViewExtension().setVerticalTrackDrawable(Drawable drawable)
//判断水平滚动条是否启动
//        getX5WebViewExtension().isHorizontalScrollBarEnabled()
//启用或禁用水平滚动条
//        getX5WebViewExtension().setHorizontalScrollBarEnabled( boolean enabled)
//判断竖直滚动条是否启动
//        getX5WebViewExtension().isVerticalScrollBarEnabled()
//启用或禁用竖直滚动条
//        getX5WebViewExtension().setVerticalScrollBarEnabled( boolean enabled)
//设置滚动条渐隐消失的时间段
//        getX5WebViewExtension().setScrollBarFadeDuration( int duration)
//设置滚动条多久开始渐隐消失
//        getX5WebViewExtension().setScrollBarDefaultDelayBeforeFade( int delay)
        //回调的接口需要如下设置：
        getX5WebViewExtension().setWebViewClientExtension(new ProxyWebViewClientExtension() {
            /**
             * 当js在非用户操作下打开新页面被内核拦截且宿主也不允许当前页面自动打开时，对应页面会被内核阻止
             * 如果在IX5WebSettingsExtension设置了setJavaScriptOpenWindowsBlockedNotifyEnabled为true时
             * 此时会回调宿主当前拦截的所有页面是否允许被打开
             * 如果callback回调true，后续对应页面的弹窗再被拦截时则会直接按照此授权处理,不再回调该接口通知宿主
             * 如果callback回调false，页面下次弹出窗口被拦截仍会通知宿主，但此时hadAllowShow的值为true
             * @param host 页面域名
             * @param blockedUrlList 被阻止打开的页面url列表
             * @param valueCallback true:打开拦截页面且后续不再拦截，false:打开宿主页面后续继续拦截,如果已经做过该操作,则后续回调接口中hadAllowShow为true
             * @param hadAllowShow 是否允许展示过该host的弹出窗口，当曾经设置过callback<false>时该值为true,否则为false
             * @return 宿主处理了该接口返回true，否则返回false
             */
            @Override
            public boolean notifyJavaScriptOpenWindowsBlocked(String host, String[] blockedUrlList, android.webkit.ValueCallback<Boolean> valueCallback, boolean hadAllowShow) {
                //在此可以弹框提示用户
                //只允许当前一次调用callback.onReceiveValue(false)
                //如果后续都允许调用callback.onReceiveValue(true)
                return true;
//                return super.notifyJavaScriptOpenWindowsBlocked(host, blockedUrlList, callback, hadAllowShow);
            }

            //当每次首次记录一个域名的缩放比例时会回调这个接口
            @Override
            public void onPromptScaleSaved() {
                super.onPromptScaleSaved();
            }
        });
    }

    /**
     * 单步后退
     */
    public void goBacks() {
        if (canGoBack()) {
            goBack();
        }
    }

    /**
     * 单步前进
     */
    public void goForwards() {
        if (canGoForward()) {
            goForward();
        }
    }

    /**
     * 前进后退统一接口及设置跨度
     *
     * @param index -1表示后退，1表示前进
     */
    public void goBackOrForwards(int index) {
        goBackOrForward(index);
    }

    /**
     * 查找字符串
     *
     * @param key 关键字
     */
    public void findContent(String key) {
        findAllAsync(key);
    }

    /**
     * 查找上一项、下一项：
     *
     * @param forward 为true时表示下一项，为false时表示上一项
     */
    public void findNexts(boolean forward) {
        findNext(forward);
    }

    /**
     * 清除查找项
     */
    public void clearFind() {
        clearMatches();
    }

    /**
     * 如果需要提示用户当前查找的内容有几个以及获取当前选中了哪个，可以用如下回调
     */
    public void setFindListener() {
        setFindListener(new IX5WebViewBase.FindListener() {
            @Override
            public void onFindResultReceived(int i, int i1, boolean b) {

            }
        });
    }

    /**
     * 页面滚动
     *
     * @param state 页面滚动方向的状态 DOWN表示向下滚动，UP表示向上滚动
     */
    public void pageScroll(PageScrollState state) {
        if (PageScrollState.DOWN == state) {
            pageDown(true);
        } else if (PageScrollState.UP == state) {
            pageUp(true);
        }
    }

    /**
     * 保存网页
     *
     * @param filename
     */
    public void saveWebArchives(String filename) {
        saveWebArchive(filename);
    }

    public void saveWebArchives(String basename, boolean autoname, ValueCallback<String> callback) {
        saveWebArchive(basename, autoname, callback);
    }

    /**
     * 滚动：
     *
     * @param x
     * @param y
     */
    public void scrollTos(int x, int y) {
        getView().scrollTo(x, y);
    }

}
