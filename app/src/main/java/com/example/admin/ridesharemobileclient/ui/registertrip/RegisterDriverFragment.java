package com.example.admin.ridesharemobileclient.ui.registertrip;

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

public class RegisterDriverFragment extends Fragment {
    private View view;
    private RecyclerView rvRegisterDriver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_driver, container, false);

        initView();
        init();

        return view;
    }

    private void init() {
        RegisterDriverAdapter adapter = new RegisterDriverAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvRegisterDriver.setLayoutManager(layoutManager);
        rvRegisterDriver.setAdapter(adapter);
    }

    private void initView() {
        rvRegisterDriver = view.findViewById(R.id.rvRegisterDriver);
    }
}
