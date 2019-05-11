package com.example.admin.ridesharemobileclient.ui.tripsubmit;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by admin on 5/4/2019.
 */

public class TripSubmitAdapter extends FragmentStatePagerAdapter {
    public TripSubmitAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;

        switch (i) {
            case 0:
                fragment = new DriverSubmitFragment();
                break;
            case 1:
                fragment = new HitchhikerSubmitFragment();
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
