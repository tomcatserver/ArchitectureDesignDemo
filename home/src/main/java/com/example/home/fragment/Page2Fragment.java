package com.example.home.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.base.MvvmFragment;
import com.example.base.viewmodel.ViewStatus;
import com.example.home.R;
import com.example.home.bean.HomeChangeVO;
import com.example.home.databinding.FragmentPage2Binding;
import com.example.home.viewmodel.HomeChangeViewModel;

public class Page2Fragment extends MvvmFragment<FragmentPage2Binding, HomeChangeViewModel, HomeChangeVO> {

    HomeChangeViewModel mHomeChangeViewModel = new HomeChangeViewModel();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_page2;
    }

    @Override
    protected void observeChangeViewStatus(ViewStatus viewStatus) {

    }

    @Override
    protected void observeChangeData(HomeChangeVO data) {
        if (data != null) {
            mViewDataBinding.tvPager2.setText(data.getFloors().get(0).getName());
        }
    }

    @Override
    protected HomeChangeViewModel getViewModel() {
        return mHomeChangeViewModel;
    }

    @Override
    protected void init(Bundle bundle) {
        mViewDataBinding.tvPager2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeChangeViewModel.requestData();
            }
        });
    }


}
