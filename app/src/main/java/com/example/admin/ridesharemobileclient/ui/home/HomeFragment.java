package com.example.admin.ridesharemobileclient.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.Driver;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.example.admin.ridesharemobileclient.ui.registertrip.RegisterTripActivity;
import com.google.gson.Gson;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private IAPIHelper mIAPIHelper;

    private View mView;
    private RecyclerView rvDriver, rvHitchhiker;
    private LinearLayout llStartPosition, llEndPosition;
    private TextView tvStartPosition, tvEndPosition, tvRegisterTrip;

    private static final int REQUEST_SEARCH_START = 1;
    private static final int REQUEST_SEARCH_END = 2;
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
        llStartPosition.setOnClickListener(this);
        llEndPosition.setOnClickListener(this);
        tvRegisterTrip.setOnClickListener(this);
    }

    private void initView() {
        rvDriver = mView.findViewById(R.id.rvDriver);
        rvHitchhiker = mView.findViewById(R.id.rvHitchhiker);
        llStartPosition = mView.findViewById(R.id.llStartPosition);
        llEndPosition = mView.findViewById(R.id.llEndPosition);
        tvStartPosition = mView.findViewById(R.id.tvStartPosition);
        tvEndPosition = mView.findViewById(R.id.tvEndPosition);
        tvRegisterTrip = mView.findViewById(R.id.tvRegisterTrip);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llStartPosition:
                showSearch(REQUEST_SEARCH_START);
                break;
            case R.id.llEndPosition:
                showSearch(REQUEST_SEARCH_END);
                break;
            case R.id.tvRegisterTrip:
                Intent intent = new Intent(getContext(), RegisterTripActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showSearch(int requestSearch) {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(getString(R.string.map_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(Color.parseColor("#EEEEEE"))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(getActivity());
        startActivityForResult(intent, requestSearch);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                CarmenFeature carmenFeature = PlaceAutocomplete.getPlace(data);
                double lat = ((Point) carmenFeature.geometry()).latitude();
                double lng = ((Point) carmenFeature.geometry()).longitude();

                if (requestCode == REQUEST_SEARCH_START) {
                    tvStartPosition.setText(carmenFeature.placeName());
                }
                else if (requestCode == REQUEST_SEARCH_END){
                    tvEndPosition.setText(carmenFeature.placeName());
                }
            }
        }
    }
}
