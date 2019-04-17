package com.example.admin.ridesharemobileclient.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.admin.ridesharemobileclient.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class MapDialog extends AlertDialog implements OnMapReadyCallback {
    private AlertDialog mDialog;

    private View mView;
    private MapView mvMap;
    private MapboxMap mpMap;

    public MapDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void show() {
        initView();
        init();

        Builder builder = new Builder(getContext());
        mDialog = builder.setView(mView).create();
        mDialog.show();
    }

    private void init() {
        Mapbox.getInstance(getContext(), getContext().getString(R.string.map_token));
        mvMap.getMapAsync(this);
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_map, null, false);
        mvMap = view.findViewById(R.id.mvMap);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        mpMap = mapboxMap;

        mpMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {

            }
        });
    }
}
