package com.example.base.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(savedInstanceState);
        if (layoutRes() != 0) {
            setContentView(layoutRes());
            initView();
        }
    }

    protected abstract void initData(Bundle bundle);

    protected void initView() {

    }

    protected int layoutRes() {
        return 0;
    }
}
