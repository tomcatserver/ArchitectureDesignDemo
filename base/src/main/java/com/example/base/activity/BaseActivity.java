package com.example.base.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.base.util.StatusBarUtil;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        initData(savedInstanceState);
        if (layoutRes() != 0) {
            setContentView(layoutRes());
            initView();
        }
    }

    private void setStatusBar() {
        //这里做了两件事情，1.使状态栏透明并使contentView填充到状态栏 2.预留出状态栏的位置，防止界面上的控件离顶部靠的太近。这样就可以实现开头说的第二种情况的沉浸式状态栏了
        StatusBarUtil.setTransparent(this);
    }

    protected abstract void initData(Bundle bundle);

    protected void initView() {

    }

    protected int layoutRes() {
        return 0;
    }
}
