package com.example.base.viewmodel;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.base.model.IBaseModelListener;
import com.example.base.model.MvvmBaseModel;

public class MvvmBaseViewModel<M extends MvvmBaseModel, D> extends ViewModel implements IBaseModelListener<D>, LifecycleObserver {
    protected M mModel;
    public MutableLiveData<D> mData = new MutableLiveData<>();
    public MutableLiveData<ViewStatus> mViewStatusLivedata = new MutableLiveData<>();

    public MvvmBaseViewModel() {
    }

    public void register() {
        if (mModel != null) {
            mModel.register(this);
        }
    }

    public void unregister() {
        if (mModel != null) {
            mModel.unregister(this);
        }
    }

    @Override
    protected void onCleared() {
        if (mModel != null) {
            mModel.cancel();
        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        mData.postValue(mData.getValue());
        mViewStatusLivedata.postValue(mViewStatusLivedata.getValue());
    }

    @Override
    public void onLoadFinish(MvvmBaseModel model, D data) {
        if (data == null) {
            mViewStatusLivedata.postValue(ViewStatus.EMPTY);
        } else {
            mViewStatusLivedata.postValue(ViewStatus.SHOW_CONTENT);
            mData.postValue(data);
        }

    }

    @Override
    public void onLoadFail(MvvmBaseModel model, String prompt) {
        mViewStatusLivedata.postValue(ViewStatus.LOAD_FAILED);
    }
}
