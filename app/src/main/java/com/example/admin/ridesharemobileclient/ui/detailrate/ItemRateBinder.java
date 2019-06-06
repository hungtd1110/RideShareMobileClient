package com.example.admin.ridesharemobileclient.ui.detailrate;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.Rate;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by admin on 6/6/2019.
 */

public class ItemRateBinder extends ItemViewBinder<Rate, ItemRateBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View itemView = layoutInflater.inflate(R.layout.item_rate, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull Rate rate) {
        viewHolder.tvContent.setText(rate.getComment());
        viewHolder.tvName.setText(rate.getUsername());
        viewHolder.tvStar.setText(rate.getStar());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent, tvName, tvStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.tvContent);
            tvName = itemView.findViewById(R.id.tvName);
            tvStar = itemView.findViewById(R.id.tvStar);
        }
    }
}
