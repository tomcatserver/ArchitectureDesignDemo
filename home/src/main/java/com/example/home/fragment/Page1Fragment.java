package com.example.home.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.base.BaseFragment;
import com.example.home.R;
import com.example.home.api.IHomePage1Service;
import com.example.home.bean.ForceLoginVo;
import com.example.home.bean.HomeChangeVO;
import com.example.network.CommonNetworkApi;
import com.example.network.bean.Response;
import com.example.network.model.IMvvmNetworkObserver;
import com.example.network.observer.BaseObserver;

import io.reactivex.Observable;

public class Page1Fragment extends BaseFragment {

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
    }


    @Override
    protected void findView(View view) {
        view.findViewById(R.id.tv_get_data);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //post请求
                postMethod();
                //get请求
//                getMethod();
            }
        });
    }

    private void postMethod() {
        Observable<Response<HomeChangeVO>> newsChannels = CommonNetworkApi.getService(IHomePage1Service.class).getNewsChannels(4, "", 1, 20);
        Observable<Response<HomeChangeVO>> compose = newsChannels.compose(CommonNetworkApi.getInstance().applySchedulers(new BaseObserver<>(new IMvvmNetworkObserver<Response<HomeChangeVO>>() {

            @Override
            public void onSuccess(Response<HomeChangeVO> t, boolean isFromCache) {
                Log.e("tag", "onSuccess: ----t=" + t.getData().toString() + ",isFromCache=" + isFromCache);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("", "onFailure: ---e=" + e);
            }
        })));

    }

    private void getMethod() {
        Observable<Response<ForceLoginVo>> newsChannels = CommonNetworkApi.getService(IHomePage1Service.class).getImLoginSwitchInfo();
        Observable<Response<ForceLoginVo>> compose = newsChannels.compose(CommonNetworkApi.getInstance().applySchedulers(new BaseObserver<Response<ForceLoginVo>>(new IMvvmNetworkObserver<Response<ForceLoginVo>>() {
            @Override
            public void onSuccess(Response<ForceLoginVo> t, boolean isFromCache) {
                Log.e("tag", "onSuccess: ---t=" + t.getData().toString() + ",isFromCache=" + isFromCache);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("tag", "onFailure: ----e=" + e);
            }
        })));
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_page1;
    }

}
