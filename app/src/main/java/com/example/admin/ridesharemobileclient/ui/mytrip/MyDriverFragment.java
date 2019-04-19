package com.example.admin.ridesharemobileclient.ui.mytrip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.ui.registertrip.RegisterDriverAdapter;

public class MyDriverFragment extends Fragment {
    private View view;
    private RecyclerView rvMyDriver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_driver, container, false);

        initView();
        init();

        return view;
    }

    private void init() {
        MyDriverAdapter adapter = new MyDriverAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvMyDriver.setLayoutManager(layoutManager);
        rvMyDriver.setAdapter(adapter);
    }

    private void initView() {
        rvMyDriver = view.findViewById(R.id.rvMyDriver);
    }
}
