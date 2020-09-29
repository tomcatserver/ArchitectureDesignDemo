package com.example.network.model;


public interface IMvvmNetworkObserver<F> {
    void onSuccess(F t, boolean isNeedCache);

    void onFailure(Throwable e);
}
