package com.example.admin.ridesharemobileclient.data;

import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.example.admin.ridesharemobileclient.entity.RouteStep;
import com.example.admin.ridesharemobileclient.entity.User;
import com.example.admin.ridesharemobileclient.entity.request.AcceptRequest;
import com.example.admin.ridesharemobileclient.entity.request.DriverRequest;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IAPIHelper {
    //Đăng ký

    @POST("user/register")
//    Call<User> register(@Body User user);
    @FormUrlEncoded
    Call<BaseRespone> register(@Field("email") String email, @Field("username") String username, @Field("password") String password);


    //Đăng nhập

    @POST("authentication/token")
    Call<BaseRespone> loginEmail(@Body User user);

    @POST()
    @FormUrlEncoded
    Call<String> loginPhone(@Field("phone") String phone);

    //Driver

    @GET("trip-by-driver")
    Call<BaseRespone> getListDriver(@Header("authorization") String token, @QueryMap Map<String, String> maps);

    @GET("trip-by-driver/my-trip")
    Call<BaseRespone> getListDriverSubmit(@Header("authorization") String token, @QueryMap Map<String, String> maps);

    @GET("trip-by-driver/my-trip-register")
    Call<BaseRespone> getListDriverRegister(@Header("authorization") String token, @QueryMap Map<String, String> maps);

    @GET("trip-by-driver/{idDriver}")
    Call<BaseRespone> getDriver(@Header("authorization") String token, @Path("idDriver") String idDriver);

    @GET("trip-by-driver/{idDriver}/route-step-accept")
    Call<BaseRespone> getRouteStep(@Header("authorization") String token, @Path("idDriver") String idDriver);

    @GET("trip-by-driver/status/{idDriver}")
    Call<BaseRespone> checkRegisterDriver(@Header("authorization") String token, @Path("idDriver") String idDriver);

    @POST("trip-by-driver/new-trip")
    @FormUrlEncoded
    Call<BaseRespone> addDriver(@Header("authorization") String token,
                                @Field("startLongitude") String startLongitude,
                                @Field("startLatitude") String startLatitude,
                                @Field("endLongitude") String endLongitude,
                                @Field("endLatitude") String endLatitude,
                                @Field("time") String time,
                                @Field("typeVehicle") String typeVehicle,
                                @Field("numberSeat") String numberSeat,
                                @Field("price") String price,
                                @Field("note") String note);

    @POST("trip-by-driver/update-trip")
    Call<BaseRespone> updateDriver(@Header("authorization") String token, @Body DriverRequest driverRequest);

    @GET("trip-by-driver/delete-trip/{driverId}")
    Call<BaseRespone> deleteDriver(@Header("authorization") String token, @Path("driverId") String idDriver);

    @POST("trip-by-driver/register-with-driver/{driverId}")
    Call<BaseRespone> registerDriver(@Header("authorization") String token,
                                     @Path("driverId") String idDriver,
                                     @Body RouteStep r);

    @GET("trip-by-driver/register-trip/{driverId}")
    Call<BaseRespone> getUserRegisterDriver(@Header("authorization") String token, @Path("driverId") String idDriver);

    @POST("trip-by-driver/accept-hitchhiker/{driverId}")
    Call<BaseRespone> driverAccept(@Header("authorization") String token,
                                   @Path("driverId") Long idDriver,
                                   @Body List<AcceptRequest> listAccept);


    //Hitchhiker

    @GET("trip-by-hitchhiker")
    Call<BaseRespone> getListHitchhiker(@Header("authorization") String token, @QueryMap Map<String, String> maps);

    @GET("trip-by-hitchhiker/my-trip")
    Call<BaseRespone> getListHitchhikerSubmit(@Header("authorization") String token, @QueryMap Map<String, String> maps);

    @GET("trip-by-hitchhiker/my-trip-register")
    Call<BaseRespone> getListHitchhikerRegister(@Header("authorization") String token, @QueryMap Map<String, String> maps);

    @GET("trip-by-hitchhiker/{hitchhikerId}")
    Call<BaseRespone> getHitchhiker(@Header("authorization") String token, @Path("hitchhikerId") String idHitchhiker);

    @GET("trip-by-hitchhiker/status/{idHitchhiker}")
    Call<BaseRespone> checkRegisterHitchhiker(@Header("authorization") String token, @Path("idHitchhiker") String idHitchhiker);

    @POST("trip-by-hitchhiker/new-trip")
    Call<BaseRespone> addHitchhiker(@Header("authorization") String token, @Body Hitchhiker hitchhiker);

    @GET("trip-by-hitchhiker/delete-trip/{hitchhikerId}")
    Call<BaseRespone> deleteHitchhiker(@Header("authorization") String token, @Path("hitchhikerId") String idHitchhiker);

    @POST("trip-by-hitchhiker/register-with-hitchhiker/{hitchhikerId}")
    Call<BaseRespone> registerHitchhiker(@Header("authorization") String token, @Path("hitchhikerId") String idHitchhiker);

    @GET("trip-by-hitchhiker/register-trip/{hitchhikerId}")
    Call<BaseRespone> getUserRegisterHitchhiker(@Header("authorization") String token, @Path("hitchhikerId") String idHitchhiker);

    @POST("trip-by-hitchhiker/accept-driver/{hitchhikerId}")
    Call<BaseRespone> hitchhikerAccept(@Header("authorization") String token,
                                       @Path("hitchhikerId") Long idHitchhiker,
                                       @Body AcceptRequest accept);

    //Message

    @GET("message/user-conversation")
    Call<BaseRespone> getListMessage(@Header("authorization") String token);

    @GET("message")
    Call<BaseRespone> getDetailMessage(@Header("authorization") String token, @QueryMap Map<String, String> maps);

    @GET("")
    Call<BaseRespone> getLastMessage(@Header("authorization") String token);


    //User

    @GET("user/detail")
    Call<BaseRespone> getDetailUser(@Header("authorization") String token);


    //Notification

    @GET("notification")
    Call<BaseRespone> getNotification(@Header("authorization") String token);
}
