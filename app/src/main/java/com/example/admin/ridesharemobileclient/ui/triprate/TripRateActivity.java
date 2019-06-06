package com.example.admin.ridesharemobileclient.ui.triprate;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.ridesharemobileclient.R;

public class TripRateActivity extends AppCompatActivity {
    private ViewPager vpTripRate;
    private TabLayout tlTripRate;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_rate);
        initView();
        initEvent();
        init();
    }

    private void init() {
        try {
            TripRateAdapter adapter = new TripRateAdapter(getSupportFragmentManager());
            vpTripRate.setAdapter(adapter);
            tlTripRate.setupWithViewPager(vpTripRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        try {
            vpTripRate = findViewById(R.id.vpTripRate);
            tlTripRate = findViewById(R.id.tlTripRate);
            ivBack = findViewById(R.id.ivBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
