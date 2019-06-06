package com.example.admin.ridesharemobileclient.ui.routestep;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.RouteStep;
import com.example.admin.ridesharemobileclient.ui.addtrip.AddTripActivity;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.util.ArrayList;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_ROUTE_STEP;

public class RouteStepActivity extends AppCompatActivity {
    private RecyclerView rvRouteStep;
    private ImageView ivAdd;
    private TextView tvFinish;

    private MultiTypeAdapter mAdapter = new MultiTypeAdapter();
    private Items mItems = new Items();
    private ArrayList<RouteStep> listRouteStep = new ArrayList<>();
    private int positionRoute;

    private static final int REQUEST_ADD = 1;
    private static final int REQUEST_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_step);

        initView();
        initEvent();
        getDate();
        init();
    }

    private void getDate() {
        try {
            Bundle bundle = getIntent().getExtras();
            listRouteStep = (ArrayList<RouteStep>) bundle.getSerializable(KEY_ROUTE_STEP);
            mItems.addAll(listRouteStep);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rvRouteStep = findViewById(R.id.rvRouteStep);
        ivAdd = findViewById(R.id.ivAdd);
        tvFinish = findViewById(R.id.tvFinish);
    }

    private void initEvent() {
        ivAdd.setOnClickListener(new AddClick());
        tvFinish.setOnClickListener(new FinishClick());
    }

    private void init() {
        mAdapter.register(RouteStep.class, new ItemRouteStepBinder(this, new ItemRouteStepBinder.CallBack() {
            @Override
            public void onEdit(int position) {
                positionRoute = position;
                handldeAdd(REQUEST_EDIT);
            }
        }));

        mAdapter.setItems(mItems);
        rvRouteStep.setLayoutManager(new LinearLayoutManager(this));
        rvRouteStep.setAdapter(mAdapter);
    }

    class AddClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            handldeAdd(REQUEST_ADD);
        }
    }

    class FinishClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), AddTripActivity.class);
            intent.putExtra(KEY_ROUTE_STEP, listRouteStep);
            startActivity(intent);
        }
    }

    private void handldeAdd(int request) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(getString(R.string.map_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(getResources().getColor(R.color.colorBackground3))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(RouteStepActivity.this);
            startActivityForResult(intent, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    CarmenFeature carmenFeature = PlaceAutocomplete.getPlace(data);
                    double lat = ((Point) carmenFeature.geometry()).latitude();
                    double lng = ((Point) carmenFeature.geometry()).longitude();

                    RouteStep routeStep = new RouteStep();
                    routeStep.setLatitude(String.valueOf(lat));
                    routeStep.setLongitude(String.valueOf(lng));

                    if (requestCode == REQUEST_ADD) {
                        listRouteStep.add(routeStep);
                        mItems.add(routeStep);
                    } else if (requestCode == REQUEST_EDIT) {
                        listRouteStep.set(positionRoute, routeStep);
                        mItems.set(positionRoute, routeStep);
                    }

                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Intent intent = new Intent();
//        intent.putExtra(KEY_ROUTE_STEP, listRouteStep);
//        setResult(RESULT_ROUTE, intent);
    }
}
