package com.example.admin.ridesharemobileclient.ui.detailtripregister;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.ui.map.MapActivity;

public class DetailTripRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivShowMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip_register);
        
        initView();
        initEvent();
        init();
    }

    private void init() {
    }

    private void initEvent() {
        ivShowMap.setOnClickListener(this);
    }

    private void initView() {
        ivShowMap = findViewById(R.id.ivShowMap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivShowMap:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                break;
        }
    }
}
