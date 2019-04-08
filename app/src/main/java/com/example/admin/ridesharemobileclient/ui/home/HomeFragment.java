package com.example.admin.ridesharemobileclient.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.Driver;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private IAPIHelper mIAPIHelper;

    private View mView;
    private RecyclerView rvDriver, rvHitchhiker;

    private static final String TAG = "HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
        initEvent();
        init();

        return mView;
    }

    private void init() {
        mIAPIHelper = APIHelper.getInstance();

        initDriver();
        initHitchhiker();
    }

    private void initHitchhiker() {
        try {
            final HitchhikerAdapter adapter = new HitchhikerAdapter(getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvHitchhiker.setLayoutManager(layoutManager);
            rvHitchhiker.setAdapter(adapter);

            Map<String, String> maps = new HashMap<>();
            maps.put("page", "1");
            maps.put("size", "10");

            Call<BaseRespone> call = mIAPIHelper.getHitchhiker(App.sToken, maps);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(@NonNull Call<BaseRespone> call, @NonNull Response<BaseRespone> response) {
                    BaseRespone baseRespone = response.body();
                    Log.d(TAG, "onResponse: " + baseRespone);

                    if (baseRespone != null) {
                        Gson gson = new Gson();
                        Hitchhiker[] hitchhikers = gson.fromJson((String) baseRespone.getMetadata(), Hitchhiker[].class);

                        ArrayList<Hitchhiker> listHitchhiker = new ArrayList<>();
                        Collections.addAll(listHitchhiker, hitchhikers);
                        adapter.setData(listHitchhiker);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseRespone> call, @NonNull Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDriver() {
        try {
            final DriverAdapter adapter = new DriverAdapter(getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvDriver.setLayoutManager(layoutManager);
            rvDriver.setAdapter(adapter);

            Map<String, String> maps = new HashMap<>();
            maps.put("page", "1");
            maps.put("size", "10");

            Call<BaseRespone> call = mIAPIHelper.getDriver(App.sToken, maps);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(@NonNull Call<BaseRespone> call, @NonNull Response<BaseRespone> response) {
                    BaseRespone baseRespone = response.body();
                    Log.d(TAG, "onResponse: " + baseRespone);

                    if (baseRespone != null) {
                        Gson gson = new Gson();
                        Driver[] drivers = gson.fromJson((String) baseRespone.getMetadata(), Driver[].class);

                        ArrayList<Driver> listDriver = new ArrayList<>();
                        Collections.addAll(listDriver, drivers);
                        adapter.setData(listDriver);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseRespone> call, @NonNull Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
    }

    private void initView() {
        rvDriver = mView.findViewById(R.id.rvDriver);
        rvHitchhiker = mView.findViewById(R.id.rvHitchhiker);
    }
}
