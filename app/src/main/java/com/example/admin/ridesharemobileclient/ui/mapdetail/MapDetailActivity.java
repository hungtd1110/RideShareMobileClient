package com.example.admin.ridesharemobileclient.ui.mapdetail;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.RouteStep;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_ROUTE_STEP;

public class MapDetailActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private MapView mvMap;
    private MapboxMap mbmMap;
    private ImageView ivNavigation, ivBack;
    private TextView startPosition, endPosition;

    private MarkerViewManager mMarkerManager;
    private ArrayList<RouteStep> mListRouteStep = new ArrayList<>();
    private List<Point> coordinates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.map_token));
        setContentView(R.layout.activity_map_detail);

        initView();
        initEvent();
        getData();
        init(savedInstanceState);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        mListRouteStep = (ArrayList<RouteStep>) bundle.getSerializable(KEY_ROUTE_STEP);
    }

    private void initEvent() {
        ivNavigation.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void init(Bundle savedInstanceState) {
        try {
            mvMap.onCreate(savedInstanceState);
            mvMap.getMapAsync(this);

            PlaceUtils.setFullNamePosition(mListRouteStep.get(0).getLatitude(),
                    mListRouteStep.get(0).getLongitude(),
                    startPosition);
            PlaceUtils.setFullNamePosition(mListRouteStep.get(mListRouteStep.size() - 1).getLatitude(),
                    mListRouteStep.get(mListRouteStep.size() - 1).getLongitude(),
                    endPosition);
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
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            LatLng[] waypoints = new LatLng[mListRouteStep.size()];

            for (int i = 0; i < mListRouteStep.size(); i++) {
                RouteStep routeStep = mListRouteStep.get(i);
                Icon icon;

                waypoints[i] = new LatLng(Double.parseDouble(routeStep.getLatitude()),
                        Double.parseDouble(routeStep.getLongitude()));

                coordinates.add(Point.fromLngLat(Double.parseDouble(routeStep.getLatitude()),
                        Double.parseDouble(routeStep.getLongitude())));

                if (i == 0) {
                    icon = iconFactory.fromResource(R.mipmap.ic_map_start);
                } else if (i == mListRouteStep.size() - 1) {
                    icon = iconFactory.fromResource(R.mipmap.ic_map_end);
                } else {
                    icon = iconFactory.fromResource(R.drawable.ic_position);
                }

                Address address = geocoder.getFromLocation(Double.parseDouble(routeStep.getLatitude()),
                        Double.parseDouble(routeStep.getLongitude()), 1).get(0);

                mbmMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(routeStep.getLatitude()),
                                Double.parseDouble(routeStep.getLongitude())))
                        .title(address.getAddressLine(0)))
                        .setIcon(icon);
            }

//            Icon iconStart = iconFactory.fromResource(R.mipmap.ic_map_start);
//            mbmMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(Double.parseDouble(mListRouteStep.get(0).getLatitude()),
//                            Double.parseDouble(mListRouteStep.get(0).getLongitude())))
//                    .title(startAddress.getAddressLine(0)))
//                    .setIcon(iconStart);
//
//            Icon iconEnd = iconFactory.fromResource(R.mipmap.ic_map_end);
//            mbmMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(latEnd, lngEnd))
//                    .title(endAddress.getAddressLine(0)))
//                    .setIcon(iconEnd);
//
//            Icon i1 = iconFactory.fromResource(R.mipmap.ic_map_end);
//            mbmMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(mPoint1.latitude(), mPoint1.longitude()))
//                    .title(endAddress.getAddressLine(0)))
//                    .setIcon(i1);
//
//            Icon i2 = iconFactory.fromResource(R.mipmap.ic_map_end);
//            mbmMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(mPoint2.latitude(), mPoint2.longitude()))
//                    .title(endAddress.getAddressLine(0)))
//                    .setIcon(i2);

//            if (origin != null && destination != null) {
//                NavigationRoute.Builder builder = NavigationRoute.builder(this)
//                        .accessToken(getString(R.string.map_token))
//                        .origin(origin)
//                        .destination(destination);
//
//                for (Point waypoint : listWayPoint) {
//                    builder.addWaypoint(waypoint);
//                }
//
//                builder.build()
//                        .getRoute(new Callback<DirectionsResponse>() {
//                            @Override
//                            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                                List<DirectionsRoute> listRoute = response.body().routes();
//
//                                //chỉ show route
//                                for (DirectionsRoute route : listRoute) {
//                                    NavigationMapRoute navigationMapRoute = new NavigationMapRoute(null, mvMap, mbmMap, R.style.NavigationMapRoute);
//                                    navigationMapRoute.addRoute(route);
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
//
//                            }
//                        });
//            }
            mbmMap.addPolyline(new PolylineOptions()
                    .add(waypoints)
                    .color(Color.parseColor("#4080ff"))
                    .width(5));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNavigation() {
        try {
            NavigationRoute.Builder builder = NavigationRoute.builder(this)
                    .accessToken(getString(R.string.map_token))
                    .profile(DirectionsCriteria.PROFILE_DRIVING)
                    .origin(Point.fromLngLat(Double.parseDouble(mListRouteStep.get(0).getLongitude()),
                            Double.parseDouble(mListRouteStep.get(0).getLatitude())))
                    .destination(Point.fromLngLat(Double.parseDouble(mListRouteStep.get(mListRouteStep.size() - 1).getLongitude()),
                            Double.parseDouble(mListRouteStep.get(mListRouteStep.size() - 1).getLatitude())));

//            for (Point waypoint : coordinates) {
//                builder.addWaypoint(waypoint);
//            }

            builder.build()
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

//            MapboxMapMatching.builder()
//                    .accessToken(Mapbox.getAccessToken())
//                    .coordinates(coordinates)
//                    .steps(true)
//                    .voiceInstructions(true)
//                    .bannerInstructions(true)
//                    .profile(DirectionsCriteria.PROFILE_DRIVING)
//                    .build()
//                    .enqueueCall(new Callback<MapMatchingResponse>() {
//
//                        @Override
//                        public void onResponse(Call<MapMatchingResponse> call, Response<MapMatchingResponse> response) {
//                            if (response.isSuccessful()) {
//                                DirectionsRoute route = response.body().matchings().get(0).toDirectionRoute();
//                                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
//                                    .directionsRoute(route)
//                                    .shouldSimulateRoute(true)
//                                    .build();
//                                NavigationLauncher.startNavigation(MapDetailActivity.this, options);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<MapMatchingResponse> call, Throwable throwable) {
//
//                        }
//                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
