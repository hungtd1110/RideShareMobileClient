package com.example.admin.ridesharemobileclient.ui.notification;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.respone.Notification;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by admin on 6/6/2019.
 */

public class ItemNotificationBinder extends ItemViewBinder<Notification, ItemNotificationBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View itemView = layoutInflater.inflate(R.layout.item_notification, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull Notification notification) {
        viewHolder.tvContent.setText(notification.getContent());
        viewHolder.tvTime.setText(notification.getCreatedAt());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
