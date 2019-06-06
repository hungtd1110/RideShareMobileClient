package com.example.admin.ridesharemobileclient.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.User;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private IAPIHelper mIAPIHelper;

    private EditText etEmail, etUsername, etPassword, etConfirm, etPhone;
    private Button btnRegister;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initEvent();
        init();
    }

    private void init() {
        mIAPIHelper = APIHelper.getInstance();
    }

    private void initEvent() {
        btnRegister.setOnClickListener(this);
    }

    private void initView() {
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        etPhone = findViewById(R.id.etPhone);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String confirm = etConfirm.getText().toString();
                String phone = etPhone.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(this, getString(R.string.no_email), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(username)) {
                    Toast.makeText(this, getString(R.string.no_username), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, getString(R.string.no_password), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirm)) {
                    Toast.makeText(this, getString(R.string.no_confirm), Toast.LENGTH_SHORT).show();
                } else if (!confirm.equals(password)) {
                    Toast.makeText(this, getString(R.string.errors_confirm), Toast.LENGTH_SHORT).show();
                } else {
                    handleRegister(email, username, phone, password);
                }
                break;
        }
    }

    private void handleRegister(String email, String username, String phone, String password) {
        Call<BaseRespone> call = mIAPIHelper.register(email, username, phone, password);

        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(@NonNull Call<BaseRespone> call, @NonNull Response<BaseRespone> response) {
                BaseRespone baseRespone = response.body();
                Log.d(TAG, "onResponse: " + baseRespone);

                if (baseRespone != null) {
//                    Gson gson = new Gson();
//                    User user = gson.fromJson((String) baseRespone.getMetadata(), User.class);

                    User user = new User();
                    user.setEmail(email);
                    user.setUsername(username);
                    user.setPassword(password);

                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.key_user), user);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseRespone> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
