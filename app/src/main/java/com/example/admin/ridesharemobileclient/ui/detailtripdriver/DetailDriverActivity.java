package com.example.admin.ridesharemobileclient.ui.detailtripdriver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.ui.map.MapActivity;
import com.example.admin.ridesharemobileclient.ui.map.MapDialog;

public class DetailDriverActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvShowMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip_driver);
        
        initView();
        initEvent();
        init();
    }

    private void init() {
    }

    private void initEvent() {
        tvShowMap.setOnClickListener(this);
    }

    private void initView() {
        tvShowMap = findViewById(R.id.tvShowMap);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvShowMap:
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);

//                MapDialog mapDialog = new MapDialog(this);
//                mapDialog.show();
                break;
        }
    }
}
