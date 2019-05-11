package com.example.admin.ridesharemobileclient.ui.detailtripregister;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.respone.DetailTripRespone;
import com.example.admin.ridesharemobileclient.ui.mapdetail.MapDetailActivity;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.DATA_DRIVER;
import static com.example.admin.ridesharemobileclient.config.Const.DATA_HITCHHIKER;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;
import static com.example.admin.ridesharemobileclient.config.Const.PREFIX_IMAGE_ADDRESS;

public class DetailTripRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private IAPIHelper mIAPIHelper;

    private TextView tvType, tvStartPosition, tvEndPosition, tvDate, tvTime, tvVehicle, tvNumberSeat, tvPrice, tvNote, tvMap,
            tvName, tvRegister;
    private ImageView ivBack;
    private CircleImageView civImage;

    private ProgressDialog mProgressDialog;
    private SimpleDateFormat mDateFormat, mTimeFormat;
    private String idDriver, idHitchhiker, type;
    private DetailTripRespone mDetailTripRespone;

    private static final String TAG = "DetailTripRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip_register);

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

    @SuppressLint("SimpleDateFormat")
    private void init() {
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTimeFormat = new SimpleDateFormat("hh:mm:ss");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Đang xử lý");

        if (type.equals(DATA_DRIVER)) {
            getInfoDriver();
        } else if (type.equals(DATA_HITCHHIKER)) {
            getInfoHitchhiker();
        }
    }

    private void getInfoHitchhiker() {
    }

    private void getInfoDriver() {
        try {
            mIAPIHelper = APIHelper.getInstance();

            Call<BaseRespone> call = mIAPIHelper.getDriver(App.sToken, idDriver);
            call.enqueue(new Callback<BaseRespone>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken<DetailTripRespone>() {
                        }.getType();
                        mDetailTripRespone = new Gson().fromJson(response.body().getMetadata().toString(), type);
                        Log.d(TAG, "onResponse: " + mDetailTripRespone);

                        showInforDriver();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInforDriver() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(mDetailTripRespone.getTime()));

            Picasso.get().
                    load(PREFIX_IMAGE_ADDRESS + mDetailTripRespone.getAvatar()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(civImage);
            tvName.setText(mDetailTripRespone.getUsername());
            tvType.setText(getString(R.string.type0));
            PlaceUtils.setNamePosition(mDetailTripRespone.getStartLatitude(), mDetailTripRespone.getStartLongitude(), tvStartPosition);
            PlaceUtils.setNamePosition(mDetailTripRespone.getEndLatitude(), mDetailTripRespone.getEndLongitude(), tvEndPosition);
            tvDate.setText(mDateFormat.format(calendar.getTime()));
            tvTime.setText(mTimeFormat.format(calendar.getTime()));

            String vehicle = "";
            switch (mDetailTripRespone.getNumberSeat()) {
                case "0":
                    vehicle = getString(R.string.vehicle0);
                    break;
                case "1":
                    vehicle = getString(R.string.vehicle1);
                    break;
                case "2":
                    vehicle = getString(R.string.vehicle2);
                    break;
            }
            tvVehicle.setText(vehicle);

            tvNumberSeat.setText(mDetailTripRespone.getNumberSeat());
            tvPrice.setText(mDetailTripRespone.getPrice());
            tvNote.setText(mDetailTripRespone.getNote());

            if (mDetailTripRespone.getUserId().equals(App.sUser.getUserId())) {
                tvRegister.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        tvMap.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    private void initView() {
        try {
            tvType = findViewById(R.id.tvType);
            tvStartPosition = findViewById(R.id.tvStartPosition);
            tvEndPosition = findViewById(R.id.tvEndPosition);
            tvDate = findViewById(R.id.tvDate);
            tvTime = findViewById(R.id.tvTime);
            tvVehicle = findViewById(R.id.tvVehicle);
            tvNumberSeat = findViewById(R.id.tvNumberSeat);
            tvPrice = findViewById(R.id.tvPrice);
            tvNote = findViewById(R.id.tvNote);
            tvMap = findViewById(R.id.tvMap);
            tvName = findViewById(R.id.tvName);
            tvRegister = findViewById(R.id.tvRegister);
            civImage = findViewById(R.id.civImage);
            ivBack = findViewById(R.id.ivBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.tvMap:
                    Intent intent = new Intent(this, MapDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ivBack:
                    finish();
                    break;
                case R.id.tvRegister:
                    mProgressDialog.show();
                    handleRegisterDriver();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRegisterDriver() {
        try {
            Call<BaseRespone> call = mIAPIHelper.registerDriver(App.sToken, mDetailTripRespone.getDriverId());
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());

                        Toast.makeText(getBaseContext(), "Đã gửi đăng ký", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                        tvRegister.setVisibility(View.GONE);
                    } catch (Exception e) {
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
}
