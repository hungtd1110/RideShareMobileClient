package com.example.admin.ridesharemobileclient.ui.searchadvance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.Driver;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_END_LAT;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_END_LNG;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_START_LAT;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_START_LNG;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;
import static com.example.admin.ridesharemobileclient.config.Const.PAGE;
import static com.example.admin.ridesharemobileclient.config.Const.SIZE;
import static com.example.admin.ridesharemobileclient.config.Const.TYPE_DRIVER;
import static com.example.admin.ridesharemobileclient.config.Const.TYPE_HITCHHIKER;

public class SearchAdvanceActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvTrip;
    private ImageView ivBack, ivFilter;
    private DrawerLayout dlSearch;
    private TextView tvCancel, tvApply, tvTimeBot, tvTimeTop;
    private EditText etPriceBot, etPriceTop;
    private CheckBox cbNearMe;

    private IAPIHelper mIAPIHelper;
    private String startLat, endLat, startLng, endLng, type;
    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String TAG = "SearchAdvanceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_advance);

        initView();
        initEvent();
        getData();
        init();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString(KEY_TYPE);
        startLat = bundle.getString(KEY_START_LAT);
        endLat = bundle.getString(KEY_END_LAT);
        startLng = bundle.getString(KEY_START_LNG);
        endLng = bundle.getString(KEY_END_LNG);
    }

    private void init() {
        mIAPIHelper = APIHelper.getInstance();
        mAdapter = new MultiTypeAdapter();
        mItems = new Items();

        Map<String, String> maps = new HashMap<>();
        maps.put("page", PAGE);
        maps.put("size", SIZE);
        maps.put("startLongitude", startLng);
        maps.put("startLatitude", startLat);
        maps.put("radius", "0");
//        maps.put("endLatitude", endLng);
//        maps.put("endLatitude", endLat);

        mAdapter.register(Driver.class, new ItemDriverBinder(this));
        mAdapter.register(Hitchhiker.class, new ItemHitchhikerBinder(this));

        mAdapter.setItems(mItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTrip.setLayoutManager(layoutManager);
        rvTrip.setAdapter(mAdapter);

        if (type.equals(TYPE_DRIVER)) {
            showListDriver(maps);
        } else if (type.equals(TYPE_HITCHHIKER)) {
            showListHitchhiker(maps);
        }
    }

    private void showListHitchhiker(Map<String, String> maps) {
        Call<BaseRespone> call = mIAPIHelper.getListHitchhiker(App.sToken, maps);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(@NonNull Call<BaseRespone> call, @NonNull Response<BaseRespone> response) {
                try {
                    Type type = new TypeToken<List<Hitchhiker>>() {
                    }.getType();
                    List<Hitchhiker> listHitchhiker = new Gson().fromJson((String) response.body().getMetadata(), type);
                    mItems.clear();
                    mItems.addAll(listHitchhiker);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseRespone> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void showListDriver(Map<String, String> maps) {
        Call<BaseRespone> call = mIAPIHelper.getListDriver(App.sToken, maps);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(@NonNull Call<BaseRespone> call, @NonNull Response<BaseRespone> response) {
                try {
                    Type type = new TypeToken<List<Driver>>() {
                    }.getType();
                    List<Driver> listDriver = new Gson().fromJson((String) response.body().getMetadata(), type);
                    mItems.clear();
                    mItems.addAll(listDriver);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseRespone> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void initEvent() {
        ivBack.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        tvTimeBot.setOnClickListener(this);
        tvTimeTop.setOnClickListener(this);
    }

    private void initView() {
        rvTrip = findViewById(R.id.rvSearch);
        ivBack = findViewById(R.id.ivBack);
        ivFilter = findViewById(R.id.ivFilter);
        dlSearch = findViewById(R.id.dlSearch);
        tvCancel = findViewById(R.id.tvCancel);
        tvApply = findViewById(R.id.tvApply);
        tvTimeBot = findViewById(R.id.tvTimeBot);
        tvTimeTop = findViewById(R.id.tvTimeTop);
        etPriceBot = findViewById(R.id.etPriceBot);
        etPriceTop = findViewById(R.id.etPriceTop);
        cbNearMe = findViewById(R.id.cbNearMe);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivFilter:
                dlSearch.openDrawer(Gravity.END);
                break;
            case R.id.tvCancel:
                dlSearch.closeDrawers();
                break;
            case R.id.tvTimeBot:
                handleSelectTimeBot();
                break;
            case R.id.tvTimeTop:
                handleSelectTimeTop();
                break;
            case R.id.tvApply:
                Map<String, String> maps = new HashMap<>();
                maps.put("page", PAGE);
                maps.put("size", SIZE);
                maps.put("startLongitude", startLng);
                maps.put("startLatitude", startLat);
                if (cbNearMe.isChecked()) {
                    maps.put("radius", "10");
                }
                else {
                    maps.put("radius", "0");
                }

                if (!TextUtils.isEmpty(tvTimeBot.getText().toString())) {
                    String time = tvTimeBot.getText().toString() + "T00:00:00";
                    maps.put("timeBoundBottom", time);
                }

                if (!TextUtils.isEmpty(tvTimeTop.getText().toString())) {
                    String time = tvTimeTop.getText().toString() + "T00:00:00";
                    maps.put("timeBoundTop", time);
                }

                if (!TextUtils.isEmpty(etPriceBot.getText().toString())) {
                    maps.put("priceBoundBottom", etPriceBot.getText().toString());
                }

                if (!TextUtils.isEmpty(etPriceTop.getText().toString())) {
                    maps.put("priceBoundTop", etPriceTop.getText().toString());
                }

                if (type.equals(TYPE_DRIVER)) {
                    showListDriver(maps);
                } else if (type.equals(TYPE_HITCHHIKER)) {
                    showListHitchhiker(maps);
                }

                break;
        }
    }

    private void handleSelectTimeTop() {
        try {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, 0, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    try {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        tvTimeTop.setText(mDateFormat.format(calendar.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, year, month, day);

            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSelectTimeBot() {
        try {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, 0, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    try {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        tvTimeBot.setText(mDateFormat.format(calendar.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, year, month, day);

            datePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
