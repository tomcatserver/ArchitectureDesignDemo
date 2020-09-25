package com.example.common.views;

import android.util.Log;

/**
 * Log 相关工具类
 */
public class YWLogUtil {
    /* 可以用于 release 时，统一关闭 Log */
    public static boolean sLogEnabled = true;

    /**
     * 打印所有的信息
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (sLogEnabled) {
            Log.v(tag, msg);
        }
    }

    /**
     * 打印 info 信息
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (sLogEnabled) {
            Log.i(tag, msg);
        }
    }

    /**
     * 打印 debug 信息
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (sLogEnabled) {
            Log.d(tag, msg);
        }
    }

    /**
     * 打印警告信息
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (sLogEnabled) {
            Log.w(tag, msg);
        }
    }

    /**
     * 打印错误信息
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (sLogEnabled) {
            Log.e(tag, msg);
        }
    }
}
