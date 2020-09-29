package com.example.home.viewmodel;

import com.example.base.viewmodel.MvvmBaseViewModel;
import com.example.base.viewmodel.ViewStatus;
import com.example.home.bean.HomeChangeVO;
import com.example.home.model.HomeChangeModel;

public class HomeChangeViewModel extends MvvmBaseViewModel<HomeChangeModel, HomeChangeVO> {

    public HomeChangeViewModel() {
        mModel = new HomeChangeModel();
        mModel.register(this);
    }

    public void requestData() {
        mViewStatusLivedata.setValue(ViewStatus.LOADING);
        mModel.request();
    }

    public void clearData() {
        mModel.unRegister(this);
    }
}
