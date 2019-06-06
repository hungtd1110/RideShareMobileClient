package com.example.admin.ridesharemobileclient.ui.detailtripsubmit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.RouteStep;
import com.example.admin.ridesharemobileclient.entity.request.DriverRequest;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.respone.DetailDriverRespone;
import com.example.admin.ridesharemobileclient.entity.respone.DetailHitchhikerRespone;
import com.example.admin.ridesharemobileclient.entity.respone.RouteStepResponse;
import com.example.admin.ridesharemobileclient.ui.mapdetail.MapDetailActivity;
import com.example.admin.ridesharemobileclient.ui.userregister.UserRegisterActivity;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.optimization.v1.MapboxOptimization;
import com.mapbox.api.optimization.v1.models.OptimizationResponse;
import com.mapbox.api.optimization.v1.models.OptimizationWaypoint;
import com.mapbox.geojson.Point;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.DATA_DRIVER;
import static com.example.admin.ridesharemobileclient.config.Const.DATA_HITCHHIKER;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ROUTE_STEP;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;
import static com.example.admin.ridesharemobileclient.config.Const.PREFIX_IMAGE_ADDRESS;
import static com.example.admin.ridesharemobileclient.config.Const.UPDATE_DRIVER_SUBMIT;
import static com.example.admin.ridesharemobileclient.config.Const.UPDATE_HITCHHIKER_SUBMIT;

public class DetailTripSubmitActivity extends AppCompatActivity implements View.OnClickListener {
    private IAPIHelper mIAPIHelper;

    private TextView tvType, tvStartPosition, tvEndPosition, tvRouteStep, tvDate, tvTime, tvVehicle, tvNumberSeat, tvPrice, tvNote, tvMap,
            tvName, tvSave;
    private ImageView ivBack, ivEditDate, ivEditTime, ivEditOther, ivHideEditOther, ivMore;
    private CircleImageView civImage;
    private LinearLayout llEditOther, llShowOther, llVehicle;
    private EditText etNumberSeat, etPrice, etNote;
    private RelativeLayout rlRoute;
    private View vRoute;

    private ProgressDialog mProgressDialog;
    private String time;
    private DetailDriverRespone mDetailDriverRespone;
    private DetailHitchhikerRespone mDetailHitchhikerRespone;
    private SimpleDateFormat mDateFormat, mTimeFormat;
    private String idDriver, idHitchhiker, type;
    private MapboxOptimization mOptimizedClient;
    private ArrayList<RouteStep> mListRouteStep = new ArrayList<>();

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
        try {
            mIAPIHelper = APIHelper.getInstance();

            Call<BaseRespone> call = mIAPIHelper.getHitchhiker(App.sToken, idHitchhiker);
            call.enqueue(new Callback<BaseRespone>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());

                        Type type = new TypeToken<DetailHitchhikerRespone>() {
                        }.getType();
                        mDetailHitchhikerRespone = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        showInforHitchhiker();
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

    private void showInforHitchhiker() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(mDetailHitchhikerRespone.getTime()));

            Picasso.get().load(PREFIX_IMAGE_ADDRESS + mDetailHitchhikerRespone.getAvatar()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(civImage);
            tvName.setText(mDetailHitchhikerRespone.getUsername());
            tvType.setText(getString(R.string.type0));
            PlaceUtils.setFullNamePosition(mDetailHitchhikerRespone.getStartLatitude(), mDetailHitchhikerRespone.getStartLongitude(), tvStartPosition);
            PlaceUtils.setFullNamePosition(mDetailHitchhikerRespone.getEndLatitude(), mDetailHitchhikerRespone.getEndLongitude(), tvEndPosition);
            tvDate.setText(mDateFormat.format(calendar.getTime()));
            tvTime.setText(mTimeFormat.format(calendar.getTime()));

            llVehicle.setVisibility(View.GONE);

            tvNumberSeat.setText(mDetailHitchhikerRespone.getNumberSeat());
            tvPrice.setText(mDetailHitchhikerRespone.getPrice());
            tvNote.setText(mDetailHitchhikerRespone.getNote());

            rlRoute.setVisibility(View.GONE);
            vRoute.setVisibility(View.GONE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void getInfoDriver() {
        try {
            mIAPIHelper = APIHelper.getInstance();

            Call<BaseRespone> callDriver = mIAPIHelper.getDriver(App.sToken, idDriver);
            callDriver.enqueue(new Callback<BaseRespone>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken<DetailDriverRespone>() {
                        }.getType();
                        mDetailDriverRespone = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        showInforDriver();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {

                }
            });

            Call<BaseRespone> callRouteStep = mIAPIHelper.getRouteStep(App.sToken, idDriver);
            callRouteStep.enqueue(new Callback<BaseRespone>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken<List<RouteStepResponse>>() {
                        }.getType();
                        List<RouteStepResponse> listRouteStepResponse = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        showRouteStep(listRouteStepResponse);
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

    private void showRouteStep(List<RouteStepResponse> listRouteStepResponse) {
        if (listRouteStepResponse != null) {
            mListRouteStep.clear();
            Type type = new TypeToken<ArrayList<RouteStep>>() {
            }.getType();

            for (RouteStepResponse routeStepResponse : listRouteStepResponse) {
                mListRouteStep.addAll(new Gson().fromJson(routeStepResponse.getSteps(), type));
            }

            sortRouteStep();
        }
    }

    private void sortRouteStep() {
        //thêm điểm đầu và điểm cuối
        mListRouteStep.add(0, new RouteStep(mDetailDriverRespone.getStartLongitude(),
                mDetailDriverRespone.getStartLatitude()));
        mListRouteStep.add(new RouteStep(mDetailDriverRespone.getEndLongitude(),
                mDetailDriverRespone.getEndLatitude()));

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
                mListRouteStep.remove(mListRouteStep.size() - 1);
                mListRouteStep.remove(0);

                PlaceUtils.setListNamePosition(mListRouteStep, tvRouteStep, mProgressDialog);
            }

            @Override
            public void onFailure(Call<OptimizationResponse> call, Throwable t) {

            }
        });
    }

    private void showInforDriver() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(mDetailDriverRespone.getTime()));

            Picasso.get().load(PREFIX_IMAGE_ADDRESS + mDetailDriverRespone.getAvatar()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(civImage);
            tvName.setText(mDetailDriverRespone.getUsername());
            tvType.setText(getString(R.string.type0));
            PlaceUtils.setFullNamePosition(mDetailDriverRespone.getStartLatitude(), mDetailDriverRespone.getStartLongitude(), tvStartPosition);
            PlaceUtils.setFullNamePosition(mDetailDriverRespone.getEndLatitude(), mDetailDriverRespone.getEndLongitude(), tvEndPosition);
            tvDate.setText(mDateFormat.format(calendar.getTime()));
            tvTime.setText(mTimeFormat.format(calendar.getTime()));

            String vehicle = "";
            switch (mDetailDriverRespone.getTypeVehicle()) {
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

            tvNumberSeat.setText(mDetailDriverRespone.getNumberSeat());
            tvPrice.setText(mDetailDriverRespone.getPrice());
            tvNote.setText(mDetailDriverRespone.getNote());

            rlRoute.setVisibility(View.VISIBLE);
            vRoute.setVisibility(View.VISIBLE);
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
        ivMore.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    private void initView() {
        try {
            tvType = findViewById(R.id.tvType);
            tvStartPosition = findViewById(R.id.tvStartPosition);
            tvEndPosition = findViewById(R.id.tvEndPosition);
            tvRouteStep = findViewById(R.id.tvRouteStep);
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
            ivMore = findViewById(R.id.ivMore);
            llEditOther = findViewById(R.id.llEditOther);
            llShowOther = findViewById(R.id.llShowOther);
            llVehicle = findViewById(R.id.llVehicle);
            etNumberSeat = findViewById(R.id.etNumberSeat);
            etPrice = findViewById(R.id.etPrice);
            etNote = findViewById(R.id.etNote);
            rlRoute = findViewById(R.id.rlRoute);
            rlRoute = findViewById(R.id.rlRoute);
            vRoute = findViewById(R.id.vRoute);
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

                    //thêm điểm đầu và điểm cuối
                    mListRouteStep.add(0, new RouteStep(mDetailDriverRespone.getStartLongitude(),
                            mDetailDriverRespone.getStartLatitude()));
                    mListRouteStep.add(new RouteStep(mDetailDriverRespone.getEndLongitude(),
                            mDetailDriverRespone.getEndLatitude()));

                    intent.putExtra(KEY_ROUTE_STEP, mListRouteStep);
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
                case R.id.ivMore:
                    ActionTripSubmitDialog dialog = new ActionTripSubmitDialog(new ActionTripSubmitDialog.CallBack() {
                        @Override
                        public void onListPeople() {
                            Intent intent = new Intent(getBaseContext(), UserRegisterActivity.class);

                            if (type.equals(DATA_DRIVER)) {
                                intent.putExtra(KEY_TYPE, DATA_DRIVER);
                                intent.putExtra(KEY_ID, idDriver);
                            } else if (type.equals(DATA_HITCHHIKER)) {
                                intent.putExtra(KEY_TYPE, DATA_HITCHHIKER);
                                intent.putExtra(KEY_ID, idHitchhiker);
                            }

                            startActivity(intent);
                        }

                        @Override
                        public void onDelete() {
                            if (type.equals(DATA_DRIVER)) {
                                handleDeleteDriver();
                            } else if (type.equals(DATA_HITCHHIKER)) {
                                handleDeleteHitchhiker();
                            }
                        }
                    });

                    dialog.show(getSupportFragmentManager(), null);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDeleteHitchhiker() {
        mProgressDialog.show();

        Call<BaseRespone> call = mIAPIHelper.deleteHitchhiker(App.sToken, idHitchhiker);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                Toast.makeText(getBaseContext(), "Đã hủy chuyến đi", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();

                EventBus.getDefault().post(UPDATE_HITCHHIKER_SUBMIT);
                finish();
            }

            @Override
            public void onFailure(Call<BaseRespone> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Lỗi huỷ chuyến đi", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    private void handleDeleteDriver() {
        mProgressDialog.show();

        Call<BaseRespone> call = mIAPIHelper.deleteDriver(App.sToken, idDriver);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                Toast.makeText(getBaseContext(), "Đã hủy chuyến đi", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();

                EventBus.getDefault().post(UPDATE_DRIVER_SUBMIT);
                finish();
            }

            @Override
            public void onFailure(Call<BaseRespone> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Lỗi huỷ chuyến đi", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
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
            driverRequest.setDriverId(mDetailDriverRespone.getDriverId());
            driverRequest.setTime(time);
            driverRequest.setNumberSeat(tvNumberSeat.getText().toString());
            driverRequest.setPrice(tvPrice.getText().toString());
            driverRequest.setNote(tvNote.getText().toString());

            Call<BaseRespone> call = APIHelper.getInstance().updateDriver(App.sToken, driverRequest);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
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
            }, hourOfDay, minute, true);

            timePickerDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOptimizedClient != null) {
            mOptimizedClient.cancelCall();
        }
    }
}
