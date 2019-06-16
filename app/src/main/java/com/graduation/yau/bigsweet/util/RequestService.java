package com.graduation.yau.bigsweet.util;

import com.graduation.yau.bigsweet.model.BeautyPhoto;
import com.graduation.yau.bigsweet.model.BeautyPhotoData;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by yyj on 2019/6/16.
 */

public interface RequestService {

    // 新浪美图的网址格式
    @GET("data/福利/{size}/{page}")
    Observable<BeautyPhotoData> getPhotoList(
            @Header("Cache-Control") String cacheControl,
            @Path("size") int size,
            @Path("page") int page);
}
