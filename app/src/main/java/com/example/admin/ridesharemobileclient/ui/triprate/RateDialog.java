package com.example.admin.ridesharemobileclient.ui.triprate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.request.RateRequest;

/**
 * Created by admin on 6/6/2019.
 */

public class RateDialog extends AlertDialog {
    private Context mContext;
    private CallBack mCallBack;

    public interface CallBack {
        void onRate(RateRequest rateRequest);
    }

    protected RateDialog(@NonNull Context context, CallBack callBack) {
        super(context);
        mContext = context;
        mCallBack = callBack;
    }

    @Override
    public void show() {
        Builder builder = new Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_rate, null);
        AlertDialog dialog = builder.setView(view).create();
        dialog.show();

        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvRate = view.findViewById(R.id.tvRate);
        EditText etNote = view.findViewById(R.id.etNote);
        RatingBar rbStar = view.findViewById(R.id.rbStar);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateRequest rateRequest = new RateRequest();
                rateRequest.setComment(etNote.getText().toString());
                rateRequest.setStar(rbStar.getNumStars() + "");
                mCallBack.onRate(rateRequest);
                dialog.dismiss();
            }
        });
    }
}
