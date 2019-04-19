package com.example.admin.ridesharemobileclient.ui.mytrip;

import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.ui.registertrip.RegisterTripAdapter;

public class MyTripActivity extends AppCompatActivity {
    private TabLayout tlMyTrip;
    private ViewPager vpMyTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip);

        initView();
        init();
    }

    private void init() {
        MyTripAdapter adapter = new MyTripAdapter(getSupportFragmentManager());
        vpMyTrip.setAdapter(adapter);
        tlMyTrip.setupWithViewPager(vpMyTrip);

        tlMyTrip.getTabAt(0).setIcon(R.drawable.ic_driver);
        tlMyTrip.getTabAt(1).setIcon(R.drawable.ic_hitchhiker);
        tlMyTrip.setTabTextColors(R.color.colorBlackLight, R.color.colorBlackLight);

        View view = tlMyTrip.getChildAt(0);
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
        tlMyTrip = findViewById(R.id.tlMyTrip);
        vpMyTrip = findViewById(R.id.vpMyTrip);
    }
}
