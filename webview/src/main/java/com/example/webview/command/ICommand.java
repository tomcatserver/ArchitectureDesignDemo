package com.example.webview.command;

import android.content.Context;

import java.util.Map;

public interface ICommand {
    String COMMAND_UPDATE_TITLE = "webview_update_title";
    String COMMAND_UPDATE_TITLE_PARAMS_TITLE = "webview_update_title_params_title";

    String name();

    void exec(Context context, Map params, ResultBack resultBack);
}
