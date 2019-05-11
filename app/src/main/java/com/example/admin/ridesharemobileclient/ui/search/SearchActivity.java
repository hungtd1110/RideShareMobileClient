package com.example.admin.ridesharemobileclient.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.ui.mapsearch.MapSearchActivity;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvTrip;
    private TextView tvStartPosition, tvEndPosition;
    private LinearLayout llStartPosition, llEndPosition;
    private ImageView ivBack, ivFilter, ivShowMap, ivSearch;
    private DrawerLayout dlSearch;

    private static final int REQUEST_SEARCH_START = 1;
    private static final int REQUEST_SEARCH_END = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        initEvent();
        init();
    }

    private void init() {
        SearchAdapter adapter = new SearchAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTrip.setLayoutManager(layoutManager);
        rvTrip.setAdapter(adapter);
    }

    private void initEvent() {
        llStartPosition.setOnClickListener(this);
        llEndPosition.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        ivShowMap.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
    }

    private void initView() {
        rvTrip = findViewById(R.id.rvSearch);
        tvStartPosition = findViewById(R.id.tvStartPosition);
        tvEndPosition = findViewById(R.id.tvEndPosition);
        llStartPosition = findViewById(R.id.llStartPosition);
        llEndPosition = findViewById(R.id.llEndPosition);
        ivBack = findViewById(R.id.ivBack);
        ivFilter = findViewById(R.id.ivFilter);
        ivShowMap = findViewById(R.id.ivShowMap);
        ivSearch = findViewById(R.id.ivSearch);
        dlSearch = findViewById(R.id.dlSearch);
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
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivFilter:
                dlSearch.openDrawer(Gravity.END);
                break;
            case R.id.ivShowMap:
                Intent intent = new Intent(this, MapSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ivSearch:
                handleSearch();
                break;
        }
    }

    private void handleSearch() {
    }

    private void showSearch(int requestSearch) {
        Intent intent = new PlaceAutocomplete.IntentBuilder()
                .accessToken(getString(R.string.map_token))
                .placeOptions(PlaceOptions.builder()
                        .backgroundColor(getResources().getColor(R.color.colorBackground3))
                        .limit(10)
                        .build(PlaceOptions.MODE_CARDS))
                .build(this);
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
                } else if (requestCode == REQUEST_SEARCH_END) {
                    tvEndPosition.setText(carmenFeature.placeName());
                }
            }
        }
    }
}
