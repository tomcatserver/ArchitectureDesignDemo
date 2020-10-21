package com.example.home.model;

import com.example.base.model.MvvmBaseModel;
import com.example.base.util.YWLogUtil;
import com.example.home.api.IHomePage1Service;
import com.example.home.bean.Page2ListBean;
import com.example.home.bean.Pager2ItemBean;
import com.example.network.CommonNetworkApi;
import com.example.network.bean.Response;
import com.example.network.model.IMvvmNetworkObserver;
import com.example.network.observer.BaseObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class Page2ListModel extends MvvmBaseModel<Page2ListBean> {

    public Page2ListModel() {
        setCache(Page2ListBean.class.getSimpleName(), Page2ListBean.class);
    }

    @Override
    protected void loadData() {
        Observable<Response<Page2ListBean>> newsChannels = CommonNetworkApi.getService(IHomePage1Service.class).getPager2List();
        Observable<Response<Page2ListBean>> compose = newsChannels.compose(CommonNetworkApi.getInstance().applySchedulers(new BaseObserver<>(false, this, new IMvvmNetworkObserver<Response<Page2ListBean>>() {

            @Override
            public void onSuccess(Response<Page2ListBean> t, boolean isNeedCache) {
                YWLogUtil.e("tag", "onSuccess: ----t=" + t.getData().toString() + ",isFromCache=" + isNeedCache);
                loadSuccess(t.getData(), isNeedCache);
            }

            @Override
            public void onFailure(Throwable e) {
                YWLogUtil.e("", "onFailure: ---e=" + e);

                Page2ListBean page2ListBean = new Page2ListBean();
                List<Pager2ItemBean> list = new ArrayList();
                for (int i = 0; i < 100; i++) {
                    Pager2ItemBean pager2ItemBean = new Pager2ItemBean();
                    if (i % 2 == 0) {
//                        pager2ItemBean.jumpUrl = "https://m.111.com.cn/yyw/app/jsBridge/index.html";
                        pager2ItemBean.jumpUrl = "https://im.111.com.cn/yyw-im/pc/YYWIM.html?system=1_android&department=100009&productId=&provinceId=1&orderId=&param={}";
                        pager2ItemBean.content = "这是一个特殊特殊特殊特殊特殊特殊特殊的测试数据" + i;
                        pager2ItemBean.icon = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601439875343&di=7a988450de0442b7cbe9211821508b71&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F01%2F35%2F34%2F79573bdede9d1a5.jpg";
                        pager2ItemBean.type = "1";
                    } else {
                        pager2ItemBean.content = "这是一个普通普通的测试数据" + i;
                        pager2ItemBean.icon = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601445283655&di=ab531715a816e14c7cccd54914b7ee33&imgtype=0&src=http%3A%2F%2Fpic1.16pic.com%2F00%2F37%2F98%2F16pic_3798816_b.jpg";
                        pager2ItemBean.type = "2";
                        pager2ItemBean.jumpUrl = "https://m.111.com.cn/cmsPage/2019f3e83aee0307113044/index.html";
                    }
                    list.add(pager2ItemBean);
                }
                page2ListBean.setList(list);
                loadSuccess(page2ListBean, false);
//                loadFail(e.getMessage());
            }
        })));
    }

    public void request() {
        requestData();
    }
}
