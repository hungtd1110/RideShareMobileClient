package com.example.admin.ridesharemobileclient.ui.main;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.ui.home.HomeFragment;
import com.example.admin.ridesharemobileclient.ui.message.MessageFragment;
import com.example.admin.ridesharemobileclient.ui.notification.NotificationFragment;
import com.example.admin.ridesharemobileclient.ui.setting.SettingFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FragmentTransaction mTransaction;
    private TextView tvHome, tvMessage, tvNotificaton, tvSetting;

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
            mTransaction.replace(R.id.frl_container, new HomeFragment(), getString(R.string.tag_home));
            mTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEvent() {
        try {
            tvHome.setOnClickListener(this);
            tvMessage.setOnClickListener(this);
            tvNotificaton.setOnClickListener(this);
            tvSetting.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        try {
            tvHome = findViewById(R.id.tv_home);
            tvMessage = findViewById(R.id.tv_message);
            tvNotificaton = findViewById(R.id.tv_notification);
            tvSetting = findViewById(R.id.tv_setting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            mTransaction = getSupportFragmentManager().beginTransaction();

            switch (view.getId()) {
                case R.id.tv_home:
                    mTransaction.replace(R.id.frl_container, new HomeFragment(), getString(R.string.tag_home));

                    //set drawable
                    tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_home_selected, 0, 0);
                    tvMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_message, 0, 0);
                    tvNotificaton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_notification, 0, 0);
                    tvSetting.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_setting, 0, 0);

                    //set text color
                    tvHome.setTextColor(getResources().getColor(R.color.colorGreen));
                    tvMessage.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvNotificaton.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvSetting.setTextColor(getResources().getColor(R.color.colorGrayLight));

                    break;
                case R.id.tv_message:
                    mTransaction.replace(R.id.frl_container, new MessageFragment(), getString(R.string.tag_message));

                    //set drawable
                    tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_home, 0, 0);
                    tvMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_message_selected, 0, 0);
                    tvNotificaton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_notification, 0, 0);
                    tvSetting.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_setting, 0, 0);

                    //set text color
                    tvHome.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvMessage.setTextColor(getResources().getColor(R.color.colorGreen));
                    tvNotificaton.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvSetting.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    break;
                case R.id.tv_notification:
                    mTransaction.replace(R.id.frl_container, new NotificationFragment(), getString(R.string.tag_notification));

                    //set drawable
                    tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_home, 0, 0);
                    tvMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_message, 0, 0);
                    tvNotificaton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_notification_selected, 0, 0);
                    tvSetting.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_setting, 0, 0);

                    //set text color
                    tvHome.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvMessage.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvNotificaton.setTextColor(getResources().getColor(R.color.colorGreen));
                    tvSetting.setTextColor(getResources().getColor(R.color.colorGrayLight));

                    break;
                case R.id.tv_setting:
                    mTransaction.replace(R.id.frl_container, new SettingFragment(), getString(R.string.tag_setting));

                    //set drawable
                    tvHome.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_home, 0, 0);
                    tvMessage.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_message, 0, 0);
                    tvNotificaton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_notification, 0, 0);
                    tvSetting.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_setting_selected, 0, 0);

                    //set text color
                    tvHome.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvMessage.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvNotificaton.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    tvSetting.setTextColor(getResources().getColor(R.color.colorGreen));

                    break;

            }

            mTransaction.commit();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }
}
