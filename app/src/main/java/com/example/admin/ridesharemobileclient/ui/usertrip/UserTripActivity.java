package com.example.admin.ridesharemobileclient.ui.usertrip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.respone.DriverUserRespone;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.ACCEPT;
import static com.example.admin.ridesharemobileclient.config.Const.DATA_DRIVER;
import static com.example.admin.ridesharemobileclient.config.Const.DATA_HITCHHIKER;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;

public class UserTripActivity extends AppCompatActivity {
    private IAPIHelper mIAPIHelper;

    private RecyclerView rvUserTrip;
    private ImageView ivBack;

    private UserTripAdapter mAdapter;
    private String idDriver, idHitchhiker, type;

    private static final String TAG = "UserTripActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_trip);

        initView();
        initEvent();
        getData();
        init();
    }

    private void getData() {
        try {
            Bundle bundle = getIntent().getExtras();
            type = bundle.getString(KEY_TYPE);

            if (type.equals(DATA_DRIVER)) {
                idDriver = bundle.getString(KEY_ID);
            } else if (type.equals(DATA_HITCHHIKER)) {
                idHitchhiker = bundle.getString(KEY_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        mIAPIHelper = APIHelper.getInstance();

        mAdapter = new UserTripAdapter(this);
        rvUserTrip.setLayoutManager(new LinearLayoutManager(this));
        rvUserTrip.setAdapter(mAdapter);

        if (type.equals(DATA_DRIVER)) {
            initDriver();
        } else if (type.equals(DATA_HITCHHIKER)) {
            initHitchhiker();
        }
    }

    private void initHitchhiker() {
    }

    private void initDriver() {
        try {
            Call<BaseRespone> call = mIAPIHelper.getUserRegisterDriver(App.sToken, idDriver);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());

                        Type type = new TypeToken<List<DriverUserRespone>>() {
                        }.getType();
                        List<DriverUserRespone> listUser = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        handleSortList(listUser);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSortList(List<DriverUserRespone> listUser) {
        try {
            List<DriverUserRespone> listAccept = new ArrayList<>();

            for (DriverUserRespone user : listUser) {
                if (user.getStatus().equals(ACCEPT)) {
                    listAccept.add(user);
                }
            }

            mAdapter.setData(listAccept);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        rvUserTrip = findViewById(R.id.rvUserTrip);
        ivBack = findViewById(R.id.ivBack);
    }
}
