package com.example.admin.ridesharemobileclient.ui.tripregister;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.ridesharemobileclient.R;

public class TripRegisterFragment extends Fragment {
    private View mView;
    private ViewPager vpTripRegister;
    private TabLayout tlTripRegister;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_trip_register, container, false);

        initView();
        initEvent();
        init();

        return mView;
    }

    private void init() {
        TripRegisterAdapter adapter = new TripRegisterAdapter(getFragmentManager());
        vpTripRegister.setAdapter(adapter);
        tlTripRegister.setupWithViewPager(vpTripRegister);
    }

    private void initEvent() {

    }

    private void initView() {
        vpTripRegister = mView.findViewById(R.id.vpTripRegister);
        tlTripRegister = mView.findViewById(R.id.tlTripRegister);
    }
}
