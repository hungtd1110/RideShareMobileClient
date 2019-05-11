package com.example.admin.ridesharemobileclient.ui.userregister;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.HeaderRegister;
import com.example.admin.ridesharemobileclient.entity.Separate;
import com.example.admin.ridesharemobileclient.entity.request.AcceptRequest;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.respone.UserAccept;
import com.example.admin.ridesharemobileclient.entity.respone.UserNotAccept;
import com.example.admin.ridesharemobileclient.entity.respone.UserRespone;
import com.example.admin.ridesharemobileclient.ui.common.ItemSeparate;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
import static com.example.admin.ridesharemobileclient.config.Const.CANCEL;
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
        mAdapter = new MultiTypeAdapter();
        mItems = new Items();

        mAdapter.register(HeaderRegister.class, new ItemHeaderBinder());
        mAdapter.register(Separate.class, new ItemSeparate());

        mAdapter.register(UserAccept.class, new ItemUserAcceptBinder(new ItemUserAcceptBinder.CallBack() {
            @Override
            public void onCancel(String idTrip, String idUserRegister) {
                if (type.equals(DATA_DRIVER)) {
                    handleDriverCancel(idTrip, idUserRegister);
                } else if (type.equals(DATA_HITCHHIKER)) {
                    handleHitchhikerCancel(idTrip, idUserRegister);
                }
            }
        }));

        mAdapter.register(UserNotAccept.class, new ItemUserNotAcceptBinder(new ItemUserNotAcceptBinder.CallBack() {
            @Override
            public void onAccept(String idTrip, String idUserRegister) {
                if (type.equals(DATA_DRIVER)) {
                    handleDriverAccept(idTrip, idUserRegister);
                } else if (type.equals(DATA_HITCHHIKER)) {
                    handleHitchhikerAccept(idTrip, idUserRegister);
                }
            }

            @Override
            public void onCancel(String idTrip, String idUserRegister) {
                if (type.equals(DATA_DRIVER)) {
                    handleDriverCancel(idTrip, idUserRegister);
                } else if (type.equals(DATA_HITCHHIKER)) {
                    handleHitchhikerCancel(idTrip, idUserRegister);
                }
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

    private void handleHitchhikerCancel(String idTrip, String idUserRegister) {
    }

    private void handleDriverCancel(String idTrip, String idUserRegister) {
        try {
            List<AcceptRequest> listAccept = new ArrayList<>();
            listAccept.add(new AcceptRequest(idUserRegister, CANCEL));
            Call<BaseRespone> call = mIAPIHelper.driverAccept(App.sToken, idTrip, listAccept);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());
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

    private void handleHitchhikerAccept(String idTrip, String idUserRegister) {
    }

    private void handleDriverAccept(String idTrip, String idUserRegister) {
        try {
            List<AcceptRequest> listAccept = new ArrayList<>();
            listAccept.add(new AcceptRequest(idUserRegister, ACCEPT));
            Call<BaseRespone> call = mIAPIHelper.driverAccept(App.sToken, idTrip, listAccept);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Log.d(TAG, "onResponse: " + response.body().getMetadata().toString());
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

                        Type type = new TypeToken< List<UserRespone>>(){}.getType();
                        List<UserRespone> listUser = new Gson().fromJson(response.body().getMetadata().toString(), type);

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

    private void handleSortList(List<UserRespone> listUser) {
        try {
            List<UserAccept> listAccept = new ArrayList<>();
            List<UserNotAccept> listNotAccept = new ArrayList<>();

            for (UserRespone user : listUser) {
                if (user.getStatus().equals(ACCEPT)) {
                    UserAccept userAccept = new UserAccept(user);
                    listAccept.add(userAccept);
                }
                else if (user.getStatus().equals(NOT_ACCEPT)) {
                    UserNotAccept userNotAccept = new UserNotAccept(user);
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
//                mItems.addAll(listAccept);
            }

            if (listNotAccept.size() > 0) {
                mItems.add(new HeaderRegister("Danh sách chờ xác nhận"));
                for (int i = 0 ; i < listNotAccept.size() ; i++) {
                    mItems.add(listNotAccept.get(i));

                    if (i < listNotAccept.size() - 1) {
                        mItems.add(new Separate());
                    }
                }
//                mItems.addAll(listNotAccept);
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
