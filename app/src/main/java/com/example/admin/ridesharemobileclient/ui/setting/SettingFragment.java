package com.example.admin.ridesharemobileclient.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.ui.profile.ProfileActivity;
import com.example.admin.ridesharemobileclient.ui.triprate.TripRateActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.admin.ridesharemobileclient.config.Const.PREFIX_IMAGE_ADDRESS;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private CallBack mCallBack;

    private View mView;
    private CircleImageView civImage;
    private ConstraintLayout cslInformation;
    private TextView tvName;
    private LinearLayout llNewFeed, llTripSubmit, llTripRegister, llMessage, llNotification, llRate, llIntroduce, llLogout;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llNewFeed:
                mCallBack.onNewFeed();
                break;
            case R.id.llTripSubmit:
                mCallBack.onTripSubmit();
                break;
            case R.id.llTripRegister:
                mCallBack.onTripRegister();
                break;
            case R.id.llMessage:
                mCallBack.onMessage();
                break;
            case R.id.llNotification:
                mCallBack.onNotification();
                break;
            case R.id.llRate: {
                Intent intent = new Intent(getContext(), TripRateActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.llIntroduce:
                break;
            case R.id.llLogout:
                break;
            case R.id.cslInformation: {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    public interface CallBack {
        void onNewFeed();

        void onTripSubmit();

        void onTripRegister();

        void onMessage();

        void onNotification();
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting, container, false);

        initView();
        initEvent();
        init();

        return mView;
    }

    private void initView() {
        try {
            civImage = mView.findViewById(R.id.civImage);
            tvName = mView.findViewById(R.id.tvName);
            llNewFeed = mView.findViewById(R.id.llNewFeed);
            llTripSubmit = mView.findViewById(R.id.llTripSubmit);
            llTripRegister = mView.findViewById(R.id.llTripRegister);
            llMessage = mView.findViewById(R.id.llMessage);
            llNotification = mView.findViewById(R.id.llNotification);
            llRate = mView.findViewById(R.id.llRate);
            llIntroduce = mView.findViewById(R.id.llIntroduce);
            llLogout = mView.findViewById(R.id.llLogout);
            cslInformation = mView.findViewById(R.id.cslInformation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        llNewFeed.setOnClickListener(this);
        llTripSubmit.setOnClickListener(this);
        llTripRegister.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llNotification.setOnClickListener(this);
        llRate.setOnClickListener(this);
        llIntroduce.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        cslInformation.setOnClickListener(this);
    }

    private void init() {
        try {
            Picasso.get().
                    load(PREFIX_IMAGE_ADDRESS + App.sUser.getImage()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(civImage);
            tvName.setText(App.sUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
