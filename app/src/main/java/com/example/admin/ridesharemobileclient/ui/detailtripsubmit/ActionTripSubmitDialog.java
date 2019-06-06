package com.example.admin.ridesharemobileclient.ui.detailtripsubmit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.ridesharemobileclient.R;

/**
 * Created by admin on 6/4/2019.
 */

@SuppressLint("ValidFragment")
public class ActionTripSubmitDialog extends BottomSheetDialogFragment {
    private CallBack mCallBack;

    public ActionTripSubmitDialog(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void onListPeople();

        void onDelete();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_action_trip_submit, container, false);

        view.findViewById(R.id.tvListPeopel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onListPeople();
            }
        });

        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onDelete();
            }
        });

        return view;
    }
}
