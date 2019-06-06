package com.example.admin.ridesharemobileclient.ui.search;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.ui.searchadvance.SearchAdvanceActivity;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_END_LAT;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_END_LNG;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_START_LAT;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_START_LNG;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;
import static com.example.admin.ridesharemobileclient.config.Const.TYPE_DRIVER;
import static com.example.admin.ridesharemobileclient.config.Const.TYPE_HITCHHIKER;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rbType0, rbType1;
    private TextView tvStartPosition, tvEndPosition, tvSearch;
    private ImageView ivEditStartPosition, ivEditEndPosition, ivBack;
    private String startLat, endLat, startLng, endLng, type;

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

    private void initView() {
        tvStartPosition = findViewById(R.id.tvStartPosition);
        tvEndPosition = findViewById(R.id.tvEndPosition);
        tvSearch = findViewById(R.id.tvSearch);
        rbType0 = findViewById(R.id.rbType0);
        rbType1 = findViewById(R.id.rbType1);
        ivEditStartPosition = findViewById(R.id.ivEditStartPosition);
        ivEditEndPosition = findViewById(R.id.ivEditEndPosition);
        ivBack = findViewById(R.id.ivBack);
    }

    private void initEvent() {
        ivEditStartPosition.setOnClickListener(this);
        ivEditEndPosition.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void init() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivEditStartPosition:
                showSearch(REQUEST_SEARCH_START);
                break;
            case R.id.ivEditEndPosition:
                showSearch(REQUEST_SEARCH_END);
                break;
            case R.id.tvSearch:
                if (rbType0.isChecked()) {
                    type = TYPE_DRIVER;
                } else if (rbType1.isChecked()) {
                    type = TYPE_HITCHHIKER;
                }

                Intent intent = new Intent(this, SearchAdvanceActivity.class);
                intent.putExtra(KEY_TYPE, type);
                intent.putExtra(KEY_START_LAT, startLat);
                intent.putExtra(KEY_END_LAT, endLat);
                intent.putExtra(KEY_START_LNG, startLng);
                intent.putExtra(KEY_END_LNG, endLng);
                startActivity(intent);
        }
    }

    private void showSearch(int requestSearch) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(getString(R.string.map_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(getResources().getColor(R.color.colorBackground3))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(this);
            startActivityForResult(intent, requestSearch);
        } catch (Resources.NotFoundException e) {
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

                    if (requestCode == REQUEST_SEARCH_START) {
                        tvStartPosition.setText(carmenFeature.placeName());

                        startLat = String.valueOf(lat);
                        startLng = String.valueOf(lng);
                    } else if (requestCode == REQUEST_SEARCH_END) {
                        tvEndPosition.setText(carmenFeature.placeName());

                        endLat = String.valueOf(lat);
                        endLng = String.valueOf(lng);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
