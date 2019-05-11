package com.example.admin.ridesharemobileclient.ui.tripregister;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TripRegisterAdapter extends FragmentStatePagerAdapter {
    public TripRegisterAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;

        switch (i) {
            case 0:
                fragment = new DriverRegisterFragment();
                break;
            case 1:
                fragment = new HitchhikerRegisterFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
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
