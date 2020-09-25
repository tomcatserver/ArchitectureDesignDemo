package com.example.network.observer;

import android.text.TextUtils;

import com.example.common.views.AppExecutors;
import com.example.network.errorhandler.ExceptionHandle;
import com.example.network.model.IMvvmNetworkObserver;
import com.example.network.utils.ResponseBodyToDiskUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class BaseObserver<T> implements Observer<T> {
    private IMvvmNetworkObserver<T> mMvvmNetworkObserver;

    public BaseObserver(IMvvmNetworkObserver<T> mvvmNetworkObserver) {
        mMvvmNetworkObserver = mvvmNetworkObserver;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        mMvvmNetworkObserver.onSuccess(t, false);
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
