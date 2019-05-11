package com.example.admin.ridesharemobileclient.data;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.admin.ridesharemobileclient.config.Const.IP;

public class APIHelper {
    private static String BASE_URL = "http://" + IP + ":8080/api/";
    private static IAPIHelper mInstance;

    public static IAPIHelper getInstance() {
        if (mInstance == null) {
            new APIHelper();
        }
        return mInstance;
    }

    private APIHelper() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build();

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mInstance = retrofit.create(IAPIHelper.class);
    }
}
