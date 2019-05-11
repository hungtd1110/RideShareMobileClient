package com.example.admin.ridesharemobileclient.ui.mapdetail;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapDetailActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private MapView mvMap;
    private MapboxMap mbmMap;
    private ImageView ivNavigation, ivBack;
    private TextView startPosition, endPosition;

    private MarkerViewManager mMarkerManager;
    private Point mOrigin, mDestination;
    private Address startAddress, endAddress;

    //fake data
    private double latStart = 21.036445;
    private double lngStart = 105.773045;
    private double latEnd = 21.028619;
    private double lngEnd = 105.786000;
    private Point mPoint1 = Point.fromLngLat(105.784895, 21.026606);
    private Point mPoint2 = Point.fromLngLat(105.778512, 21.030588);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.map_token));
        setContentView(R.layout.activity_map_detail);

        initView();
        initEvent();
        init(savedInstanceState);
    }

    private void initEvent() {
        ivNavigation.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void init(Bundle savedInstanceState) {
        try {
            mvMap.onCreate(savedInstanceState);
            mvMap.getMapAsync(this);

            mOrigin = Point.fromLngLat(lngStart, latStart);
            mDestination = Point.fromLngLat(lngEnd, latEnd);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            startAddress = geocoder.getFromLocation(latStart, lngStart, 1).get(0);
            endAddress = geocoder.getFromLocation(latEnd, lngEnd, 1).get(0);

            startPosition.setText(startAddress.getAddressLine(0));
            endPosition.setText(endAddress.getAddressLine(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mvMap = findViewById(R.id.mvMap);
        ivNavigation = findViewById(R.id.ivNavigation);
        ivBack = findViewById(R.id.ivBack);
        startPosition = findViewById(R.id.startPosition);
        endPosition = findViewById(R.id.endPosition);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mbmMap = mapboxMap;

        mMarkerManager = new MarkerViewManager(mvMap, mbmMap);
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                showRoute();
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
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private void showRoute() {
        try {
            IconFactory iconFactory = IconFactory.getInstance(MapDetailActivity.this);

            Icon iconStart = iconFactory.fromResource(R.mipmap.ic_map_start);
            mbmMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latStart, lngStart))
                    .title(startAddress.getAddressLine(0)))
                    .setIcon(iconStart);

            Icon iconEnd = iconFactory.fromResource(R.mipmap.ic_map_end);
            mbmMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latEnd, lngEnd))
                    .title(endAddress.getAddressLine(0)))
                    .setIcon(iconEnd);

            Icon i1 = iconFactory.fromResource(R.mipmap.ic_map_end);
            mbmMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mPoint1.latitude(), mPoint1.longitude()))
                    .title(endAddress.getAddressLine(0)))
                    .setIcon(i1);

            Icon i2 = iconFactory.fromResource(R.mipmap.ic_map_end);
            mbmMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mPoint2.latitude(), mPoint2.longitude()))
                    .title(endAddress.getAddressLine(0)))
                    .setIcon(i2);

            NavigationRoute.builder(this)
                    .accessToken(getString(R.string.map_token))
                    .addWaypoint(mPoint1)
//                    .addWaypoint(mPoint2)
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNavigation() {
        try {
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

                            NavigationLauncher.startNavigation(MapDetailActivity.this, options);
                        }

                        @Override
                        public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
