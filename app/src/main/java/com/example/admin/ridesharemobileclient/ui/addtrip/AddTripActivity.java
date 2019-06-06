package com.example.admin.ridesharemobileclient.ui.addtrip;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.entity.Driver;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.example.admin.ridesharemobileclient.entity.RouteStep;
import com.example.admin.ridesharemobileclient.entity.request.DriverRequest;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.ui.routestep.RouteStepActivity;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.api.optimization.v1.models.OptimizationWaypoint;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_ROUTE_STEP;
import static com.example.admin.ridesharemobileclient.config.Const.UPDATE_DRIVER_SUBMIT;

public class AddTripActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvStartPosition, tvEndPosition, tvRouteStep, tvDate, tvTime, tvAdd;
    private ImageView ivEditStartPosition, ivEditEndPosition, ivEditRouteStep, ivEditDate, ivEditTime, ivBack;
    private RadioButton rbVehicle0, rbVehicle1, rbVehicle2, rbType0, rbType1;
    private EditText etNumberSeat, etPrice, etNote;
    private ProgressDialog mProgressDialog;
    private LinearLayout llVehicle;
    private RelativeLayout rlRoute;
    private View vRoute;

    private SimpleDateFormat mDateFormat, mTimeFormat;
    private String startLat, endLat, startLng, endLng, typeVehicle, time;
    private ArrayList<RouteStep> mListRouteStep;
    private MapboxOptimization mOptimizedClient;

    private static final int REQUEST_SEARCH_START = 1;
    private static final int REQUEST_SEARCH_END = 2;
    private static final int REQUEST_ROUTE_STEP = 3;
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
        ivEditRouteStep.setOnClickListener(this);
        ivEditDate.setOnClickListener(this);
        ivEditTime.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        rbType1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llVehicle.setVisibility(View.GONE);
                    rlRoute.setVisibility(View.GONE);
                    vRoute.setVisibility(View.GONE);
                }
                else {
                    llVehicle.setVisibility(View.VISIBLE);
                    rlRoute.setVisibility(View.VISIBLE);
                    vRoute.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void init() {
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mTimeFormat = new SimpleDateFormat("hh:mm:ss");
        mListRouteStep = new ArrayList<>();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Đang xử lý");
    }

    private void initView() {
        tvStartPosition = findViewById(R.id.tvStartPosition);
        tvEndPosition = findViewById(R.id.tvEndPosition);
        tvRouteStep = findViewById(R.id.tvRouteStep);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvAdd = findViewById(R.id.tvAdd);
        etNumberSeat = findViewById(R.id.etNumberSeat);
        etPrice = findViewById(R.id.etPrice);
        etNote = findViewById(R.id.etNote);
        ivEditStartPosition = findViewById(R.id.ivEditStartPosition);
        ivEditEndPosition = findViewById(R.id.ivEditEndPosition);
        ivEditRouteStep = findViewById(R.id.ivEditRouteStep);
        ivEditDate = findViewById(R.id.ivEditDate);
        ivEditTime = findViewById(R.id.ivEditTime);
        ivBack = findViewById(R.id.ivBack);
        rbVehicle0 = findViewById(R.id.rbVehicle0);
        rbVehicle1 = findViewById(R.id.rbVehicle1);
        rbVehicle2 = findViewById(R.id.rbVehicle2);
        rbType0 = findViewById(R.id.rbType0);
        rbType1 = findViewById(R.id.rbType1);
        llVehicle = findViewById(R.id.llVehicle);
        rlRoute = findViewById(R.id.rlRoute);
        rlRoute = findViewById(R.id.rlRoute);
        vRoute = findViewById(R.id.vRoute);
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
            case R.id.ivEditRouteStep: {
                Intent intent = new Intent(this, RouteStepActivity.class);
                intent.putExtra(KEY_ROUTE_STEP, mListRouteStep);
                startActivityForResult(intent, REQUEST_ROUTE_STEP);
                break;
            }
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

                        EventBus.getDefault().post(UPDATE_DRIVER_SUBMIT);
                        finish();
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
                        Type type = new TypeToken<Driver>() {
                        }.getType();

                        Driver driver = new Gson().fromJson(response.body().getMetadata().toString(), type);
                        saveRouteStep(driver);
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

    private void saveRouteStep(Driver driver) {
        DriverRequest driverRequest = new DriverRequest();
        driverRequest.setDriverId(driver.getId());
        driverRequest.setRouteStep(new Gson().toJson(mListRouteStep));

        Call<BaseRespone> call = APIHelper.getInstance().updateDriver(App.sToken, driverRequest);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                try {
                    Toast.makeText(AddTripActivity.this, "Thêm chuyến đi thành công", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                    EventBus.getDefault().post(UPDATE_DRIVER_SUBMIT);
                    finish();
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
        } catch (Exception e) {
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
//        else if (requestCode == REQUEST_ROUTE_STEP) {
//            mListRouteStep = (ArrayList<RouteStep>) data.getSerializableExtra(KEY_ROUTE_STEP);
//            if (mListRouteStep != null) {
//                sortRouteStep();
//            }
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mListRouteStep = (ArrayList<RouteStep>) intent.getSerializableExtra(KEY_ROUTE_STEP);
        if (mListRouteStep != null && mListRouteStep.size() > 0) {
            sortRouteStep();
        }
    }

    private void sortRouteStep() {
        //thêm điểm đầu và điểm cuối
        if (!TextUtils.isEmpty(startLat) && !TextUtils.isEmpty(startLng) && !TextUtils.isEmpty(endLat) && !TextUtils.isEmpty(endLng)) {
            mListRouteStep.add(0, new RouteStep(startLng, startLat));
            mListRouteStep.add(new RouteStep(endLng, endLat));
        }

        List<Point> coordinates = new ArrayList<>();
        for (RouteStep routeStep : mListRouteStep) {
            Point point = Point.fromLngLat(Double.parseDouble(routeStep.getLongitude()),
                    Double.parseDouble(routeStep.getLatitude()));

            coordinates.add(point);
        }
        mOptimizedClient = MapboxOptimization.builder()
                .source("first")
                .destination("last")
                .coordinates(coordinates)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.map_token))
                .build();

        mOptimizedClient.enqueueCall(new Callback<OptimizationResponse>() {
            @Override
            public void onResponse(Call<OptimizationResponse> call, Response<OptimizationResponse> response) {
                List<OptimizationWaypoint> listOptimization = response.body().waypoints();

                for (OptimizationWaypoint waypoint : listOptimization) {
                    RouteStep routeStep = new RouteStep();
                    routeStep.setLongitude(String.valueOf(waypoint.location().longitude()));
                    routeStep.setLatitude(String.valueOf(waypoint.location().latitude()));

                    mListRouteStep.set(waypoint.waypointIndex(), routeStep);
                }

                //xóa điểm đầu và điểm cuối
                if (!TextUtils.isEmpty(startLat) && !TextUtils.isEmpty(startLng) && !TextUtils.isEmpty(endLat) && !TextUtils.isEmpty(endLng)) {
                    mListRouteStep.remove(mListRouteStep.size() - 1);
                    mListRouteStep.remove(0);
                }

                //hiển thị
                PlaceUtils.setListNamePosition(mListRouteStep, tvRouteStep, mProgressDialog);
            }

            @Override
            public void onFailure(Call<OptimizationResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOptimizedClient != null) {
            mOptimizedClient.cancelCall();
        }
    }
}
