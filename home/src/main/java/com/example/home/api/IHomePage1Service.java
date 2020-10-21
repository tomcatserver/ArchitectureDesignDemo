package com.example.home.api;

import android.util.ArrayMap;

import com.example.home.bean.ForceLoginVo;
import com.example.home.bean.HomeChangeVO;
import com.example.home.bean.Page2ListBean;
import com.example.network.bean.Response;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface IHomePage1Service {
    @POST("mobile/homepage/getNewlayermodel")
    @FormUrlEncoded
    Observable<Response<HomeChangeVO>> getNewsChannels(@Field("homepageVersion") int homepageVersion,
                                                       @Field("yztoken") String yztoken,
                                                       @Field("pageNo") int pageNo,
                                                       @Field("pageSize") int pageSize);

    @GET("presale/getImLoginSwitchInfo")
    Observable<Response<ForceLoginVo>> getImLoginSwitchInfo();

    @GET("mobile/homepage/getPager2Listtestsdss")
    Observable<Response<Page2ListBean>> getPager2List();

    /**
     * 下载文件
     * 如果下载大文件的一定要加上  @Streaming  注解
     *
     * @param fileUrl 文件的路径
     * @return 请求call
     */
    @GET
    Observable<ResponseBody> download(@Url String fileUrl);

    @POST("upload")
    Observable<ResponseBody> uploadFile(@Body RequestBody body);
}
