package com.example.home.model;

import android.util.Log;

import com.example.base.model.MvvmBaseModel;
import com.example.base.util.YWLogUtil;
import com.example.home.api.IHomePage1Service;
import com.example.home.bean.HomeChangeVO;
import com.example.network.CommonNetworkApi;
import com.example.network.bean.Response;
import com.example.network.model.IMvvmNetworkObserver;
import com.example.network.observer.BaseObserver;

import io.reactivex.Observable;

public class HomeChangeModel extends MvvmBaseModel<HomeChangeVO> {

    public HomeChangeModel() {
        setCache(HomeChangeVO.class.getSimpleName(), HomeChangeVO.class);
    }

    @Override
    protected void loadData() {
        Observable<Response<HomeChangeVO>> newsChannels = CommonNetworkApi.getService(IHomePage1Service.class).getNewsChannels(4, "", 1, 20);
        Observable<Response<HomeChangeVO>> compose = newsChannels.compose(CommonNetworkApi.getInstance().applySchedulers(new BaseObserver<>(false, this, new IMvvmNetworkObserver<Response<HomeChangeVO>>() {

            @Override
            public void onSuccess(Response<HomeChangeVO> t, boolean isNeedCache) {
                YWLogUtil.e("tag", "onSuccess: ----t=" + t.getData().toString() + ",isFromCache=" + isNeedCache);
                loadSuccess(t.getData(), isNeedCache);
            }

            @Override
            public void onFailure(Throwable e) {
                YWLogUtil.e("", "onFailure: ---e=" + e);
                loadFail(e.getMessage());
            }
        })));
    }

    public void request() {
        requestData();
    }
}
