package com.example.admin.ridesharemobileclient.ui.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.broadcast.NotificationBroadcast;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.ui.message.MessageFragment;
import com.example.admin.ridesharemobileclient.ui.newfeed.NewFeedFragment;
import com.example.admin.ridesharemobileclient.ui.notification.NotificationFragment;
import com.example.admin.ridesharemobileclient.ui.profile.ProfileActivity;
import com.example.admin.ridesharemobileclient.ui.search.SearchActivity;
import com.example.admin.ridesharemobileclient.ui.setting.SettingFragment;
import com.example.admin.ridesharemobileclient.ui.tripregister.TripRegisterFragment;
import com.example.admin.ridesharemobileclient.ui.tripsubmit.TripSubmitFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SettingFragment.CallBack {
    private FragmentTransaction mTransaction;
    private ImageView ivNewFeed, ivTripSubmit, ivTripRegister, ivMessage, ivNotificaton, ivSetting, ivSearch, ivProfile;

//    public static StompClient stompClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        init();
    }

    private void init() {
        try {
            mTransaction = getSupportFragmentManager().beginTransaction();
            mTransaction.replace(R.id.frl_container_main, new NewFeedFragment(), getString(R.string.tag_new_feed));
            mTransaction.commit();

//            Intent intent = new Intent(this, NotificationService.class);
//            startService(intent);

            NotificationBroadcast broadcast = new NotificationBroadcast();
            IntentFilter filter = new IntentFilter();
            filter.addAction("notification");
            getApplicationContext().registerReceiver(broadcast, filter);

            Intent intentBroadcast = new Intent();
            intentBroadcast.setAction("notification");
            sendBroadcast(intentBroadcast);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        try {
            ivNewFeed.setOnClickListener(this);
            ivTripSubmit.setOnClickListener(this);
            ivTripRegister.setOnClickListener(this);
            ivMessage.setOnClickListener(this);
            ivNotificaton.setOnClickListener(this);
            ivSetting.setOnClickListener(this);
            ivSearch.setOnClickListener(this);
            ivProfile.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        try {
            ivNewFeed = findViewById(R.id.iv_new_feed);
            ivTripSubmit = findViewById(R.id.iv_trip_submit);
            ivTripRegister = findViewById(R.id.iv_trip_register);
            ivMessage = findViewById(R.id.iv_message);
            ivNotificaton = findViewById(R.id.iv_notification);
            ivSetting = findViewById(R.id.iv_setting);
            ivSearch = findViewById(R.id.ivSearch);
            ivProfile = findViewById(R.id.ivProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.iv_new_feed:
                    handleNewFeed();
                    break;
                case R.id.iv_trip_submit:
                    handleTripSubmit();
                    break;
                case R.id.iv_trip_register:
                    handleTripRegister();
                    break;
                case R.id.iv_message:
                    handleMessage();
                    break;
                case R.id.iv_notification:
                    handleNotification();
                    break;
                case R.id.iv_setting:
                    SettingFragment fragment = new SettingFragment();
                    fragment.setCallBack(this);

                    mTransaction = getSupportFragmentManager().beginTransaction();
                    mTransaction.replace(R.id.frl_container_main, fragment, getString(R.string.tag_setting));
                    mTransaction.commit();

                    ivNewFeed.setImageResource(R.drawable.ic_new_feed_black);
                    ivTripSubmit.setImageResource(R.drawable.ic_trip_submit_black);
                    ivTripRegister.setImageResource(R.drawable.ic_trip_register_black);
                    ivMessage.setImageResource(R.drawable.ic_message_black);
                    ivNotificaton.setImageResource(R.drawable.ic_notification_black);
                    ivSetting.setImageResource(R.drawable.ic_setting_green);
                    break;
                case R.id.ivSearch: {
                    Intent intent = new Intent(this, SearchActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.ivProfile: {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNotification() {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.frl_container_main, new NotificationFragment(), getString(R.string.tag_notification));
        mTransaction.commit();

        ivNewFeed.setImageResource(R.drawable.ic_new_feed_black);
        ivTripSubmit.setImageResource(R.drawable.ic_trip_submit_black);
        ivTripRegister.setImageResource(R.drawable.ic_trip_register_black);
        ivMessage.setImageResource(R.drawable.ic_message_black);
        ivNotificaton.setImageResource(R.drawable.ic_notification_green);
        ivSetting.setImageResource(R.drawable.ic_setting_black);
    }

    private void handleMessage() {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.frl_container_main, new MessageFragment(), getString(R.string.tag_message));
        mTransaction.commit();

        ivNewFeed.setImageResource(R.drawable.ic_new_feed_black);
        ivTripSubmit.setImageResource(R.drawable.ic_trip_submit_black);
        ivTripRegister.setImageResource(R.drawable.ic_trip_register_black);
        ivMessage.setImageResource(R.drawable.ic_message_green);
        ivNotificaton.setImageResource(R.drawable.ic_notification_black);
        ivSetting.setImageResource(R.drawable.ic_setting_black);
    }

    private void handleTripRegister() {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.frl_container_main, new TripRegisterFragment(), getString(R.string.tag_trip_register));
        mTransaction.commit();

        ivNewFeed.setImageResource(R.drawable.ic_new_feed_black);
        ivTripSubmit.setImageResource(R.drawable.ic_trip_submit_black);
        ivTripRegister.setImageResource(R.drawable.ic_trip_register_green);
        ivMessage.setImageResource(R.drawable.ic_message_black);
        ivNotificaton.setImageResource(R.drawable.ic_notification_black);
        ivSetting.setImageResource(R.drawable.ic_setting_black);
    }

    private void handleTripSubmit() {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.frl_container_main, new TripSubmitFragment(), getString(R.string.tag_trip_submit));
        mTransaction.commit();

        ivNewFeed.setImageResource(R.drawable.ic_new_feed_black);
        ivTripSubmit.setImageResource(R.drawable.ic_trip_submit_green);
        ivTripRegister.setImageResource(R.drawable.ic_trip_register_black);
        ivMessage.setImageResource(R.drawable.ic_message_black);
        ivNotificaton.setImageResource(R.drawable.ic_notification_black);
        ivSetting.setImageResource(R.drawable.ic_setting_black);
    }

    private void handleNewFeed() {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.frl_container_main, new NewFeedFragment(), getString(R.string.tag_new_feed));
        mTransaction.commit();

        ivNewFeed.setImageResource(R.drawable.ic_new_feed_green);
        ivTripSubmit.setImageResource(R.drawable.ic_trip_submit_black);
        ivTripRegister.setImageResource(R.drawable.ic_trip_register_black);
        ivMessage.setImageResource(R.drawable.ic_message_black);
        ivNotificaton.setImageResource(R.drawable.ic_notification_black);
        ivSetting.setImageResource(R.drawable.ic_setting_black);
    }

    @Override
    public void onNewFeed() {
        handleNewFeed();
    }

    @Override
    public void onTripSubmit() {
        handleTripSubmit();
    }

    @Override
    public void onTripRegister() {
        handleTripRegister();
    }

    @Override
    public void onMessage() {
        handleMessage();
    }

    @Override
    public void onNotification() {
        handleNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.sToken = "";
    }
}
