package com.example.admin.ridesharemobileclient.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;

import java.util.ArrayList;

public class HitchhikerAdapter extends RecyclerView.Adapter<HitchhikerAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Hitchhiker> mListHitchhiker;

    HitchhikerAdapter(Context context) {
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
        return mListHitchhiker != null ? mListHitchhiker.size() : 3;
    }

    public void setData(ArrayList<Hitchhiker> listHitchhiker) {
        mListHitchhiker = listHitchhiker;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
