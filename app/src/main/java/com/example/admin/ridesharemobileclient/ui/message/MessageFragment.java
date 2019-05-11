package com.example.admin.ridesharemobileclient.ui.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.respone.MessageRespone;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragment extends Fragment {
    private IAPIHelper mIAPIHelper;

    private View mView;
    private RecyclerView rvMessage;

    private MessageAdapter adapter;
    private int page, size;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_message, container, false);

        initView();
        initEvent();
        init();

        return mView;
    }

    private void init() {
        try {
            mIAPIHelper = APIHelper.getInstance();
            page = 1;
            size = 10;

            adapter = new MessageAdapter(getContext());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rvMessage.setLayoutManager(layoutManager);
            rvMessage.setAdapter(adapter);

            showListMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
    }

    private void initView() {
        rvMessage = mView.findViewById(R.id.rvMessage);
    }

    private void showListMessage() {
        try {
            Map<String, String> maps = new HashMap<>();
            maps.put("page", String.valueOf(this.page));
            maps.put("size", String.valueOf(this.size));

            Call<BaseRespone> call = mIAPIHelper.getListMessage(App.sToken);
            call.enqueue(new Callback<BaseRespone>() {
                @Override
                public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                    try {
                        Type type = new TypeToken<List<MessageRespone>>(){}.getType();
                        List<MessageRespone> listMessage = new Gson().fromJson(response.body().getMetadata().toString(), type);
                        adapter.setData(listMessage);
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
}
