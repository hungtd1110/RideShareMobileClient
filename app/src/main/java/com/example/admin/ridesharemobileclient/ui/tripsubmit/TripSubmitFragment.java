package com.example.admin.ridesharemobileclient.ui.tripsubmit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.ui.addtrip.AddTripActivity;

/**
 * Created by admin on 3/18/2019.
 */

public class TripSubmitFragment extends Fragment {
    private View mView;
    private ViewPager vpTripSubmit;
    private TabLayout tlTripSubmit;
    private ImageView ivAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_trip_submit, container, false);

        initView();
        initEvent();
        init();

        return mView;
    }

    private void init() {
        try {
            TripSubmitAdapter adapter = new TripSubmitAdapter(getFragmentManager());
            vpTripSubmit.setAdapter(adapter);
            tlTripSubmit.setupWithViewPager(vpTripSubmit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        try {
            ivAdd.setOnClickListener(new AddClick());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        try {
            vpTripSubmit = mView.findViewById(R.id.vpTripSubmit);
            tlTripSubmit = mView.findViewById(R.id.tlTripSubmit);
            ivAdd = mView.findViewById(R.id.ivAdd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class AddClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(getContext(), AddTripActivity.class);
                getContext().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
