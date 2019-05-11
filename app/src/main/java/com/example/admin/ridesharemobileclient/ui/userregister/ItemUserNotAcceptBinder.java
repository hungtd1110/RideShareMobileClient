package com.example.admin.ridesharemobileclient.ui.userregister;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.respone.UserNotAccept;
import com.squareup.picasso.Picasso;

import me.drakeet.multitype.ItemViewBinder;

import static com.example.admin.ridesharemobileclient.config.Const.PREFIX_IMAGE_ADDRESS;

public class ItemUserNotAcceptBinder extends ItemViewBinder<UserNotAccept, ItemUserNotAcceptBinder.ViewHolder> {
    private CallBack mCallBack;

    ItemUserNotAcceptBinder(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
        void onAccept(String idTrip, String idUserRegister);

        void onCancel(String idTrip, String idUserRegister);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_user_register, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull UserNotAccept item) {
        try {
            holder.mItem = item;

            Picasso.get().
                    load(PREFIX_IMAGE_ADDRESS + item.getUser().getAvatar()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(holder.ivAvatar);
            holder.tvName.setText(item.getUser().getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivAvatar;
        private TextView tvName, tvAccept, tvCancel;
        private UserNotAccept mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvAccept = itemView.findViewById(R.id.tvAccept);
            tvCancel = itemView.findViewById(R.id.tvCancel);

            tvAccept.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAccept:
                    mCallBack.onAccept(mItem.getUser().getUserDriverId(), mItem.getUser().getUserId());
                    break;
                case R.id.tvCancel:
                    mCallBack.onCancel(mItem.getUser().getUserDriverId(), mItem.getUser().getUserDriverId());
                    break;
            }
        }
    }
}
