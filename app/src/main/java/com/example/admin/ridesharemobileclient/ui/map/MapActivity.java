package com.example.admin.ridesharemobileclient.ui.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.ridesharemobileclient.R;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private MapView mvMap;
    private MapboxMap mbmMap;
    private ImageView ivNavigation;

    private MarkerViewManager mMarkerManager;
    private Point mOrigin, mDestination;

    //fake data
    private double latStart = 21.037685;
    private double lngStart = 105.783337;
    private double latEnd = 21.006430;
    private double lngEnd = 105.843493;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.map_token));
        setContentView(R.layout.activity_map);

        initView();
        initEvent();
        init(savedInstanceState);
    }

    private void initEvent() {
        ivNavigation.setOnClickListener(this);
    }

    private void init(Bundle savedInstanceState) {
        mvMap.onCreate(savedInstanceState);
        mvMap.getMapAsync(this);

        mOrigin = Point.fromLngLat(lngStart, latStart);
        mDestination = Point.fromLngLat(lngEnd, latEnd);
        showRoute();
    }

    private void showRoute() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewMarker = inflater.inflate(R.layout.view_marker, null);
        ImageView ivMarker = viewMarker.findViewById(R.id.ivMarker);

//        View viewMarkerEnd = inflater.inflate(R.layout.view_marker, null);
//        ImageView ivMarkerEnd = viewMarkerStart.findViewById(R.id.ivMarker);
//        ivMarkerEnd.setBackgroundResource(R.drawable.ic_marker_end);

//        ivMarker.setBackgroundResource(R.drawable.ic_marker_start);
//        MarkerView markerStart = new MarkerView(new LatLng(latStart, lngStart), viewMarker);
//
//        ivMarker.setBackgroundResource(R.drawable.ic_marker_end);
//        MarkerView markerEnd = new MarkerView(new LatLng(latEnd, lngEnd), viewMarker);
//
//        mMarkerManager.addMarker(markerStart);
//        mMarkerManager.addMarker(markerEnd);

        NavigationRoute.builder(this)
                .accessToken(getString(R.string.map_token))
                .origin(mOrigin)
                .destination(mDestination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        DirectionsRoute route = response.body().routes().get(0);

                        //chỉ show route
                        NavigationMapRoute navigationMapRoute = new NavigationMapRoute(null, mvMap, mbmMap, R.style.NavigationMapRoute);
                        navigationMapRoute.addRoute(route);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }

    private void initView() {
        mvMap = findViewById(R.id.mvMap);
        ivNavigation = findViewById(R.id.ivNavigation);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mbmMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mMarkerManager = new MarkerViewManager(mvMap, mbmMap);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mvMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mvMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mvMap.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mvMap.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMarkerManager.onDestroy();
        mvMap.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mvMap.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mvMap.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNavigation:
                showNavigation();
                break;
        }
    }

    private void showNavigation() {
        NavigationRoute.builder(this)
                .accessToken(getString(R.string.map_token))
                .origin(mOrigin)
                .destination(mDestination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        DirectionsRoute route = response.body().routes().get(0);

                        //show chạy hẳn cái navigation
                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(route)
                                .shouldSimulateRoute(true)
                                .build();

                        NavigationLauncher.startNavigation(MapActivity.this, options);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });
    }
}
