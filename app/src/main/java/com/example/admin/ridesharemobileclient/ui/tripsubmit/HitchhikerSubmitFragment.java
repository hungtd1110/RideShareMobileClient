package com.example.admin.ridesharemobileclient.ui.tripsubmit;

import android.annotation.SuppressLint;
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
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
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
import static com.example.admin.ridesharemobileclient.config.Const.UPDATE_HITCHHIKER_SUBMIT;

public class HitchhikerSubmitFragment extends Fragment {
    private IAPIHelper mIAPIHelper;

    private View mView;
    private RecyclerView rvHitchhiker;

    private LinearLayoutManager layoutManager;
    private HitchhikerSubmitAdapter adapter;
    private int page, size, visibleThreshold;
    private boolean isLoading;
    private ProgressDialog mProgressDialog;

    private static final String TAG = "HitchhikerSubmitFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_hitchhiker, container, false);

        initView();
        initEvent();
        init();

        return mView;
    }

    private void initEvent() {
//        rvHitchhiker.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
//                        showListHitchhiker(ACTION_ADD_DATA);
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

            adapter = new HitchhikerSubmitAdapter(getContext(), new HitchhikerSubmitAdapter.CallBack() {
                @Override
                public void onCancelHitchhiker(String idHitchhiker) {
                    try {
                        mProgressDialog.show();

                        Call<BaseRespone> call = mIAPIHelper.deleteHitchhiker(App.sToken, idHitchhiker);
                        call.enqueue(new Callback<BaseRespone>() {
                            @Override
                            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                                Toast.makeText(getContext(), "Đã huỷ chuyến đi", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();

                                showListHitchhiker(ACTION_SET_DATA);
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
            rvHitchhiker.setLayoutManager(layoutManager);
            rvHitchhiker.setAdapter(adapter);

            showListHitchhiker(ACTION_SET_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showListHitchhiker(String action) {
        try {
            Map<String, String> maps = new HashMap<>();
//            maps.put("page", String.valueOf(this.page));
//            maps.put("size", String.valueOf(this.size));
            maps.put("page", PAGE);
            maps.put("size", SIZE);

            mProgressDialog.show();
            Call<BaseRespone> call = mIAPIHelper.getListHitchhikerSubmit(App.sToken, maps);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(@NonNull Call<BaseRespone> call, @NonNull Response<BaseRespone> response) {
                    try {
                        Hitchhiker[] hitchhikers = new Gson().fromJson((String) response.body().getMetadata(), Hitchhiker[].class);

                        ArrayList<Hitchhiker> listHitchhiker = new ArrayList<>();
                        Collections.addAll(listHitchhiker, hitchhikers);

                        if (action.equals(ACTION_SET_DATA)) {
                            adapter.setData(listHitchhiker);
                        } else if (action.equals(ACTION_ADD_DATA)) {
                            adapter.loadMore();
                            adapter.addData(listHitchhiker);
                            isLoading = false;
                        }

                        mProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @SuppressLint("LongLogTag")
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
        rvHitchhiker = mView.findViewById(R.id.rvHitchhiker);
    }

    @Subscribe
    public void onEvent(String action) {
        if (action.equals(UPDATE_HITCHHIKER_SUBMIT)) {
            showListHitchhiker(ACTION_SET_DATA);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
