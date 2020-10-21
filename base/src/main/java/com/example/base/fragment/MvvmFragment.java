package com.example.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.base.util.YWLogUtil;
import com.example.base.viewmodel.MvvmBaseViewModel;
import com.example.base.viewmodel.ViewStatus;

public abstract class MvvmFragment<V extends ViewDataBinding, VM extends MvvmBaseViewModel, D> extends Fragment {
    private static final String TAG = MvvmFragment.class.getSimpleName();
    protected V mViewDataBinding;
    protected VM mViewModel;

    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = getViewModel();
        if (mViewModel != null) {
            mViewModel.register();
            getLifecycle().addObserver(mViewModel);
            mViewModel.mData.observe(getViewLifecycleOwner(), new Observer<D>() {
                @Override
                public void onChanged(D data) {
                    observeChangeData(data);
                }
            });
            mViewModel.mViewStatusLivedata.observe(getViewLifecycleOwner(), new Observer<ViewStatus>() {
                @Override
                public void onChanged(ViewStatus viewStatus) {
                    if (viewStatus != null) {
                        observeChangeViewStatus(viewStatus);
                        switch (viewStatus) {
                            case LOADING:
                                YWLogUtil.e(TAG, "加载中。。。。。");
                                break;
                            case EMPTY:
                                YWLogUtil.e(TAG, "空数据。。。。。");
                                break;
                            case LOAD_FAILED:
                                YWLogUtil.e(TAG, "加载失败。。。。。");
                                break;
                            case SHOW_CONTENT:
                                YWLogUtil.e(TAG, "加载成功。。。。。");
                                break;
                        }
                    }
                }
            });
        }
        init(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mViewModel != null) {
            mViewModel.unregister();
        }
    }

    protected abstract void observeChangeViewStatus(ViewStatus viewStatus);

    protected abstract void observeChangeData(D data);

    protected abstract VM getViewModel();

    protected abstract void init(Bundle bundle);

}
