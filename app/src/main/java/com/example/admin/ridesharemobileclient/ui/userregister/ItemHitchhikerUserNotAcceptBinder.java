package com.example.admin.ridesharemobileclient.ui.userregister;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.respone.HitchhikerUserNotAccept;

import me.drakeet.multitype.ItemViewBinder;

public class ItemHitchhikerUserNotAcceptBinder extends ItemViewBinder<HitchhikerUserNotAccept, ItemHitchhikerUserNotAcceptBinder.ViewHolder> {
    private CallBack mCallBack;

    ItemHitchhikerUserNotAcceptBinder(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void onAccept(String idUserRegister);

        void onCancel(String idUserRegister);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_user_register, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull HitchhikerUserNotAccept item) {
        try {
            holder.mItem = item;

            holder.tvName.setText(item.getUser().getUsername());
            holder.tvStar.setText(item.getUser().getStar());
            holder.tvAccept.setVisibility(View.VISIBLE);
            holder.tvCancel.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName, tvAccept, tvCancel, tvStar;
        private HitchhikerUserNotAccept mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvAccept = itemView.findViewById(R.id.tvAccept);
            tvCancel = itemView.findViewById(R.id.tvCancel);
            tvStar = itemView.findViewById(R.id.tvStar);

            tvAccept.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAccept:
                    mCallBack.onAccept(mItem.getUser().getUserHitchhikerId());
                    break;
                case R.id.tvCancel:
                    mCallBack.onCancel(mItem.getUser().getUserHitchhikerId());
                    break;
            }
        }
    }
}
