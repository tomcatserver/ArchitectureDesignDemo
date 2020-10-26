package com.example.home.viewmodel;

import com.example.base.viewmodel.MvvmBaseViewModel;
import com.example.base.viewmodel.ViewStatus;
import com.example.home.bean.Page2ListBean;
import com.example.home.model.Page2ListModel;

public class Page2ViewModel extends MvvmBaseViewModel<Page2ListModel, Page2ListBean> {

    public Page2ViewModel() {
        mModel = new Page2ListModel();
    }

    public void requestData() {
        mViewStatusLivedata.setValue(ViewStatus.LOADING);
        mModel.request();
    }

}
