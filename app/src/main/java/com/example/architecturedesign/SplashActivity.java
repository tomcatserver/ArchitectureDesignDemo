package com.example.architecturedesign;

import android.os.Bundle;


import com.example.base.BaseActivity;
import com.example.router.RouteServiceManager;

import static com.example.router.Router.HOME_PAGE_PATH;

public class SplashActivity extends BaseActivity {

    @Override
    protected void initData(Bundle bundle) {
        RouteServiceManager.jumpPage(HOME_PAGE_PATH, bundle);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
