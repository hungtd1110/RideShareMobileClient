package com.example.admin.ridesharemobileclient.ui.detailtripregister;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.RouteStep;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;

/**
 * Created by admin on 6/2/2019.
 */

public class RegisterTripDialog extends AlertDialog {
    private Context mContext;
    private RouteStep mRouteStep;
    private CallBack mCallBack;

    public interface CallBack {
        void onPickPosition();

        void onRegiser(RouteStep mRouteStep);
    }

    public RegisterTripDialog(@NonNull Context context, RouteStep routeStep, CallBack callBack) {
        super(context);
        mContext = context;
        mCallBack = callBack;
        mRouteStep = routeStep;
    }

    @Override
    public void show() {
        Builder builder = new Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_register_trip, null);
        AlertDialog dialog = builder.setView(view).create();
        dialog.show();

        TextView tvPickPosition = view.findViewById(R.id.tvPickPosition);
        TextView tvRegister = view.findViewById(R.id.tvRegister);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        ImageView ivEditPickPosition = view.findViewById(R.id.ivEditPickPosition);
        RelativeLayout rlTool = view.findViewById(R.id.rlTool);


        if (mRouteStep != null) {
            rlTool.setVisibility(View.VISIBLE);

            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Đang xử lý");
            PlaceUtils.setFullNamePosition(mRouteStep.getLatitude(), mRouteStep.getLongitude(), tvPickPosition, progressDialog);
        }
        else {
            rlTool.setVisibility(View.GONE);
        }

        ivEditPickPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onPickPosition();
                dialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onRegiser(mRouteStep);
                dialog.dismiss();
            }
        });
    }
}
