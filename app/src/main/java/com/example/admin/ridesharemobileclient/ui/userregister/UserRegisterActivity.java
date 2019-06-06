package com.example.admin.ridesharemobileclient.ui.userregister;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.HeaderRegister;
import com.example.admin.ridesharemobileclient.entity.Separate;
import com.example.admin.ridesharemobileclient.entity.request.AcceptRequest;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.respone.DriverUserAccept;
import com.example.admin.ridesharemobileclient.entity.respone.DriverUserNotAccept;
import com.example.admin.ridesharemobileclient.entity.respone.DriverUserRespone;
import com.example.admin.ridesharemobileclient.entity.respone.HitchhikerUserAccept;
import com.example.admin.ridesharemobileclient.entity.respone.HitchhikerUserNotAccept;
import com.example.admin.ridesharemobileclient.entity.respone.HitchhikerUserRespone;
import com.example.admin.ridesharemobileclient.ui.common.ItemSeparate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.ACCEPT;
import static com.example.admin.ridesharemobileclient.config.Const.DATA_DRIVER;
import static com.example.admin.ridesharemobileclient.config.Const.DATA_HITCHHIKER;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;
import static com.example.admin.ridesharemobileclient.config.Const.NOT_ACCEPT;

public class UserRegisterActivity extends AppCompatActivity {
    private IAPIHelper mIAPIHelper;

    private RecyclerView rvUserRegister;
    private ImageView ivBack;

    private MultiTypeAdapter mAdapter;
    private Items mItems;
    private String idDriver, idHitchhiker, type;
    private ProgressDialog mProgressDialog;

    private static final String TAG = "UserRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

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
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Đang xử lý");
        mAdapter = new MultiTypeAdapter();
        mItems = new Items();

        mAdapter.register(HeaderRegister.class, new ItemHeaderBinder());
        mAdapter.register(Separate.class, new ItemSeparate());

        mAdapter.register(DriverUserAccept.class, new ItemDriverUserAcceptBinder(new ItemDriverUserAcceptBinder.CallBack() {
            @Override
            public void onCancel(String idUserRegister) {
                handleDriverCancel(idUserRegister);
            }
        }));

        mAdapter.register(DriverUserNotAccept.class, new ItemDriverUserNotAcceptBinder(new ItemDriverUserNotAcceptBinder.CallBack() {
            @Override
            public void onAccept(String idUserRegister) {
                handleDriverAccept(idUserRegister);
            }

            @Override
            public void onCancel(String idUserRegister) {
                handleDriverCancel(idUserRegister);
            }
        }));

        mAdapter.register(HitchhikerUserAccept.class, new ItemHitchhikerUserAcceptBinder(new ItemHitchhikerUserAcceptBinder.CallBack() {
            @Override
            public void onCancel(String idUserRegister) {
                handleHitchhikerCancel(idUserRegister);
            }
        }));

        mAdapter.register(HitchhikerUserNotAccept.class, new ItemHitchhikerUserNotAcceptBinder(new ItemHitchhikerUserNotAcceptBinder.CallBack() {
            @Override
            public void onAccept(String idUserRegister) {
                handleHitchhikerAccept(idUserRegister);
            }

            @Override
            public void onCancel(String idUserRegister) {
                handleHitchhikerCancel(idUserRegister);
            }
        }));


        rvUserRegister.setLayoutManager(new LinearLayoutManager(this));
        rvUserRegister.setAdapter(mAdapter);

        if (type.equals(DATA_DRIVER)) {
            initDriver();
        } else if (type.equals(DATA_HITCHHIKER)) {
            initHitchhiker();
        }
    }

    private void handleHitchhikerCancel(String idUserRegister) {
        try {
            AcceptRequest acceptRequest = new AcceptRequest(idUserRegister, NOT_ACCEPT);
            mProgressDialog.show();

            Call<BaseRespone> call = mIAPIHelper.hitchhikerAccept(App.sToken, Long.parseLong(idHitchhiker), acceptRequest);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        mItems.clear();
                        initHitchhiker();

                        Toast.makeText(getBaseContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
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

    private void handleDriverCancel(String idUserRegister) {
        try {
            List<AcceptRequest> listAccept = new ArrayList<>();
            listAccept.add(new AcceptRequest(idUserRegister, NOT_ACCEPT));
            mProgressDialog.show();

            Call<BaseRespone> call = mIAPIHelper.driverAccept(App.sToken, Long.parseLong(idDriver), listAccept);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        mItems.clear();
                        initDriver();

                        Toast.makeText(getBaseContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
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

    private void handleHitchhikerAccept(String idUserRegister) {
        try {
            AcceptRequest acceptRequest = new AcceptRequest(idUserRegister, ACCEPT);
            mProgressDialog.show();

            //accept
            Call<BaseRespone> callRegister = mIAPIHelper.hitchhikerAccept(App.sToken, Long.parseLong(idHitchhiker), acceptRequest);
            callRegister.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        mItems.clear();
                        initHitchhiker();
                        Toast.makeText(getBaseContext(), "Đã chấp nhận", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });

            //update route step
//            Call<BaseRespone> callRouteStep = mIAPIHelper.getRouteStep(App.sToken, idDriver);
//            callRouteStep.enqueue(new Callback<BaseRespone>() {
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
//                    try {
//                        Type type = new TypeToken<RouteStepResponse>() {
//                        }.getType();
//                        RouteStepResponse stepResponse = new Gson().fromJson(response.body().getMetadata().toString(), type);
//
//                        showRouteStep(stepResponse);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<BaseRespone> call, Throwable t) {
//
//                }
//            });
//            MapboxOptimization optimizedClient = MapboxOptimization.builder()
//                    .coordinates()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDriverAccept(String idUserRegister) {
        try {
            List<AcceptRequest> listAccept = new ArrayList<>();
            listAccept.add(new AcceptRequest(idUserRegister, ACCEPT));
            mProgressDialog.show();

            //accept
            Call<BaseRespone> callRegister = mIAPIHelper.driverAccept(App.sToken, Long.parseLong(idDriver), listAccept);
            callRegister.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        mItems.clear();
                        initDriver();
                        Toast.makeText(getBaseContext(), "Đã chấp nhận", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<BaseRespone> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });

            //update route step
//            Call<BaseRespone> callRouteStep = mIAPIHelper.getRouteStep(App.sToken, idDriver);
//            callRouteStep.enqueue(new Callback<BaseRespone>() {
//                @SuppressLint("LongLogTag")
//                @Override
//                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
//                    try {
//                        Type type = new TypeToken<RouteStepResponse>() {
//                        }.getType();
//                        RouteStepResponse stepResponse = new Gson().fromJson(response.body().getMetadata().toString(), type);
//
//                        showRouteStep(stepResponse);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<BaseRespone> call, Throwable t) {
//
//                }
//            });
//            MapboxOptimization optimizedClient = MapboxOptimization.builder()
//                    .coordinates()
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHitchhiker() {
        try {
            Call<BaseRespone> call = mIAPIHelper.getUserRegisterHitchhiker(App.sToken, idHitchhiker);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken< List<HitchhikerUserRespone>>(){}.getType();
                        List<HitchhikerUserRespone> listUser = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        handleSortListHitchhiker(listUser);
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

    private void initDriver() {
        try {
            Call<BaseRespone> call = mIAPIHelper.getUserRegisterDriver(App.sToken, idDriver);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken< List<DriverUserRespone>>(){}.getType();
                        List<DriverUserRespone> listUser = new Gson().fromJson(response.body().getMetadata().toString(), type);

                        handleSortListDriver(listUser);
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

    private void handleSortListDriver(List<DriverUserRespone> listUser) {
        try {
            List<DriverUserAccept> listAccept = new ArrayList<>();
            List<DriverUserNotAccept> listNotAccept = new ArrayList<>();

            for (DriverUserRespone user : listUser) {
                if (user.getStatus().equals(ACCEPT)) {
                    DriverUserAccept userAccept = new DriverUserAccept(user);
                    listAccept.add(userAccept);
                }
                else if (user.getStatus().equals(NOT_ACCEPT)) {
                    DriverUserNotAccept userNotAccept = new DriverUserNotAccept(user);
                    listNotAccept.add(userNotAccept);
                }
            }

            if (listAccept.size() > 0) {
                mItems.add(new HeaderRegister("Danh sách đã đăng ký"));
                for (int i = 0 ; i < listAccept.size() ; i++) {
                    mItems.add(listAccept.get(i));

                    if (i < listAccept.size() - 1) {
                        mItems.add(new Separate());
                    }
                }
            }

            if (listNotAccept.size() > 0) {
                mItems.add(new HeaderRegister("Danh sách chờ xác nhận"));
                for (int i = 0 ; i < listNotAccept.size() ; i++) {
                    mItems.add(listNotAccept.get(i));

                    if (i < listNotAccept.size() - 1) {
                        mItems.add(new Separate());
                    }
                }
            }

            mAdapter.setItems(mItems);
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSortListHitchhiker(List<HitchhikerUserRespone> listUser) {
        try {
            List<HitchhikerUserAccept> listAccept = new ArrayList<>();
            List<HitchhikerUserNotAccept> listNotAccept = new ArrayList<>();

            for (HitchhikerUserRespone user : listUser) {
                if (user.getStatus().equals(ACCEPT)) {
                    HitchhikerUserAccept userAccept = new HitchhikerUserAccept(user);
                    listAccept.add(userAccept);
                }
                else if (user.getStatus().equals(NOT_ACCEPT)) {
                    HitchhikerUserNotAccept userNotAccept = new HitchhikerUserNotAccept(user);
                    listNotAccept.add(userNotAccept);
                }
            }

            if (listAccept.size() > 0) {
                mItems.add(new HeaderRegister("Danh sách đã đăng ký"));
                for (int i = 0 ; i < listAccept.size() ; i++) {
                    mItems.add(listAccept.get(i));

                    if (i < listAccept.size() - 1) {
                        mItems.add(new Separate());
                    }
                }
            }

            if (listNotAccept.size() > 0) {
                mItems.add(new HeaderRegister("Danh sách chờ xác nhận"));
                for (int i = 0 ; i < listNotAccept.size() ; i++) {
                    mItems.add(listNotAccept.get(i));

                    if (i < listNotAccept.size() - 1) {
                        mItems.add(new Separate());
                    }
                }
            }

            mAdapter.setItems(mItems);
            mAdapter.notifyDataSetChanged();
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
        rvUserRegister = findViewById(R.id.rvUserRegister);
        ivBack = findViewById(R.id.ivBack);
    }
}
