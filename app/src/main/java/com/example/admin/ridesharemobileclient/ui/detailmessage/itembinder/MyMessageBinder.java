package com.example.admin.ridesharemobileclient.ui.detailmessage.itembinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.detailmessage.MyMessage;

import me.drakeet.multitype.ItemViewBinder;

public class MyMessageBinder extends ItemViewBinder<MyMessage, MyMessageBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_my_message, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MyMessage item) {
        holder.tvMessage.setText(item.getContent());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}
