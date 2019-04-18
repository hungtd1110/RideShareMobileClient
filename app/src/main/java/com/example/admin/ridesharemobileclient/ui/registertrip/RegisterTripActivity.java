package com.example.admin.ridesharemobileclient.ui.registertrip;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.admin.ridesharemobileclient.R;

public class RegisterTripActivity extends AppCompatActivity {
    private TabLayout tlRegisterTrip;
    private ViewPager vpRegisterTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_trip);

        initView();
        init();
    }

    private void init() {
        RegisterTripAdapter adapter = new RegisterTripAdapter(getSupportFragmentManager());
        vpRegisterTrip.setAdapter(adapter);
        tlRegisterTrip.setupWithViewPager(vpRegisterTrip);

        tlRegisterTrip.getTabAt(0).setIcon(R.drawable.ic_driver);
        tlRegisterTrip.getTabAt(1).setIcon(R.drawable.ic_hitchhiker);
    }

    private void initView() {
        tlRegisterTrip = findViewById(R.id.tlRegisterTrip);
        vpRegisterTrip = findViewById(R.id.vpRegisterTrip);
    }
}
