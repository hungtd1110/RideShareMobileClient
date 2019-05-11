package com.example.admin.ridesharemobileclient.ui.search;

import android.content.Context;
import android.content.Intent;
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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Driver> mListTrip;
    private boolean showMore;

    SearchAdapter(Context context) {
        mContext = context;
        showMore = false;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.llInformation.setVisibility(View.GONE);
        holder.llAction.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mListTrip != null ? mListTrip.size() : 3;
    }

    void setData(ArrayList<Driver> listTrip) {
        mListTrip = listTrip;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDetail, tvShowMore;
        private LinearLayout llInformation, llAction;

        ViewHolder(View itemView) {
            super(itemView);

            tvDetail = itemView.findViewById(R.id.tvActionRight);
            tvShowMore = itemView.findViewById(R.id.tvShowMore);
            llInformation = itemView.findViewById(R.id.cslInformation);
            llAction = itemView.findViewById(R.id.llAction);

            tvDetail.setOnClickListener(this);
            tvShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvActionRight:
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
