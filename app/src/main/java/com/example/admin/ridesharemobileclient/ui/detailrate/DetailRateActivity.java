package com.example.admin.ridesharemobileclient.ui.detailrate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.Rate;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;

public class DetailRateActivity extends AppCompatActivity {
    private RecyclerView rvRate;

    private IAPIHelper mIAPIHelper = APIHelper.getInstance();
    private MultiTypeAdapter mAdapter = new MultiTypeAdapter();
    private Items mItems = new Items();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_rate);

        rvRate = findViewById(R.id.rvRate);

        mAdapter.register(Rate.class, new ItemRateBinder());

        mAdapter.setItems(mItems);
        rvRate.setLayoutManager(new LinearLayoutManager(this));
        rvRate.setAdapter(mAdapter);


        String idUser = getIntent().getStringExtra(KEY_ID);


        Call<BaseRespone> call = mIAPIHelper.detailRate(App.sToken, idUser);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                try {
                    Type type = new TypeToken<List<Rate>>() {
                    }.getType();
                    List<Rate> listRate = new Gson().fromJson(response.body().getMetadata().toString(), type);
                    mItems.addAll(listRate);
                    mAdapter.notifyDataSetChanged();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BaseRespone> call, Throwable throwable) {

            }
        });
    }
}
