package com.example.admin.ridesharemobileclient.ui.registertrip;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RegisterTripAdapter extends FragmentPagerAdapter {
    RegisterTripAdapter(FragmentManager fm) {
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
                fragment = new RegisterDriverFragment();
                break;
            case 1:
                fragment = new RegisterHitchhikerFragment();
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
