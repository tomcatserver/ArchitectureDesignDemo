package com.example.base.model;

public interface IMvvmNetworkObserver<F> {
    void onSuccess(F t, boolean isFromCache);

    void onFailure(Throwable e);
}
