package com.example.base.util;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * 方便整个app获取context
 */
public class ContextUtils {
    private static volatile ContextUtils sInstance;

    public static ContextUtils getInstance() {
        if (sInstance == null) {
            synchronized (ContextUtils.class) {
                if (sInstance == null) {
                    sInstance = new ContextUtils();
                }
            }
        }
        return sInstance;
    }

    private Context mContext;

    public void setContext(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    public Context getContext() {
        return mContext;
    }
}
