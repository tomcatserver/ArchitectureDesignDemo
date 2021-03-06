package com.example.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.base.util.YWLogUtil;
import com.example.base.viewmodel.ViewStatus;
import com.example.home.R;
import com.example.home.bean.HomeChangeVO;
import com.example.home.viewmodel.HomeChangeViewModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Page1Fragment extends Fragment {
    private static final String TAG = Page1Fragment.class.getName();
    private ImageView mIvImage;
    HomeChangeViewModel mViewModel = new HomeChangeViewModel();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_page1, null, false);
//        viewDataBinding.tvGetData
        View view = viewDataBinding.getRoot();
        findView(view);
        initData(savedInstanceState);
        return viewDataBinding.getRoot();
    }


    private void initData(@Nullable Bundle savedInstanceState) {
        getLifecycle().addObserver(mViewModel);
        mViewModel.mData.observe(getViewLifecycleOwner(), new Observer<HomeChangeVO>() {
            @Override
            public void onChanged(HomeChangeVO homeChangeVO) {
                if (homeChangeVO != null) {
                    YWLogUtil.e(TAG, homeChangeVO.toString());
                }
            }
        });
        mViewModel.mViewStatusLivedata.observe(getViewLifecycleOwner(), new Observer<ViewStatus>() {
            @Override
            public void onChanged(ViewStatus viewStatus) {
                if (viewStatus != null) {
                    switch (viewStatus) {
                        case LOADING:
                            YWLogUtil.e(TAG, "加载中。。。。。");
                            break;
                        case EMPTY:
                            YWLogUtil.e(TAG, "空数据。。。。。");
                            break;
                        case LOAD_FAILED:
                            YWLogUtil.e(TAG, "加载失败。。。。。");
                            break;
                        case SHOW_CONTENT:
                            YWLogUtil.e(TAG, "加载成功。。。。。");
                            break;
                    }
                }
            }
        });
    }


    public void findView(View view) {
        view.findViewById(R.id.tv_get_data);
        mIvImage = view.findViewById(R.id.iv_img);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //post请求
//                postMethod();
                //get请求
//                getMethod();
//                downMethod();
//                uploadMethod();
                mViewModel.requestData();

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void uploadMethod() {
        String path = "/storage/emulated/0/download.jpg";
        File file = new File(path);
        RequestBody fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody body = new MultipartBody.Builder()
//                .addFormDataPart("isGateWay", "1")
//                .addFormDataPart("pickType", "0")
//                .addFormDataPart("allowEdit", "0")
//                .addFormDataPart("pickType", "0")
                .addFormDataPart("header", file.getName(), fileRQ)
                .build();

//        CommonNetworkApi instance = CommonNetworkApi.getInstance();
//        Observable<ResponseBody> responseBodyObservable = CommonNetworkApi.getService(IHomePage1Service.class).uploadFile(body);
//        Observable<ResponseBody> compose = responseBodyObservable.compose(instance.applySchedulers(new BaseObserver<>(new IMvvmNetworkObserver<ResponseBody>() {
//
//
//            @Override
//            public void onSuccess(ResponseBody t, boolean isFromCache) {
//                Log.e("tag", "onSuccess: ----t=" + t.toString() + ",isFromCache=" + isFromCache);
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                Log.e("", "onFailure: ---e=" + e);
//            }
//        })));
    }

    private void downMethod() {
        downLoadFile();


//        https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601012353587&di=c8ff374e94f7606c62b7f95ec9143807&imgtype=0&src=http%3A%2F%2Fa0.att.hudong.com%2F70%2F91%2F01300000261284122542917592865.jpg
    }

    private void downLoadFile() {
//        final CommonNetworkApi instance = CommonNetworkApi.getInstance();
//        instance.setDownFileProgress(new IDownFileProgress() {
//            @Override
//            public void progress(FileLoadEvent event) {
//                Log.e("tag", "progress: ------count=" + event.getCurrentCount() + ",total=" + event.getTotalCount());
//            }
//        });
//        Observable<ResponseBody> newsChannels = CommonNetworkApi.getService(IHomePage1Service.class).download("timg?image&quality=80&size=b9999_10000&sec=1601012353587&di=c8ff374e94f7606c62b7f95ec9143807&imgtype=0&src=http%3A%2F%2Fa0.att.hudong.com%2F70%2F91%2F01300000261284122542917592865.jpg");
//        Observable<ResponseBody> compose = newsChannels.compose(instance.applySchedulers(new BaseObserver<>(new IMvvmNetworkObserver<ResponseBody>() {
//
//
//            @Override
//            public void onSuccess(ResponseBody t, boolean isFromCache) {
//                Log.e("tag", "onSuccess: ----t=" + t.toString() + ",isFromCache=" + isFromCache);
//                instance.writeResponseBodyToDisk(t, new IDownFileToDisk() {
//                    @Override
//                    public void onResult(String path) {
//                        mIvImage.setImageBitmap(BitmapFactory.decodeFile(path));
//                    }
//                });
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                Log.e("", "onFailure: ---e=" + e);
//            }
//        })));
    }

    private void postMethod() {
//        Observable<Response<HomeChangeVO>> newsChannels = CommonNetworkApi.getService(IHomePage1Service.class).getNewsChannels(4, "", 1, 20);
//        Observable<Response<HomeChangeVO>> compose = newsChannels.compose(CommonNetworkApi.getInstance().applySchedulers(new BaseObserver<>(new IMvvmNetworkObserver<Response<HomeChangeVO>>() {
//
//            @Override
//            public void onSuccess(Response<HomeChangeVO> t, boolean isFromCache) {
//                Log.e("tag", "onSuccess: ----t=" + t.getData().toString() + ",isFromCache=" + isFromCache);
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                Log.e("", "onFailure: ---e=" + e);
//            }
//        })));

    }

    private void getMethod() {
//        Observable<Response<ForceLoginVo>> newsChannels = CommonNetworkApi.getService(IHomePage1Service.class).getImLoginSwitchInfo();
//        Observable<Response<ForceLoginVo>> compose = newsChannels.compose(CommonNetworkApi.getInstance().applySchedulers(new BaseObserver<Response<ForceLoginVo>>(new IMvvmNetworkObserver<Response<ForceLoginVo>>() {
//            @Override
//            public void onSuccess(Response<ForceLoginVo> t, boolean isFromCache) {
//                Log.e("tag", "onSuccess: ---t=" + t.getData().toString() + ",isFromCache=" + isFromCache);
//            }
//
//            @Override
//            public void onFailure(Throwable e) {
//                Log.e("tag", "onFailure: ----e=" + e);
//            }
//        })));
    }

//    @Override
//    protected int layoutRes() {
//        return R.layout.fragment_page1;
//    }

}
