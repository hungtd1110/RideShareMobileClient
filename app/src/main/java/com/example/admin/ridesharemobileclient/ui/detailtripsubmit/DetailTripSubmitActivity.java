package com.example.admin.ridesharemobileclient.ui.detailtripsubmit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.request.DriverRequest;
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

public class DetailTripSubmitActivity extends AppCompatActivity implements View.OnClickListener {
    private IAPIHelper mIAPIHelper;

    private TextView tvType, tvStartPosition, tvEndPosition, tvDate, tvTime, tvVehicle, tvNumberSeat, tvPrice, tvNote, tvMap,
            tvName, tvSave;
    private ImageView ivBack, ivEditDate, ivEditTime, ivEditOther, ivHideEditOther;
    private CircleImageView civImage;
    private LinearLayout llEditOther, llShowOther;
    private EditText etNumberSeat, etPrice, etNote;
    private ProgressDialog mProgressDialog;

    private String time;
    private DetailTripRespone mDetailTripRespone;

    private SimpleDateFormat mDateFormat, mTimeFormat;
    private String idDriver, idHitchhiker, type;

    private static final String TAG = "DetailTripSubmitActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trip_submit);

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
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());

                        Type type = new TypeToken<DetailTripRespone>() {
                        }.getType();
                        mDetailTripRespone = new Gson().fromJson(response.body().getMetadata().toString(), type);

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

            Picasso.get().load(PREFIX_IMAGE_ADDRESS + mDetailTripRespone.getAvatar()).
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
            switch (mDetailTripRespone.getTypeVehicle()) {
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        tvMap.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivEditDate.setOnClickListener(this);
        ivEditTime.setOnClickListener(this);
        ivEditOther.setOnClickListener(this);
        ivHideEditOther.setOnClickListener(this);
        tvSave.setOnClickListener(this);
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
            tvSave = findViewById(R.id.tvSave);
            civImage = findViewById(R.id.civImage);
            ivBack = findViewById(R.id.ivBack);
            ivEditDate = findViewById(R.id.ivEditDate);
            ivEditTime = findViewById(R.id.ivEditTime);
            ivEditOther = findViewById(R.id.ivEditOther);
            ivHideEditOther = findViewById(R.id.ivHideEditOther);
            llEditOther = findViewById(R.id.llEditOther);
            llShowOther = findViewById(R.id.llShowOther);
            etNumberSeat = findViewById(R.id.etNumberSeat);
            etPrice = findViewById(R.id.etPrice);
            etNote = findViewById(R.id.etNote);
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
                case R.id.tvSave:
                    handleSave();
                    break;
                case R.id.ivBack:
                    finish();
                    break;
                case R.id.ivEditDate:
                    handleDateSelect();
                    break;
                case R.id.ivEditTime:
                    handlTimeSelect();
                    break;
                case R.id.ivEditOther:
                    etPrice.setText(tvPrice.getText().toString());
                    etNumberSeat.setText(tvNumberSeat.getText().toString());
                    etNote.setText(tvNote.getText().toString());

                    llEditOther.setVisibility(View.VISIBLE);
                    llShowOther.setVisibility(View.GONE);
                    break;
                case R.id.ivHideEditOther:
                    tvPrice.setText(etPrice.getText().toString());
                    tvNumberSeat.setText(etNumberSeat.getText().toString());
                    tvNote.setText(etNote.getText().toString());

                    llEditOther.setVisibility(View.GONE);
                    llShowOther.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSave() {
        //set time
        time = tvDate.getText().toString() + "T" + tvTime.getText().toString();

        mProgressDialog.show();

        if (type.equals(DATA_DRIVER)) {
            handleUpdateDriver();
        } else if (type.equals(DATA_HITCHHIKER)) {
            handleUpdateHitchhiker();
        }
    }

    private void handleUpdateHitchhiker() {
    }

    private void handleUpdateDriver() {
        try {
            DriverRequest driverRequest = new DriverRequest();
            driverRequest.setTripId(mDetailTripRespone.getDriverId());
            driverRequest.setTime(time);
            driverRequest.setNumberSeat(tvNumberSeat.getText().toString());
            driverRequest.setPrice(tvPrice.getText().toString());
            driverRequest.setNote(tvNote.getText().toString());

            Call<BaseRespone> call = APIHelper.getInstance().updateDriver(App.sToken, driverRequest);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());

                        Toast.makeText(DetailTripSubmitActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());

                    Toast.makeText(DetailTripSubmitActivity.this, "Lỗi nhật thông tin", Toast.LENGTH_SHORT).show();
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
            }, hourOfDay, minute , true);

            timePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
