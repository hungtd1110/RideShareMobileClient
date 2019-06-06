package com.example.admin.ridesharemobileclient.ui.newfeed;

import android.annotation.SuppressLint;
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
import com.example.admin.ridesharemobileclient.ui.detailmessage.DetailMessageActivity;
import com.example.admin.ridesharemobileclient.ui.detailtripregister.DetailTripRegisterActivity;
import com.example.admin.ridesharemobileclient.ui.usertrip.UserTripActivity;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.admin.ridesharemobileclient.config.Const.DATA_HITCHHIKER;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;

public class HitchhikerAdapter extends RecyclerView.Adapter<HitchhikerAdapter.ViewHolder> {
    private Context mContext;

    private ArrayList<Hitchhiker> mListHitchhiker;
    private boolean showMore;
    private SimpleDateFormat mDateFormat;

    private static final String TAG = "HitchhikerAdapter";

    @SuppressLint("SimpleDateFormat")
    HitchhikerAdapter(Context context) {
        mContext = context;

        showMore = false;
        mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Hitchhiker hitchhiker = mListHitchhiker.get(position);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(hitchhiker.getTime()));

            holder.llAction.setVisibility(View.GONE);
            holder.tvShowMore.setVisibility(View.GONE);

            PlaceUtils.setFullNamePosition(hitchhiker.getStartLatitude(), hitchhiker.getStartLongitude(), holder.tvStartPosition);
            PlaceUtils.setFullNamePosition(hitchhiker.getEndLatitude(), hitchhiker.getEndLongitude(), holder.tvEndPosition);
            holder.tvTime.setText(mDateFormat.format(calendar.getTime()));
            holder.tvNumberSeat.setText(hitchhiker.getNumberSeat());
            holder.tvPrice.setText(NumberFormat.getInstance().format(Long.parseLong(hitchhiker.getPrice())) + " VNĐ");

            holder.tvActionLeft.setText("Nhắn tin");
            holder.tvActionRight.setText("Danh sách");
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
        private TextView tvStartPosition, tvEndPosition, tvTime, tvActionLeft, tvActionRight, tvNumberSeat, tvPrice, tvShowMore;
        private LinearLayout llAction;

        ViewHolder(View itemView) {
            super(itemView);

            try {
                tvActionLeft = itemView.findViewById(R.id.tvActionLeft);
                tvActionRight = itemView.findViewById(R.id.tvActionRight);
                tvStartPosition = itemView.findViewById(R.id.tvStartPosition);
                tvEndPosition = itemView.findViewById(R.id.tvEndPosition);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvNumberSeat = itemView.findViewById(R.id.tvNumberSeat);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvShowMore = itemView.findViewById(R.id.tvShowMore);
                llAction = itemView.findViewById(R.id.llAction);

                tvActionLeft.setOnClickListener(this);
                tvActionRight.setOnClickListener(this);
                tvShowMore.setOnClickListener(this);
                itemView.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()) {
                    case R.id.tvActionLeft: {
                        Intent intent = new Intent(mContext, DetailMessageActivity.class);
                        intent.putExtra(KEY_ID, mListHitchhiker.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                        break;
                    }
                    case R.id.tvActionRight: {
                        Intent intent = new Intent(mContext, UserTripActivity.class);
                        intent.putExtra(KEY_TYPE, DATA_HITCHHIKER);
                        intent.putExtra(KEY_ID, mListHitchhiker.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                        break;
                    }
                    case R.id.tvShowMore:
                        if (showMore) {
                            llAction.setVisibility(View.GONE);
                            showMore = false;
                        } else {
                            llAction.setVisibility(View.VISIBLE);
                            showMore = true;
                        }
                        break;
                    default:
                        Intent intent = new Intent(mContext, DetailTripRegisterActivity.class);
                        intent.putExtra(KEY_TYPE, DATA_HITCHHIKER);
                        intent.putExtra(KEY_ID, mListHitchhiker.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
