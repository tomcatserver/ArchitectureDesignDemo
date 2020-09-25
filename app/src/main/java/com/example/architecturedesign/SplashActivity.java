package com.example.architecturedesign;

import android.os.Bundle;


import androidx.annotation.NonNull;

import com.example.base.BaseActivity;
import com.example.common.views.PermissionUtils;
import com.example.router.RouteServiceManager;

import static com.example.router.Router.HOME_PAGE_PATH;

public class SplashActivity extends BaseActivity {
    private Bundle mBundle;

    @Override
    protected void initData(Bundle bundle) {
        mBundle = bundle;
        boolean writePermissions = PermissionUtils.requestWritePermissions(this);
        boolean readPermissions = PermissionUtils.requestReadPermissions(this);
        if (writePermissions && readPermissions) {
            RouteServiceManager.jumpPage(HOME_PAGE_PATH, bundle);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.READ_FILE_REQUEST_CODE || requestCode == PermissionUtils.WRITE_FILE_REQUEST_CODE) {
            if (grantResults.length > 1) {
                RouteServiceManager.jumpPage(HOME_PAGE_PATH, mBundle);
            }
        } else {
            finish();
        }
    }
}
