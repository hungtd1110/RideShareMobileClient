package com.example.admin.ridesharemobileclient.ui.tripsubmit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.Driver;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.ACTION_ADD_DATA;
import static com.example.admin.ridesharemobileclient.config.Const.ACTION_SET_DATA;
import static com.example.admin.ridesharemobileclient.config.Const.PAGE;
import static com.example.admin.ridesharemobileclient.config.Const.SIZE;
import static com.example.admin.ridesharemobileclient.config.Const.UPDATE_DRIVER_SUBMIT;

public class DriverSubmitFragment extends Fragment {
    private IAPIHelper mIAPIHelper;

    private View mView;
    private RecyclerView rvDriver;

    private LinearLayoutManager layoutManager;
    private DriverSubmitAdapter adapter;
    private int page, size, visibleThreshold;
    private boolean isLoading;
    private ProgressDialog mProgressDialog;

    private static final String TAG = "DriverSubmitFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_driver, container, false);

        initView();
        initEvent();
        init();

        return mView;
    }

    private void initEvent() {
//        rvDriver.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                int totalItemCount = layoutManager.getItemCount(); // Lấy tổng số lượng item đang có
//                int lastVisibleItem = layoutManager.findLastVisibleItemPosition(); // Lấy vị trí của item cuối cùng
//
//                if (totalItemCount < adapter.getItemCount()) {
//                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) // Nếu không phải trạng thái loading và tổng số lượng item bé hơn hoặc bằng vị trí item cuối + số lượng item tối đa hiển thị
//                    {
//                        isLoading = true;
//                        adapter.loadMore();
//                        page++;
//                        showListDriver(ACTION_ADD_DATA);
//                    }
//                }
//            }
//        });
    }

    private void init() {
        try {
            EventBus.getDefault().register(this);
            mIAPIHelper = APIHelper.getInstance();
            visibleThreshold = 5;
            isLoading = false;
            page = 1;
            size = 10;

            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Đang xử lý");

            adapter = new DriverSubmitAdapter(getContext(), new DriverSubmitAdapter.CallBack() {
                @Override
                public void onCancelDriver(String idDriver) {
                    try {
                        mProgressDialog.show();

                        Call<BaseRespone> call = mIAPIHelper.deleteDriver(App.sToken, idDriver);
                        call.enqueue(new Callback<BaseRespone>() {
                            @Override
                            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                                Toast.makeText(getContext(), "Đã huỷ chuyến đi", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();

                                showListDriver(ACTION_SET_DATA);
                            }

                            @Override
                            public void onFailure(Call<BaseRespone> call, Throwable t) {
                                Toast.makeText(getContext(), "Lỗi huỷ chuyến đi", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvDriver.setLayoutManager(layoutManager);
            rvDriver.setAdapter(adapter);

            showListDriver(ACTION_SET_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showListDriver(String action) {
        try {
            Map<String, String> maps = new HashMap<>();
//            maps.put("page", String.valueOf(this.page));
//            maps.put("size", String.valueOf(this.size));
            maps.put("page", PAGE);
            maps.put("size", SIZE);

            mProgressDialog.show();
            Call<BaseRespone> call = mIAPIHelper.getListDriverSubmit(App.sToken, maps);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(@NonNull Call<BaseRespone> call, @NonNull Response<BaseRespone> response) {
                    try {
                        Driver[] drivers = new Gson().fromJson((String) response.body().getMetadata(), Driver[].class);

                        ArrayList<Driver> listDriver = new ArrayList<>();
                        Collections.addAll(listDriver, drivers);

                        if (action.equals(ACTION_SET_DATA)) {
                            adapter.setData(listDriver);
                        } else if (action.equals(ACTION_ADD_DATA)) {
                            adapter.loadMore();
                            adapter.addData(listDriver);
                            isLoading = false;
                        }

                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseRespone> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        rvDriver = mView.findViewById(R.id.rvDriver);
    }

    @Subscribe
    public void onEvent(String action) {
        if (action.equals(UPDATE_DRIVER_SUBMIT)) {
            showListDriver(ACTION_SET_DATA);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
