package com.example.webview;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.dispatchEvent("zs");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebViewUtil.clearWebView(mWebView);
    }

}
