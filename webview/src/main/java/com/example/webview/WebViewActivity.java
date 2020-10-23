package com.example.webview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.base.activity.BaseActivity;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;

import java.io.Serializable;

/**
 * 统一webview入口封装。
 */
public class WebViewActivity extends BaseActivity {
    private static final String JUMP_URL = "jump_url_h5";
    private static final String CALL_JS_MANAGER = "call_js_manager";
    private static final String LOCATION_STATUS = "location_status";
    protected BaseWebView mWebView;
    protected ProgressBar mPbProgress;
    private String mJumpUrl;
    private AndroidCallJSManager mAndroidCallJS;
    private LocationStatus mLocationStatus;


    public static void startCommonWeb(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(JUMP_URL, url);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startCommonWeb(Context context, String url, LocationStatus locationStatus) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(JUMP_URL, url);
        intent.putExtra(LOCATION_STATUS, locationStatus);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startCommonWeb(Context context, String url, AndroidCallJSManager manager) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(JUMP_URL, url);
        intent.putExtra(CALL_JS_MANAGER, manager);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startCommonWeb(Context context, String url, AndroidCallJSManager manager, LocationStatus locationStatus) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(JUMP_URL, url);
        intent.putExtra(CALL_JS_MANAGER, manager);
        intent.putExtra(LOCATION_STATUS, locationStatus);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 根据需求 是否需要和h5进行交互。比较进度条、js注入、加载之前、加载之后、h5页面加载失败等。
     *
     * @return 返回WebView统一回调
     */
    private WebViewCallBack getWebViewCallBack() {
        return new WebViewCallBack() {
            @Override
            public void pageStarted(String url) {

            }

            @Override
            public void pageFinished(String url) {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onShowFileChooser(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {

            }

            //打开页面 并且有想注入js的需求可以在这里注入。
            @Override
            public void loadJs() {
//                if (mAndroidCallJS != null) {
//                    mAndroidCallJS.dispatchEvent("zs");
//                }
            }

            @Override
            public ProgressBar getProgressBar() {
                return mPbProgress;
            }
        };
    }

    @Override
    protected void initData(Bundle bundle) {
        Intent intent = getIntent();
        if (intent != null) {
            mJumpUrl = intent.getStringExtra(JUMP_URL);
            Serializable serializableExtra = intent.getSerializableExtra(LOCATION_STATUS);
            Serializable serializable = intent.getSerializableExtra(CALL_JS_MANAGER);
            if (serializable instanceof AndroidCallJSManager) {
                mAndroidCallJS = (AndroidCallJSManager) serializable;
            }
            if (serializableExtra instanceof LocationStatus) {
                mLocationStatus = (LocationStatus) serializableExtra;
            }
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mWebView = findViewById(R.id.webview);
        mPbProgress = findViewById(R.id.pb_progress);
        mWebView.loadUrl(mJumpUrl);
        mWebView.registerdWebViewCallBack(getWebViewCallBack());
        if (mAndroidCallJS != null) {
            mAndroidCallJS.setWebview(mWebView);
        }
        if (mLocationStatus != null) {
            findViewById(R.id.rl_root_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            DisplayMetrics outMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
            int widthPixel = outMetrics.widthPixels;
            int heightPixel = outMetrics.heightPixels;
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mWebView.getLayoutParams();
            layoutParams.height = heightPixel / 2;
            layoutParams.width = widthPixel;
            if (mLocationStatus == LocationStatus.Bottom) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM);
            } else if (mLocationStatus == LocationStatus.Center) {
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            }
            mWebView.setLayoutParams(layoutParams);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewUtil.clearWebView(mWebView);
        if (mAndroidCallJS != null) {
            mAndroidCallJS.clearData();
        }
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_webview;
    }
}
