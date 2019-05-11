package com.example.admin.ridesharemobileclient.ui.detailmessage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.config.Chat;
import com.example.admin.ridesharemobileclient.config.Const;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.entity.Message;
import com.example.admin.ridesharemobileclient.entity.detailmessage.MyMessage;
import com.example.admin.ridesharemobileclient.entity.detailmessage.OtherMessage;
import com.example.admin.ridesharemobileclient.ui.detailmessage.itembinder.MyMessageBinder;
import com.example.admin.ridesharemobileclient.ui.detailmessage.itembinder.OtherMessageBinder;
import com.example.admin.ridesharemobileclient.utils.StompUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompCommand;
import ua.naiksoftware.stomp.dto.StompHeader;
import ua.naiksoftware.stomp.dto.StompMessage;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;

public class DetailMessageActivity extends AppCompatActivity {
    private IAPIHelper mIAPIHelper;

    private RecyclerView rvDetailMessage;
    private ImageView ivSend;
    private EditText etContentMessage;
    private ImageView ivBack;

    private Items mItems;
    private MultiTypeAdapter mAdapter;
    private StompClient stompClient;
    private String userIdReceive;
    private int page, size;
    private String pathImageOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        initView();
        initEvent();
        getData();
        init();
    }

    private void getData() {
        try {
            Bundle bundle = getIntent().getExtras();
            userIdReceive = bundle.getString(KEY_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        mIAPIHelper = APIHelper.getInstance();
        page = 1;
        size = 10;

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Chat.address);
        stompClient.connect();
        Toast.makeText(this, "Start connecting", Toast.LENGTH_SHORT).show();
        StompUtils.lifecycle(stompClient);

        stompClient.topic(Chat.recive.replace(Chat.placeholder, App.sUser.getUserId())).subscribe(stompMessage -> {
            try {
                JSONObject jsonObject = new JSONObject(stompMessage.getPayload());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OtherMessage otherMessage = new OtherMessage();
                            otherMessage.setImage(jsonObject.getString("image"));
                            otherMessage.setContent(jsonObject.getString("content"));
                            mItems.add(otherMessage);
                            mAdapter.notifyDataSetChanged();
                            rvDetailMessage.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mAdapter = new MultiTypeAdapter();
        mAdapter.register(MyMessage.class, new MyMessageBinder());
        mAdapter.register(OtherMessage.class, new OtherMessageBinder());

        mItems = new Items();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvDetailMessage.setLayoutManager(layoutManager);
        rvDetailMessage.setAdapter(mAdapter);

        Map<String, String> maps = new HashMap<>();
        maps.put("page", String.valueOf(this.page));
        maps.put("size", String.valueOf(this.size));
        maps.put("idReceive", userIdReceive);

        Call<BaseRespone> call = mIAPIHelper.getDetailMessage(App.sToken, maps);
        call.enqueue(new Callback<BaseRespone>() {
            @Override
            public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                try {
                    Type type = new TypeToken<List<Message>>() {
                    }.getType();
                    List<Message> listMessage = new Gson().fromJson(response.body().getMetadata().toString(), type);
                    for (Message message : listMessage) {
                        if (String.valueOf(message.getUserIdSend()).equals(App.sUser.getUserId())) {
                            MyMessage myMessage = new MyMessage();
                            myMessage.setContent(message.getContent());
                            mItems.add(myMessage);
                        } else {
                            if (TextUtils.isEmpty(pathImageOther)) {
                                pathImageOther = message.getImage();
                            }

                            OtherMessage otherMessage = new OtherMessage();
                            otherMessage.setContent(message.getContent());
                            otherMessage.setImage(message.getImage());
                            mItems.add(otherMessage);
                        }
                    }
                    mAdapter.setItems(mItems);
                    rvDetailMessage.smoothScrollToPosition(mItems.size() - 1);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<BaseRespone> call, Throwable t) {

            }
        });
    }

    private void initEvent() {
        ivSend.setOnClickListener(new SendClick());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        rvDetailMessage = findViewById(R.id.rvDetailMessage);
        ivSend = findViewById(R.id.ivSend);
        etContentMessage = findViewById(R.id.etContentMessage);
        ivBack = findViewById(R.id.ivBack);
    }

    class SendClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userIdSend", App.sUser.getUserId());
                jsonObject.put("userIdReceive", userIdReceive);
                jsonObject.put("content", etContentMessage.getText().toString());

                StompHeader authorizationHeader = new StompHeader("authorization", App.sToken);
                stompClient.send(new StompMessage(
                        // Stomp command
                        StompCommand.SEND,
                        // Stomp Headers, Send Headers with STOMP
                        // the first header is necessary, and the other can be customized by ourselves
                        Arrays.asList(new StompHeader(StompHeader.DESTINATION, Chat.send.replace(Chat.placeholder, userIdReceive)), authorizationHeader),
                        // Stomp payload
                        jsonObject.toString())
                ).subscribe();

                MyMessage myMessage = new MyMessage();
                myMessage.setContent(etContentMessage.getText().toString());
                mItems.add(myMessage);
                mAdapter.notifyDataSetChanged();
                etContentMessage.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
