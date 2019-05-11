package com.example.admin.ridesharemobileclient.ui.mapsearch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.ridesharemobileclient.R;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback,
        PermissionsListener, View.OnClickListener {
    private MapView mvMap;
    private MapboxMap mbmMap;
    private PermissionsManager permissionsManager;
    private ImageView ivBack;

    private static final String TAG = "MapSearchActivity";

    //fake data
    private double latStart = 21.037685;
    private double lngStart = 105.783337;
    private double latEnd = 21.006430;
    private double lngEnd = 105.843493;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.map_token));
        setContentView(R.layout.activity_map_search);

        initView();
        initEvent();
        init(savedInstanceState);
    }

    private void initEvent() {
        ivBack.setOnClickListener(this);
    }

    private void init(Bundle savedInstanceState) {
        try {
            mvMap.onCreate(savedInstanceState);
            mvMap.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mvMap = findViewById(R.id.mvMap);
        ivBack = findViewById(R.id.ivBack);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mbmMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                showLocation(style);
            }
        });
    }

    private void showLocation(Style style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mbmMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, style).build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
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
            case R.id.ivBack:
                finish();
                break;
        }
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Log.d(TAG, "onExplanationNeeded: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mbmMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    showLocation(style);
                }
            });
        } else {
            Log.d(TAG, "onPermissionResult: ");
            finish();
        }
    }
}
