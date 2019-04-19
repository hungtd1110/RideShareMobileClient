package com.example.admin.ridesharemobileclient.ui.mytrip;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.Driver;
import com.example.admin.ridesharemobileclient.ui.detailtripregister.DetailTripRegisterActivity;

import java.util.ArrayList;

public class MyDriverAdapter extends RecyclerView.Adapter<MyDriverAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Driver> mListDriver;
    private boolean showMore;

    MyDriverAdapter(Context context) {
        mContext = context;
        showMore = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_register_trip, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.llInformation.setVisibility(View.GONE);
        holder.llAction.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mListDriver != null ? mListDriver.size() : 3;
    }

    void setData(ArrayList<Driver> listDriver) {
        mListDriver = listDriver;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDetail, tvShowMore;
        private LinearLayout llInformation, llAction;

        ViewHolder(View itemView) {
            super(itemView);

            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvShowMore = itemView.findViewById(R.id.tvShowMore);
            llInformation = itemView.findViewById(R.id.llInformation);
            llAction = itemView.findViewById(R.id.llAction);

            tvDetail.setOnClickListener(this);
            tvShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvDetail:
                    Intent intent = new Intent(mContext, DetailTripRegisterActivity.class);
                    mContext.startActivity(intent);
                    break;
                case R.id.tvShowMore:
                    if (showMore) {
                        llInformation.setVisibility(View.GONE);
                        llAction.setVisibility(View.GONE);
                        showMore = false;
                    }
                    else {
                        llInformation.setVisibility(View.VISIBLE);
                        llAction.setVisibility(View.VISIBLE);
                        showMore = true;
                    }
                    break;
            }
        }
    }
}
