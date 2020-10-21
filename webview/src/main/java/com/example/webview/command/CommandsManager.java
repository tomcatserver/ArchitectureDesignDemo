package com.example.webview.command;

import android.content.Context;

import com.example.base.util.YWLogUtil;
import com.example.webview.AidlError;

import java.util.HashMap;
import java.util.Map;

public class CommandsManager {
    private static final String TAG = CommandsManager.class.getSimpleName();
    private static volatile CommandsManager sInstance;
    private HashMap<String, ICommand> mCommands;

    public HashMap<String, ICommand> getCommands() {
        return mCommands;
    }

    public CommandsManager() {
        mCommands = new HashMap<>();
    }

    public void registerCommand(ICommand command) {
        mCommands.put(command.name(), command);
    }

    public static CommandsManager getInstance() {
        if (sInstance == null) {
            synchronized (CommandsManager.class) {
                sInstance = new CommandsManager();
            }
        }
        YWLogUtil.d(TAG, sInstance + "");
        return sInstance;
    }

    /**
     * 非UI Command 的执行
     */
    public void execWebViewProcessCommand(Context context, String action, Map params, ResultBack resultBack) {
        if (getCommands().get(action) != null) {
            getCommands().get(action).exec(context, params, resultBack);
        } else {
            AidlError aidlError = new AidlError(WebConstants.ERRORCODE.NO_METHOD, WebConstants.ERRORMESSAGE.NO_METHOD);
            resultBack.onResult(WebConstants.FAILED, action, aidlError);
        }

    }

    /**
     * CommandsManager handled by Webview itself, these command does not require aidl.
     */
    public void execWebviewProcessCommand(Context context, String action, Map params, ResultBack resultBack) {
        if (getCommands().get(action) != null) {
            getCommands().get(action).exec(context, params, resultBack);
        }
    }

    public boolean isWebviewProcessCommand(String action) {
        return getCommands().get(action) != null;
    }

}
