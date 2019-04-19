package com.example.admin.ridesharemobileclient.ui.registertrip;

import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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
        tlRegisterTrip.setTabTextColors(R.color.colorBlackLight, R.color.colorBlackLight);

        View view = tlRegisterTrip.getChildAt(0);
        int padding = (int) getResources().getDimension(R.dimen.padding_normal);

        if (view instanceof LinearLayout) {
            ((LinearLayout) view).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorBlackLight3));
            drawable.setSize(1, 1);
            ((LinearLayout) view).setDividerPadding(padding);
            ((LinearLayout) view).setDividerDrawable(drawable);
        }
    }

    private void initView() {
        tlRegisterTrip = findViewById(R.id.tlRegisterTrip);
        vpRegisterTrip = findViewById(R.id.vpRegisterTrip);
    }
}
