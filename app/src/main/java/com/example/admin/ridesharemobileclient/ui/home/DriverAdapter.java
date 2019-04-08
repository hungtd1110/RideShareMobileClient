package com.example.admin.ridesharemobileclient.ui.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.Driver;
import com.example.admin.ridesharemobileclient.ui.detailtripdriver.DetailDriverActivity;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Driver> mListDriver;

    DriverAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mListDriver != null ? mListDriver.size() : 3;
    }

    public void setData(ArrayList<Driver> listDriver) {
        mListDriver = listDriver;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDetail;

        ViewHolder(View itemView) {
            super(itemView);

            tvDetail = itemView.findViewById(R.id.tvDetail);

            tvDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvDetail:
                    Intent intent = new Intent(mContext, DetailDriverActivity.class);
                    mContext.startActivity(intent);
                    break;
            }
        }
    }
}
