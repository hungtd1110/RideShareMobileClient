package com.example.admin.ridesharemobileclient.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.User;
import com.example.admin.ridesharemobileclient.ui.main.MainActivity;
import com.example.admin.ridesharemobileclient.ui.register.RegisterActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private IAPIHelper mDataClient;

    private EditText etEmail, etPassword;
    private TextView tvRegister;
    private Button btnLoginEmail;

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvent();
        init();
    }

    private void init() {
        mDataClient = APIHelper.getInstance();
    }

    private void initEvent() {
        tvRegister.setOnClickListener(this);
        btnLoginEmail.setOnClickListener(this);
    }

    private void initView() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvRegister = findViewById(R.id.tvRegister);
        btnLoginEmail = findViewById(R.id.btnLoginEmail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btnLoginEmail:
//                handleLoginEmail();
                Intent intent1 = new Intent(this, MainActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void handleLoginEmail() {
        User user = new User();
        user.setEmail(etEmail.getText().toString());
        user.setPassword(etPassword.getText().toString());

        Call<String> call = mDataClient.loginEmail(user);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                App.sToken = response.body();
                Log.d(TAG, "onResponse: " + App.sToken);

                if (!TextUtils.isEmpty(App.sToken)) {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    Bundle bundle = data.getExtras();
                    User user;

                    if (bundle != null) {
                        user = (User) bundle.getSerializable(getString(R.string.key_user));

                        if (user != null) {
                            etEmail.setText(user.getEmail());
                            etPassword.setText(user.getPassword());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
