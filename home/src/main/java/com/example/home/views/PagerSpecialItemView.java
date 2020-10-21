package com.example.home.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.example.base.item.BaseItemView;
import com.example.base.util.YWLogUtil;
import com.example.home.R;

import com.example.home.bean.Pager2ItemBean;
import com.example.home.databinding.ItemViewSpecialPager2Binding;
import com.example.webview.WebViewActivity;

public class PagerSpecialItemView extends BaseItemView<ItemViewSpecialPager2Binding, Pager2ItemBean> {
    public PagerSpecialItemView(Context context) {
        super(context);
    }

    public PagerSpecialItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerSpecialItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PagerSpecialItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onRootClick(View view) {

    }

    @Override
    protected void onDataUpdated() {

    }

    @Override
    protected void setDataToView(final Pager2ItemBean data) {
        getDataBinding().tvItemPager2.setText(data.content);
        Glide.with(this).load(data.icon).into(getDataBinding().ivImg);
        getDataBinding().flJump.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                YWLogUtil.e("tag", "jump -----webview");
                WebViewActivity.startCommonWeb(getContext(), data.jumpUrl);
            }
        });
    }


    @Override
    protected int setViewLayoutId() {
        return R.layout.item_view_special_pager2;
    }
}
