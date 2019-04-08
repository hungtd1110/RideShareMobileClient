package com.example.admin.ridesharemobileclient.data;

import com.example.admin.ridesharemobileclient.entity.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface IAPIHelper {
    @POST("user/register")
//    Call<User> register(@Body User user);
    @FormUrlEncoded
    Call<BaseRespone> register(@Field("email") String email, @Field("username") String username, @Field("password") String password);

    @POST("")
    Call<String> loginEmail(@Body User user);

    @POST()
    @FormUrlEncoded
    Call<String> loginPhone(@Field("phone") String phone);

    @GET("/api/trip-by-driver")
    Call<BaseRespone> getDriver(@Header("authorization") String token, @QueryMap Map<String, String> maps);

    @GET("")
    Call<BaseRespone> getHitchhiker(@Header("authorization") String token, @QueryMap Map<String, String> maps);
}
