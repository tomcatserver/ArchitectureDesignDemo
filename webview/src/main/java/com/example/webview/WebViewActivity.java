package com.example.webview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.webview.command.WebConstants;

import static com.example.webview.command.WebConstants.JUMP_URL;

public class WebViewActivity extends AppCompatActivity {
    private BaseWebView mWebView;

    public static void startCommonWeb(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(JUMP_URL, url);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = findViewById(R.id.webview);
        Intent intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra(JUMP_URL);
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearWebView(mWebView);
    }

    private void clearWebView(WebView m) {
        if (m == null) {
            return;
        }
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return;
        }
        m.stopLoading();
        if (m.getHandler() != null) {
            m.getHandler().removeCallbacksAndMessages(null);
        }
        m.removeAllViews();
        ViewGroup mViewGroup = null;
        if ((mViewGroup = ((ViewGroup) m.getParent())) != null) {
            mViewGroup.removeView(m);
        }
        m.setWebChromeClient(null);
        m.setWebViewClient(null);
        m.setTag(null);
        m.clearHistory();
        m.destroy();
        m = null;
    }
}
