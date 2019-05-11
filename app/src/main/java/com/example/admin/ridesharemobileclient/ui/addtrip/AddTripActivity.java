package com.example.admin.ridesharemobileclient.ui.addtrip;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTripActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvStartPosition, tvEndPosition, tvDate, tvTime, tvAdd;
    private ImageView ivEditStartPosition, ivEditEndPosition, ivEditDate, ivEditTime, ivBack;
    private RadioButton rbVehicle0, rbVehicle1, rbVehicle2, rbType0, rbType1;
    private EditText etNumberSeat, etPrice, etNote;
    private ProgressDialog mProgressDialog;

    private SimpleDateFormat mDateFormat, mTimeFormat;
    private String startLat, endLat, startLng, endLng, typeVehicle, time;

    private static final int REQUEST_SEARCH_START = 1;
    private static final int REQUEST_SEARCH_END = 2;
    private static final String TAG = "AddTripActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        initView();
        initEvent();
        init();
    }

    private void initEvent() {
        ivEditStartPosition.setOnClickListener(this);
        ivEditEndPosition.setOnClickListener(this);
        ivEditDate.setOnClickListener(this);
        ivEditTime.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @SuppressLint("SimpleDateFormat")
    private void init() {
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTimeFormat = new SimpleDateFormat("hh:mm:ss");

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Đang xử lý");
    }

    private void initView() {
        tvStartPosition = findViewById(R.id.tvStartPosition);
        tvEndPosition = findViewById(R.id.tvEndPosition);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvAdd = findViewById(R.id.tvAdd);
        etNumberSeat = findViewById(R.id.etNumberSeat);
        etPrice = findViewById(R.id.etPrice);
        etNote = findViewById(R.id.etNote);
        ivEditStartPosition = findViewById(R.id.ivEditStartPosition);
        ivEditEndPosition = findViewById(R.id.ivEditEndPosition);
        ivEditDate = findViewById(R.id.ivEditDate);
        ivEditTime = findViewById(R.id.ivEditTime);
        ivBack = findViewById(R.id.ivBack);
        rbVehicle0 = findViewById(R.id.rbVehicle0);
        rbVehicle1 = findViewById(R.id.rbVehicle1);
        rbVehicle2 = findViewById(R.id.rbVehicle2);
        rbType0 = findViewById(R.id.rbType0);
        rbType1 = findViewById(R.id.rbType1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEditStartPosition:
                showSearch(REQUEST_SEARCH_START);
                break;
            case R.id.ivEditEndPosition:
                showSearch(REQUEST_SEARCH_END);
                break;
            case R.id.ivEditDate:
                handleDateSelect();
                break;
            case R.id.ivEditTime:
                handlTimeSelect();
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvAdd:
                //get type vehicle
                if (rbVehicle0.isChecked()) {
                    typeVehicle = "0";
                } else if (rbVehicle1.isChecked()) {
                    typeVehicle = "1";
                } else if (rbVehicle2.isChecked()) {
                    typeVehicle = "2";
                }

                //set time
                time = tvDate.getText().toString() + "T" + tvTime.getText().toString();

                //show progress
                mProgressDialog.show();

                //xác định kiểu chuyến đi được thêm
                if (rbType0.isChecked()) {
                    handlAddDriver();
                } else if (rbType1.isChecked()) {
                    handlAddHitchhiker();
                }
                break;
        }
    }

    private void handlAddHitchhiker() {
        try {
            Hitchhiker hitchhiker = new Hitchhiker();
            hitchhiker.setStartLongitude(startLng);
            hitchhiker.setStartLatitude(startLat);
            hitchhiker.setEndLongitude(endLng);
            hitchhiker.setEndLatitude(endLat);
            hitchhiker.setTime(time);
            hitchhiker.setNumberSeat(etNumberSeat.getText().toString());
            hitchhiker.setPrice(etPrice.getText().toString());
            hitchhiker.setNote(etNote.getText().toString());

            Call<BaseRespone> call = APIHelper.getInstance().addHitchhiker(App.sToken, hitchhiker);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());

                        Toast.makeText(AddTripActivity.this, "Thêm chuyến đi thành công", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());

                    Toast.makeText(AddTripActivity.this, "Lỗi thêm chuyến đi", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlAddDriver() {
        try {
            Call<BaseRespone> call = APIHelper.getInstance().addDriver(
                    App.sToken,
                    startLng,
                    startLat,
                    endLng,
                    endLat,
                    time,
                    typeVehicle,
                    etNumberSeat.getText().toString(),
                    etPrice.getText().toString(),
                    etNote.getText().toString());

            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());

                        Toast.makeText(AddTripActivity.this, "Thêm chuyến đi thành công", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());

                    Toast.makeText(AddTripActivity.this, "Lỗi thêm chuyến đi", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDateSelect() {
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

                        tvDate.setText(mDateFormat.format(calendar.getTime()));
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

    private void handlTimeSelect() {
        try {
            Calendar calendar = Calendar.getInstance();
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    try {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        tvTime.setText(mTimeFormat.format(calendar.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, hourOfDay, minute, true);

            timePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSearch(int requestSearch) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(getString(R.string.map_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(getResources().getColor(R.color.colorBackground3))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(this);
            startActivityForResult(intent, requestSearch);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    CarmenFeature carmenFeature = PlaceAutocomplete.getPlace(data);
                    double lat = ((Point) carmenFeature.geometry()).latitude();
                    double lng = ((Point) carmenFeature.geometry()).longitude();

                    if (requestCode == REQUEST_SEARCH_START) {
                        tvStartPosition.setText(carmenFeature.placeName());

                        startLat = String.valueOf(lat);
                        startLng = String.valueOf(lng);
                    } else if (requestCode == REQUEST_SEARCH_END) {
                        tvEndPosition.setText(carmenFeature.placeName());

                        endLat = String.valueOf(lat);
                        endLng = String.valueOf(lng);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
