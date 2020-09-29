package com.example.network.observer;

import com.example.base.model.MvvmBaseModel;
import com.example.network.bean.Response;
import com.example.network.errorhandler.ExceptionHandle;
import com.example.network.model.IMvvmNetworkObserver;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseObserver<T> implements Observer<T> {
    private IMvvmNetworkObserver<T> mMvvmNetworkObserver;
    private MvvmBaseModel mBaseModel;
    private boolean mNeedCache;

    public BaseObserver(boolean isNeedCache, MvvmBaseModel baseModel, IMvvmNetworkObserver<T> mvvmNetworkObserver) {
        mNeedCache = isNeedCache;
        mBaseModel = baseModel;
        mMvvmNetworkObserver = mvvmNetworkObserver;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mBaseModel != null) {
            mBaseModel.addDisposable(d);
        }
    }

    @Override
    public void onNext(T t) {
        mMvvmNetworkObserver.onSuccess(t, mNeedCache);
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ExceptionHandle.ResponeThrowable) {
            mMvvmNetworkObserver.onFailure(e);
        } else {
            mMvvmNetworkObserver.onFailure(new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onComplete() {

    }
}
