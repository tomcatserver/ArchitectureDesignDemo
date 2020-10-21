package com.example.webview;

import android.content.Context;
import android.webkit.WebView;

import com.example.base.util.AppExecutors;
import com.example.base.util.GsonUtils;
import com.example.base.util.YWLogUtil;
import com.example.webview.command.CommandsManager;
import com.example.webview.command.ResultBack;

import java.util.Map;

public class CommandDispatcher {
    private static final String TAG = CommandDispatcher.class.getSimpleName();
    private static volatile CommandDispatcher sInstance;
    protected IWebToUI mWebAid;

    public static CommandDispatcher getInstance() {
        if (sInstance == null) {
            synchronized (CommandDispatcher.class) {
                if (sInstance == null) {
                    sInstance = new CommandDispatcher();
                }
            }
        }
        return sInstance;
    }

    public void initAidlConnect(final Context context) {
        if (mWebAid == null) {
            return;
        }
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
//                IWebAid.Stub.asInterface(MainProcessConnector.getInstance(context).getIWebAidlInterface());
            }
        });
    }

    public void exec(Context context, String cmd, String params, final WebView webView) {
        YWLogUtil.e(TAG, "command=" + cmd + ",params=" + params);
        try {
            if (CommandsManager.getInstance().isWebviewProcessCommand(cmd)) {
                Map map = GsonUtils.fromLocalJson(params, Map.class);
                CommandsManager.getInstance().execWebViewProcessCommand(context, cmd, map, new ResultBack() {
                    @Override
                    public void onResult(int status, String action, Object result) {

                    }
                });
            } else {

            }

        } catch (Exception e) {
            YWLogUtil.e(TAG, "command exec error ....");
        }
    }
}
