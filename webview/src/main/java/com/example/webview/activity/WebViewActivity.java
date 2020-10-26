package com.example.webview.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.webview.state.LocationState;
import com.example.webview.R;
import com.example.webview.util.WebViewUtil;
import com.example.webview.callback.WebViewCallBackImpl;
import com.example.webview.view.CommonWebView;
import com.example.webview.jsbridge.AndroidCallJSBridge;

import java.io.Serializable;

/**
 * 统一WebView入口封装 支持全屏h5、半屏h5(底部、中间)。
 */
public class WebViewActivity extends Activity {
    private static final String JUMP_URL = "jump_url_h5";
    private static final String LOCATION_STATUS = "location_status";
    private CommonWebView mWebView;
    private RelativeLayout mRootViewLayout;
    private ProgressBar mProgressBar;
    private String mJumpUrl;
    private AndroidCallJSBridge mAndroidCallJS;
    private LocationState mLocationStatus;
    private WebViewCallBackImpl mWebViewCallBack;

    public static void startCommonWeb(Context context, String url) {
        if (!(context instanceof Activity)) {
            return;
        }
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(JUMP_URL, url);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ((Activity) context).overridePendingTransition(0, 0);
        context.startActivity(intent);
    }

    public static void startCommonWeb(Context context, String url, LocationState locationStatus) {
        if (!(context instanceof Activity)) {
            return;
        }
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(JUMP_URL, url);
        intent.putExtra(LOCATION_STATUS, locationStatus);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ((Activity) context).overridePendingTransition(0, 0);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            mJumpUrl = intent.getStringExtra(JUMP_URL);
            Serializable locationStatus = intent.getSerializableExtra(LOCATION_STATUS);
            if (locationStatus instanceof LocationState) {
                mLocationStatus = (LocationState) locationStatus;
            }
            mAndroidCallJS = new AndroidCallJSBridge();
            mWebViewCallBack = new WebViewCallBackImpl(mAndroidCallJS);
        }
    }

    private void initView() {
        mWebView = findViewById(R.id.webview);
        mRootViewLayout = findViewById(R.id.rl_root_view);
        mProgressBar = findViewById(R.id.pb_progress);
        if (mWebViewCallBack != null) {
            mWebViewCallBack.setProgress(mProgressBar);
            mWebView.registerdWebViewCallBack(mWebViewCallBack);
        }
        if (mLocationStatus != null) {
            mRootViewLayout.setOnClickListener(new View.OnClickListener() {
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
            if (mLocationStatus == LocationState.Bottom) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM);
            } else if (mLocationStatus == LocationState.Center) {
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            }
            mWebView.setLayoutParams(layoutParams);
        }
        mWebView.loadUrl(mJumpUrl);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewUtil.clearWebView(mWebView);
        if (mAndroidCallJS != null) {
            mAndroidCallJS.clearData();
        }
    }
}
