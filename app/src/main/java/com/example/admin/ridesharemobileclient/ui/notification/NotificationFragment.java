package com.example.admin.ridesharemobileclient.ui.notification;

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
import com.example.admin.ridesharemobileclient.entity.respone.Notification;
import com.example.admin.ridesharemobileclient.entity.respone.NotificationResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 3/18/2019.
 */

public class NotificationFragment extends Fragment {
    private RecyclerView rvNotification;

    private IAPIHelper mIAPIHelper = APIHelper.getInstance();
    private MultiTypeAdapter mAdapter = new MultiTypeAdapter();
    private Items mItems = new Items();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        rvNotification = view.findViewById(R.id.rvNotification);

        mAdapter.register(Notification.class, new ItemNotificationBinder());

        mAdapter.setItems(mItems);
        rvNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotification.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Call<BaseRespone> call = mIAPIHelper.getNotification(App.sToken);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                Type type = new TypeToken<NotificationResponse>() {
                }.getType();
                NotificationResponse notificationResponse = new Gson().fromJson(response.body().getMetadata().toString(), type);
                mItems.addAll(notificationResponse.getNotifications());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BaseRespone> call, Throwable throwable) {

            }
        });
    }
}
