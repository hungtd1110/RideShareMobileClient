package com.example.admin.ridesharemobileclient.ui.detailtripregister;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.RouteStep;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.respone.CheckRegisterDriver;
import com.example.admin.ridesharemobileclient.entity.respone.CheckRegisterHitchhiker;
import com.example.admin.ridesharemobileclient.entity.respone.DetailDriverRespone;
import com.example.admin.ridesharemobileclient.entity.respone.DetailHitchhikerRespone;
import com.example.admin.ridesharemobileclient.entity.respone.RouteStepResponse;
import com.example.admin.ridesharemobileclient.ui.detailmessage.DetailMessageActivity;
import com.example.admin.ridesharemobileclient.ui.mapdetail.MapDetailActivity;
import com.example.admin.ridesharemobileclient.ui.profile.ProfileActivity;
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
import com.squareup.picasso.Picasso;

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
import static com.example.admin.ridesharemobileclient.config.Const.KEY_NAME;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_PROFILE;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ROUTE_STEP;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;
import static com.example.admin.ridesharemobileclient.config.Const.PREFIX_IMAGE_ADDRESS;

public class DetailTripRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private IAPIHelper mIAPIHelper = APIHelper.getInstance();

    private TextView tvType, tvStartPosition, tvEndPosition, tvRouteStep, tvDate, tvTime, tvVehicle, tvNumberSeat, tvPrice, tvNote, tvMap,
            tvName, tvRegister, tvStar;
    private ImageView ivBack, ivMessage;
    private CircleImageView civImage;
    private LinearLayout llVehicle;
    private ConstraintLayout cslInformation;
    private RelativeLayout rlRoute;
    private View vRoute;

    private ProgressDialog mProgressDialog;
    private SimpleDateFormat mDateFormat, mTimeFormat;
    private String idDriver, idHitchhiker, type;
    private DetailDriverRespone mDetailDriverRespone;
    private DetailHitchhikerRespone mDetailHitchhikerRespone;
    private boolean checkRegister;
    private MapboxOptimization mOptimizedClient;
    private ArrayList<RouteStep> mListRouteStep = new ArrayList<>();

    private static final String TAG = "DetailTripRegisterActivity";
    private static final int REQUEST = 1;

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
            checkRegisterDriver();
        } else if (type.equals(DATA_HITCHHIKER)) {
            checkRegisterHitchhiker();
        }
    }

    private void checkRegisterHitchhiker() {
        try {
            Call<BaseRespone> call = mIAPIHelper.checkRegisterHitchhiker(App.sToken, idHitchhiker);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken<CheckRegisterHitchhiker>() {
                        }.getType();
                        CheckRegisterHitchhiker check = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        if (check != null) {
                            checkRegister = true;
                        } else {
                            checkRegister = false;
                        }

                        getInfoHitchhiker();
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

    private void checkRegisterDriver() {
        try {
            Call<BaseRespone> call = mIAPIHelper.checkRegisterDriver(App.sToken, idDriver);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken<CheckRegisterDriver>() {
                        }.getType();
                        CheckRegisterDriver check = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        if (check != null) {
                            checkRegister = true;
                        } else {
                            checkRegister = false;
                        }

                        getInfoDriver();
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

    private void getInfoHitchhiker() {
        try {
            mIAPIHelper = APIHelper.getInstance();

            Call<BaseRespone> call = mIAPIHelper.getHitchhiker(App.sToken, idHitchhiker);
            call.enqueue(new Callback<BaseRespone>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken<DetailHitchhikerRespone>() {
                        }.getType();
                        mDetailHitchhikerRespone = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        showInforHitchhiker();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
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

                        if (mDetailDriverRespone.getUserId().equals(App.sUser.getUserId())) {
                            ivMessage.setVisibility(View.GONE);
                        }else {
                            ivMessage.setVisibility(View.VISIBLE);
                        }

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

            Picasso.get().
                    load(PREFIX_IMAGE_ADDRESS + mDetailDriverRespone.getAvatar()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(civImage);
            tvName.setText(mDetailDriverRespone.getUsername());
            tvType.setText(getString(R.string.type0));
            PlaceUtils.setFullNamePosition(mDetailDriverRespone.getStartLatitude(),
                    mDetailDriverRespone.getStartLongitude(),
                    tvStartPosition);
            PlaceUtils.setFullNamePosition(mDetailDriverRespone.getEndLatitude(),
                    mDetailDriverRespone.getEndLongitude(),
                    tvEndPosition);
            tvDate.setText(mDateFormat.format(calendar.getTime()));
            tvTime.setText(mTimeFormat.format(calendar.getTime()));
            tvStar.setText(mDetailDriverRespone.getStar() + "");

            String vehicle = "";
            switch (mDetailDriverRespone.getNumberSeat()) {
                case "0":
                    vehicle = getString(R.string.vehicle0);
                    break;
                case "1":
                    vehicle = getString(R.string.vehicle1);
                    break;
                case "2":
                    vehicle = getString(R.string.vehicle2);
                    break;
                default:
                    vehicle = getString(R.string.vehicle_none);
                    break;
            }
            tvVehicle.setText(vehicle);

            tvNumberSeat.setText(mDetailDriverRespone.getNumberSeat());
            tvPrice.setText(mDetailDriverRespone.getPrice());
            tvNote.setText(mDetailDriverRespone.getNote());

            rlRoute.setVisibility(View.VISIBLE);
            vRoute.setVisibility(View.VISIBLE);

            if (checkRegister || mDetailDriverRespone.getUserId().equals(App.sUser.getUserId())) {
                tvRegister.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInforHitchhiker() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(mDetailHitchhikerRespone.getTime()));

            Picasso.get().
                    load(PREFIX_IMAGE_ADDRESS + mDetailHitchhikerRespone.getAvatar()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(civImage);
            tvName.setText(mDetailHitchhikerRespone.getUsername());
            tvType.setText(getString(R.string.type1));
            PlaceUtils.setFullNamePosition(mDetailHitchhikerRespone.getStartLatitude(),
                    mDetailHitchhikerRespone.getStartLongitude(),
                    tvStartPosition);
            PlaceUtils.setFullNamePosition(mDetailHitchhikerRespone.getEndLatitude(),
                    mDetailHitchhikerRespone.getEndLongitude(),
                    tvEndPosition);
            tvDate.setText(mDateFormat.format(calendar.getTime()));
            tvTime.setText(mTimeFormat.format(calendar.getTime()));
            tvStar.setText(mDetailHitchhikerRespone.getStar() + "");

            llVehicle.setVisibility(View.GONE);

            tvNumberSeat.setText(mDetailHitchhikerRespone.getNumberSeat());
            tvPrice.setText(mDetailHitchhikerRespone.getPrice());
            tvNote.setText(mDetailHitchhikerRespone.getNote());

            rlRoute.setVisibility(View.GONE);
            vRoute.setVisibility(View.GONE);

            if (checkRegister || mDetailHitchhikerRespone.getUserId().equals(App.sUser.getUserId())) {
                tvRegister.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        tvMap.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        cslInformation.setOnClickListener(this);
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
            tvRegister = findViewById(R.id.tvRegister);
            civImage = findViewById(R.id.civImage);
            ivBack = findViewById(R.id.ivBack);
            ivMessage = findViewById(R.id.ivMessage);
            llVehicle = findViewById(R.id.llVehicle);
            cslInformation = findViewById(R.id.cslInformation);
            rlRoute = findViewById(R.id.rlRoute);
            vRoute = findViewById(R.id.vRoute);
            tvStar = findViewById(R.id.tvStar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.tvMap: {
                    Intent intent = new Intent(this, MapDetailActivity.class);

                    //thêm điểm đầu và điểm cuối
                    if (type.equals(DATA_DRIVER)) {
                        mListRouteStep.add(0, new RouteStep(mDetailDriverRespone.getStartLongitude(),
                                mDetailDriverRespone.getStartLatitude()));
                        mListRouteStep.add(new RouteStep(mDetailDriverRespone.getEndLongitude(),
                                mDetailDriverRespone.getEndLatitude()));
                    } else if (type.equals(DATA_HITCHHIKER)) {
                        mListRouteStep.add(0, new RouteStep(mDetailHitchhikerRespone.getStartLongitude(),
                                mDetailHitchhikerRespone.getStartLatitude()));
                        mListRouteStep.add(new RouteStep(mDetailHitchhikerRespone.getEndLongitude(),
                                mDetailHitchhikerRespone.getEndLatitude()));
                    }

                    intent.putExtra(KEY_ROUTE_STEP, mListRouteStep);
                    startActivity(intent);
                    break;
                }
                case R.id.ivBack:
                    finish();
                    break;
                case R.id.ivMessage: {
                    Intent intent = new Intent(this, DetailMessageActivity.class);
                    if (type.equals(DATA_DRIVER)) {
                        intent.putExtra(KEY_ID, mDetailDriverRespone.getUserId());
                        intent.putExtra(KEY_NAME, mDetailDriverRespone.getUsername());
                    } else if (type.equals(DATA_HITCHHIKER)) {
                        intent.putExtra(KEY_ID, mDetailHitchhikerRespone.getUserId());
                        intent.putExtra(KEY_NAME, mDetailHitchhikerRespone.getUsername());
                    }

                    startActivity(intent);
                    break;
                }
                case R.id.tvRegister: {
                    if (type.equals(DATA_DRIVER)) {
                        handleRegisterToDriver();
                    } else if (type.equals(DATA_HITCHHIKER)) {
                        handldeRegisterToHitchhiker();
                    }

                    break;
                }
                case R.id.cslInformation: {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    if (type.equals(DATA_DRIVER)) {
                        intent.putExtra(KEY_PROFILE, mDetailDriverRespone.getDriverId());
                    } else if (type.equals(DATA_HITCHHIKER)) {
                        intent.putExtra(KEY_PROFILE, mDetailHitchhikerRespone.getHitchhikerId());
                    }
                    startActivity(intent);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handldeRegisterToHitchhiker() {
        handleRegisterHitchhiker();
    }

    private void handleRegisterToDriver() {
        RegisterTripDialog dialog = new RegisterTripDialog(this, null, new RegisterTripDialog.CallBack() {
            @Override
            public void onPickPosition() {
                showSelectPick();
            }

            @Override
            public void onRegiser(RouteStep routeStep) {
                if (type.equals(DATA_DRIVER)) {
                    handleRegisterDriver(routeStep);
                } else if (type.equals(DATA_HITCHHIKER)) {
                    handleRegisterHitchhiker();
                }
            }
        });
        dialog.show();
    }

    private void showSelectPick() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(getString(R.string.map_token))
                    .placeOptions(PlaceOptions.builder()
                            .backgroundColor(getResources().getColor(R.color.colorBackground3))
                            .limit(10)
                            .build(PlaceOptions.MODE_CARDS))
                    .build(DetailTripRegisterActivity.this);
            startActivityForResult(intent, REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRegisterHitchhiker() {
        try {
            mProgressDialog.show();

            Call<BaseRespone> call = mIAPIHelper.registerHitchhiker(App.sToken, mDetailHitchhikerRespone.getHitchhikerId());
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
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

    private void handleRegisterDriver(RouteStep routeStep) {
        try {
            mProgressDialog.show();

            Call<BaseRespone> call = mIAPIHelper.registerDriver(App.sToken, mDetailDriverRespone.getDriverId(), routeStep);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    CarmenFeature carmenFeature = PlaceAutocomplete.getPlace(data);
                    double lat = ((Point) carmenFeature.geometry()).latitude();
                    double lng = ((Point) carmenFeature.geometry()).longitude();

                    RouteStep routeStep = new RouteStep();
                    routeStep.setLatitude(String.valueOf(lat));
                    routeStep.setLongitude(String.valueOf(lng));

                    RegisterTripDialog dialog = new RegisterTripDialog(this, routeStep, new RegisterTripDialog.CallBack() {
                        @Override
                        public void onPickPosition() {
                            showSelectPick();
                        }

                        @Override
                        public void onRegiser(RouteStep routeStep) {
                            if (type.equals(DATA_DRIVER)) {
                                handleRegisterDriver(routeStep);
                            } else if (type.equals(DATA_HITCHHIKER)) {
                                handleRegisterHitchhiker();
                            }
                        }
                    });
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
