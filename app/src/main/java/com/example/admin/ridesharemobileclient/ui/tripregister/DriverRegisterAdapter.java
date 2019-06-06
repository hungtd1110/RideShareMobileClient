package com.example.admin.ridesharemobileclient.ui.tripregister;

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
import com.example.admin.ridesharemobileclient.entity.Driver;
import com.example.admin.ridesharemobileclient.ui.detailmessage.DetailMessageActivity;
import com.example.admin.ridesharemobileclient.ui.detailtripregister.DetailTripRegisterActivity;
import com.example.admin.ridesharemobileclient.ui.usertrip.UserTripActivity;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.admin.ridesharemobileclient.config.Const.DATA_DRIVER;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;

public class DriverRegisterAdapter extends RecyclerView.Adapter<DriverRegisterAdapter.ViewHolder> {
    private Context mContext;
    private CallBack mCallBack;

    private ArrayList<Driver> mListDriver;
    private boolean showMore;
    private SimpleDateFormat mDateFormat;

    public interface CallBack {
        void onCancelDriver();
    }

    @SuppressLint("SimpleDateFormat")
    DriverRegisterAdapter(Context context, CallBack callBack) {
        mContext = context;
        mCallBack = callBack;

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
            Driver driver = mListDriver.get(position);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(driver.getTime()));

            holder.llAction.setVisibility(View.GONE);
            holder.tvShowMore.setVisibility(View.GONE);

            PlaceUtils.setFullNamePosition(driver.getStartLatitude(), driver.getStartLongitude(), holder.tvStartPosition);
            PlaceUtils.setFullNamePosition(driver.getEndLatitude(), driver.getEndLongitude(), holder.tvEndPosition);
            holder.tvTime.setText(mDateFormat.format(calendar.getTime()));
            holder.tvNumberSeat.setText(driver.getNumberSeat());
            holder.tvPrice.setText(NumberFormat.getInstance().format(Long.parseLong(driver.getPrice())) + " VNĐ");

            holder.tvActionLeft.setText("Nhắn tin");
            holder.tvActionRight.setText("Danh sách");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mListDriver != null ? mListDriver.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mListDriver.get(position) != null) {
            return 1;
        } else {
            return 0;
        }
    }

    void setData(ArrayList<Driver> listDriver) {
        try {
            if (listDriver != null) {
                mListDriver = listDriver;
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadMore() {
        try {
            mListDriver.add(null);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addData(ArrayList<Driver> listDriver) {
        try {
            if (listDriver != null) {
                mListDriver.remove(mListDriver.size() - 1);
                mListDriver.addAll(listDriver);
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
                        intent.putExtra(KEY_ID, mListDriver.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                        break;
                    }
                    case R.id.tvActionRight: {
                        Intent intent = new Intent(mContext, UserTripActivity.class);
                        intent.putExtra(KEY_TYPE, DATA_DRIVER);
                        intent.putExtra(KEY_ID, mListDriver.get(getAdapterPosition()).getId());
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
                        intent.putExtra(KEY_TYPE, DATA_DRIVER);
                        intent.putExtra(KEY_ID, mListDriver.get(getAdapterPosition()).getId());
                        mContext.startActivity(intent);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
