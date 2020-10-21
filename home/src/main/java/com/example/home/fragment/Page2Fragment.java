package com.example.home.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.base.fragment.MvvmFragment;
import com.example.base.viewmodel.ViewStatus;
import com.example.home.R;
import com.example.home.adpater.Page2ItemAdapter;
import com.example.home.bean.Page2ListBean;
import com.example.home.bean.Pager2ItemBean;
import com.example.home.databinding.FragmentPage2Binding;
import com.example.home.viewmodel.Page2ViewModel;

import java.util.List;

public class Page2Fragment extends MvvmFragment<FragmentPage2Binding, Page2ViewModel, Page2ListBean> {

    private Page2ViewModel mPage2ViewModel = new Page2ViewModel();
    private Page2ItemAdapter mPage2ItemAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_page2;
    }

    @Override
    protected void observeChangeViewStatus(ViewStatus viewStatus) {

    }

    @Override
    protected void observeChangeData(Page2ListBean data) {
        if (data != null) {
            List<Pager2ItemBean> list = data.getList();
            mPage2ItemAdapter.setData(list);
            mViewDataBinding.list.setAdapter(mPage2ItemAdapter);
            mPage2ItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected Page2ViewModel getViewModel() {
        return mPage2ViewModel;
    }


    @Override
    protected void init(Bundle bundle) {
        mPage2ItemAdapter = new Page2ItemAdapter(getContext());
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mViewDataBinding.list.setLayoutManager(mLinearLayoutManager);
        mPage2ViewModel.requestData();
    }


}
