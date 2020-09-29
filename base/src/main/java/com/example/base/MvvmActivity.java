package com.example.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;

import com.example.base.viewmodel.MvvmBaseViewModel;
import com.example.base.viewmodel.ViewStatus;

public abstract class MvvmActivity<V extends ViewDataBinding, VM extends MvvmBaseViewModel, D> extends AppCompatActivity {
    protected VM mViewModel;
    protected V mViewDataBinding;

    @LayoutRes
    abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewDataBinding.executePendingBindings();
        if (mViewModel != null) {
            mViewModel.register();
            getLifecycle().addObserver(mViewModel);
            mViewModel.mData.observe(this, new Observer<D>() {
                @Override
                public void onChanged(D data) {
                    observeChangeData();
                }
            });

            mViewModel.mViewStatusLivedata.observe(this, new Observer<ViewStatus>() {
                @Override
                public void onChanged(ViewStatus viewStatus) {
                    observeChangeViewStatus();
                }
            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.unregister();
        }
    }

    protected abstract void observeChangeViewStatus();

    protected abstract void observeChangeData();

    protected abstract VM getViewModel();
}
