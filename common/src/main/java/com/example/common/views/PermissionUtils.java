package com.example.common.views;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    public static final int READ_FILE_REQUEST_CODE = 100; //读文件需要申请的权限
    public static final int WRITE_FILE_REQUEST_CODE = 101; //写文件需要申请的权限
    private static final String TAG = "PermissionUtils";

    /**
     * 写文件权限申请
     *
     * @param activity
     * @return
     */
    public static boolean requestWritePermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_FILE_REQUEST_CODE);
        } else {
            YWLogUtil.d(TAG, "requestMyPermissions: 有写SD权限");
            return true;
        }
        return false;
    }

    /**
     * 读权限申请
     *
     * @param activity
     * @return
     */
    public static boolean requestReadPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权，编写申请权限代码
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_FILE_REQUEST_CODE);
        } else {
            YWLogUtil.d(TAG, "requestMyPermissions: 有读SD权限");
            return true;
        }
        return false;
    }
}
