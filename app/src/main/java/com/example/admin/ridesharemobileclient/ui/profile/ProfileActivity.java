package com.example.admin.ridesharemobileclient.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.respone.DetailUserRespone;
import com.example.admin.ridesharemobileclient.ui.detailrate.DetailRateActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.PREFIX_IMAGE_ADDRESS;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView civImage;
    private TextView tvUsername, tvChangeProfile, tvChangePassword, tvMessage, tvEmail, tvPhone, tvStar;
    private ImageView ivBack, ivSeeRate;

    private IAPIHelper mIAPIHelper;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        initView();
        initEvent();
//        getData();
        init();
    }

    private void getData() {
//        Bundle bundle = getIntent().getExtras();
//        idUser = bundle.getString(KEY_PROFILE);
    }

    private void init() {
        mIAPIHelper = APIHelper.getInstance();

        Call<BaseRespone> call = mIAPIHelper.getDetailUser(App.sToken);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                try {
                    Type type = new TypeToken<DetailUserRespone>() {
                    }.getType();

                    DetailUserRespone user = new Gson().fromJson((String) response.body().getMetadata(), type);
                    showProfile(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BaseRespone> call, Throwable t) {

            }
        });
    }

    private void showProfile(DetailUserRespone user) {
        try {
            tvUsername.setText(user.getUsername());
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());
            tvStar.setText(user.getStar());
            Picasso.get().
                    load(PREFIX_IMAGE_ADDRESS +user.getImage()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(civImage);

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

        ivSeeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DetailRateActivity.class);
                intent.putExtra(KEY_ID, App.sUser.getUserId());
                startActivity(intent);
            }
        });
    }

    private void initView() {
        tvUsername = findViewById(R.id.tvUsername);
        tvChangeProfile = findViewById(R.id.tvChangeProfile);
        tvChangePassword = findViewById(R.id.tvChangePassword);
        tvMessage = findViewById(R.id.tvMessage);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvStar = findViewById(R.id.tvStar);
        ivBack = findViewById(R.id.ivBack);
        ivSeeRate = findViewById(R.id.ivSeeRate);
    }
}
