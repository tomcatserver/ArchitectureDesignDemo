package com.example.home.api;

import com.example.home.bean.ForceLoginVo;
import com.example.home.bean.HomeChangeVO;
import com.example.network.bean.Response;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IHomePage1Service {
    @POST("mobile/homepage/getNewlayermodel")
    @FormUrlEncoded
    Observable<Response<HomeChangeVO>> getNewsChannels(@Field("homepageVersion") int homepageVersion,
                                                       @Field("yztoken") String yztoken,
                                                       @Field("pageNo") int pageNo,
                                                       @Field("pageSize") int pageSize);

    @GET("presale/getImLoginSwitchInfo")
    Observable<Response<ForceLoginVo>> getImLoginSwitchInfo();
}
