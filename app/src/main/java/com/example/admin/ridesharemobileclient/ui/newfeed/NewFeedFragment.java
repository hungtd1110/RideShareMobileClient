package com.example.admin.ridesharemobileclient.ui.newfeed;

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

public class NewFeedFragment extends Fragment {
    private View mView;
    private ViewPager vpNewFeed;
    private TabLayout tlNewFeed;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_feed, container, false);

        initView();
        initEvent();
        init();

        return mView;
    }

    private void init() {
        try {
            NewFeedAdapter adapter = new NewFeedAdapter(getFragmentManager());
            vpNewFeed.setAdapter(adapter);
            tlNewFeed.setupWithViewPager(vpNewFeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {

    }

    private void initView() {
        try {
            vpNewFeed = mView.findViewById(R.id.vpNewFeed);
            tlNewFeed = mView.findViewById(R.id.tlNewFeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
