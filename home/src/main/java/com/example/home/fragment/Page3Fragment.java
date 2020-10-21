package com.example.home.fragment;

import android.os.Bundle;

import com.example.base.fragment.MvvmFragment;
import com.example.base.viewmodel.ViewStatus;
import com.example.home.R;
import com.example.home.bean.HomeChangeVO;
import com.example.home.databinding.FragmentPage3Binding;
import com.example.home.viewmodel.HomeChangeViewModel;

public class Page3Fragment extends MvvmFragment<FragmentPage3Binding, HomeChangeViewModel, HomeChangeVO> {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_page3;
    }

    @Override
    protected void observeChangeViewStatus(ViewStatus viewStatus) {

    }

    @Override
    protected void observeChangeData(HomeChangeVO data) {

    }

    @Override
    protected HomeChangeViewModel getViewModel() {
        return null;
    }

    @Override
    protected void init(Bundle bundle) {

    }
}
