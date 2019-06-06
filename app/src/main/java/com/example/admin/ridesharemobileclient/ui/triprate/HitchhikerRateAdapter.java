package com.example.admin.ridesharemobileclient.ui.triprate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.data.APIHelper;
import com.example.admin.ridesharemobileclient.data.IAPIHelper;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.example.admin.ridesharemobileclient.entity.request.RateRequest;
import com.example.admin.ridesharemobileclient.entity.respone.BaseRespone;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HitchhikerRateAdapter extends RecyclerView.Adapter<HitchhikerRateAdapter.ViewHolder> {
    private Context mContext;
    private CallBack mCallBack;

    private ArrayList<Hitchhiker> mListHitchhiker;
    private boolean showMore;
    private SimpleDateFormat mDateFormat;
    private IAPIHelper mIAPIHelper = APIHelper.getInstance();
    private ProgressDialog mProgressDialog;

    public interface CallBack {
        void onCancelHitchhiker();
    }

    @SuppressLint("SimpleDateFormat")
    HitchhikerRateAdapter(Context context, CallBack callBack) {
        mContext = context;
        mCallBack = callBack;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Đang xử lý");

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
                    default:
                        RateDialog dialog = new RateDialog(mContext, new RateDialog.CallBack() {
                            @Override
                            public void onRate(RateRequest rateRequest) {
                                rateRequest.setUserId(App.sUser.getUserId());
                                rateRequest.setDriverId(mListHitchhiker.get(getAdapterPosition()).getId());
                                mProgressDialog.show();

                                Call<BaseRespone> call = mIAPIHelper.rate(App.sToken, rateRequest);
                                call.enqueue(new Callback<BaseRespone>() {
                                    @Override
                                    public void onResponse(Call<BaseRespone> call, Response<BaseRespone> response) {
                                        Toast.makeText(mContext, "Đã gửi đánh giá", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<BaseRespone> call, Throwable t) {
                                        Toast.makeText(mContext, "Lỗi gửi đánh giá", Toast.LENGTH_SHORT).show();
                                        mProgressDialog.dismiss();
                                    }
                                });
                            }
                        });

                        dialog.show();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
