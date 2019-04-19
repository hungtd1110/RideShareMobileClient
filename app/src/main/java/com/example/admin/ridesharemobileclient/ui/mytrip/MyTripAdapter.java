package com.example.admin.ridesharemobileclient.ui.mytrip;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.ridesharemobileclient.ui.registertrip.RegisterDriverFragment;
import com.example.admin.ridesharemobileclient.ui.registertrip.RegisterHitchhikerFragment;

public class MyTripAdapter extends FragmentPagerAdapter {
    MyTripAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;

        switch (i) {
            case 0:
                fragment = new MyDriverFragment();
                break;
            case 1:
                fragment = new MyHitchhikerFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        switch (position) {
            case 0:
                title = "Lái xe";
                break;
            case 1:
                title = "Đi kèm";
                break;
        }

        return title;
    }
}
