package com.example.admin.ridesharemobileclient.ui.tripregister;

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
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.example.admin.ridesharemobileclient.ui.detailtripregister.DetailTripRegisterActivity;

import java.util.ArrayList;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;

public class HitchhikerRegisterAdapter extends RecyclerView.Adapter<HitchhikerRegisterAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Hitchhiker> mListHitchhiker;
    private boolean showMore;

    HitchhikerRegisterAdapter(Context context) {
        mContext = context;
        showMore = false;

        //fake data
//        mListHitchhiker = new ArrayList<>();
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
//        mListHitchhiker.add(new Hitchhiker());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView;

        if (viewType == 0) {
            itemView = inflater.inflate(R.layout.view_load_more, parent, false);
        } else {
            itemView = inflater.inflate(R.layout.item_trip, parent, false);
        }

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.llAction.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mListHitchhiker != null ? mListHitchhiker.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListHitchhiker.get(position) != null) {
            return 1;
        } else {
            return 0;
        }
    }

    void setData(ArrayList<Hitchhiker> listHitchhiker) {
        try {
            if (listHitchhiker != null) {
                mListHitchhiker = listHitchhiker;
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadMore() {
        try {
            mListHitchhiker.add(null);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addData(ArrayList<Hitchhiker> listHitchhiker) {
        try {
            if (listHitchhiker != null) {
                mListHitchhiker.remove(mListHitchhiker.size() - 1);
                mListHitchhiker.addAll(listHitchhiker);
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDetail, tvShowMore;
        private LinearLayout llAction;

        ViewHolder(View itemView) {
            super(itemView);

            try {
                tvDetail = itemView.findViewById(R.id.tvActionRight);
                tvShowMore = itemView.findViewById(R.id.tvShowMore);
                llAction = itemView.findViewById(R.id.llAction);

                tvDetail.setOnClickListener(this);
                tvShowMore.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()) {
                    case R.id.tvActionRight:
                        Intent intent = new Intent(mContext, DetailTripRegisterActivity.class);
                        intent.putExtra(KEY_ID, mListHitchhiker.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                        break;
                    case R.id.tvShowMore:
                        if (showMore) {
                            llAction.setVisibility(View.GONE);
                            showMore = false;
                        } else {
                            llAction.setVisibility(View.VISIBLE);
                            showMore = true;
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
