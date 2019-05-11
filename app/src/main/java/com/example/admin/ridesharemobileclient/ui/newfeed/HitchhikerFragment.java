package com.example.admin.ridesharemobileclient.ui.newfeed;

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

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.ACTION_ADD_DATA;
import static com.example.admin.ridesharemobileclient.config.Const.ACTION_SET_DATA;

public class HitchhikerFragment extends Fragment {
    private IAPIHelper mIAPIHelper;

    private View mView;
    private RecyclerView rvHitchhiker;

    private LinearLayoutManager layoutManager;
    private HitchhikerAdapter adapter;
    private int page, size, visibleThreshold;
    private boolean isLoading;

    private static final String TAG = "HitchhikerFragment";

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
        try {
            rvHitchhiker.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    try {
                        int totalItemCount = layoutManager.getItemCount(); // Lấy tổng số lượng item đang có
                        int lastVisibleItem = layoutManager.findLastVisibleItemPosition(); // Lấy vị trí của item cuối cùng

                        if (totalItemCount < adapter.getItemCount()) {
                            if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) // Nếu không phải trạng thái loading và tổng số lượng item bé hơn hoặc bằng vị trí item cuối + số lượng item tối đa hiển thị
                            {
                                isLoading = true;
                                adapter.loadMore();
                                page++;
                                showListHitchhiker(ACTION_ADD_DATA);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            mIAPIHelper = APIHelper.getInstance();
            visibleThreshold = 5;
            isLoading = false;
            page = 1;
            size = 10;

            adapter = new HitchhikerAdapter(getContext(), new HitchhikerAdapter.CallBack() {
                @Override
                public void onRegisterHitchhiker(String idHitchhiker) {
                    handleRegisterHitchhiker(idHitchhiker);
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

    private void handleRegisterHitchhiker(String idHitchhiker) {
        try {
            Call<BaseRespone> call = mIAPIHelper.registerHitchhiker(App.sToken, idHitchhiker);
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

    private void showListHitchhiker(String action) {
        try {
            Map<String, String> maps = new HashMap<>();
            maps.put("page", String.valueOf(this.page));
            maps.put("size", String.valueOf(this.size));

            Call<BaseRespone> call = mIAPIHelper.getListHitchhiker(App.sToken, maps);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(@NonNull Call<BaseRespone> call, @NonNull Response<BaseRespone> response) {
                    try {
//                        Type type = new TypeToken<Hitchhiker[]>(){}.getType();
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
        try {
            rvHitchhiker = mView.findViewById(R.id.rvHitchhiker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
