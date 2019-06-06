package com.example.admin.ridesharemobileclient.ui.searchadvance;

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
import com.example.admin.ridesharemobileclient.config.App;
import com.example.admin.ridesharemobileclient.entity.Hitchhiker;
import com.example.admin.ridesharemobileclient.ui.detailtripregister.DetailTripRegisterActivity;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.drakeet.multitype.ItemViewBinder;

import static com.example.admin.ridesharemobileclient.config.Const.DATA_HITCHHIKER;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.KEY_TYPE;

public class ItemHitchhikerBinder extends ItemViewBinder<Hitchhiker, ItemHitchhikerBinder.ViewHolder> {
    private Context mContext;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");;

    public ItemHitchhikerBinder(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Hitchhiker item) {
        holder.mItem = item;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(item.getTime()));

        holder.llAction.setVisibility(View.GONE);
        holder.tvShowMore.setVisibility(View.GONE);

        PlaceUtils.setFullNamePosition(item.getStartLatitude(), item.getStartLongitude(), holder.tvStartPosition);
        PlaceUtils.setFullNamePosition(item.getEndLatitude(), item.getEndLongitude(), holder.tvEndPosition);
        holder.tvTime.setText(mDateFormat.format(calendar.getTime()));
        holder.tvNumberSeat.setText(item.getNumberSeat());
        holder.tvPrice.setText(NumberFormat.getInstance().format(Long.parseLong(item.getPrice())) + " VNĐ");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvStartPosition, tvEndPosition, tvTime, tvActionLeft, tvActionRight, tvNumberSeat, tvPrice, tvShowMore;
        private LinearLayout llAction;
        private Hitchhiker mItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvActionLeft = itemView.findViewById(R.id.tvActionLeft);
            tvActionRight = itemView.findViewById(R.id.tvActionRight);
            tvStartPosition = itemView.findViewById(R.id.tvStartPosition);
            tvEndPosition = itemView.findViewById(R.id.tvEndPosition);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNumberSeat = itemView.findViewById(R.id.tvNumberSeat);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvShowMore = itemView.findViewById(R.id.tvShowMore);
            llAction = itemView.findViewById(R.id.llAction);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                default:
                    Intent intent = new Intent(App.getInstance().getApplicationContext(), DetailTripRegisterActivity.class);
                    intent.putExtra(KEY_TYPE, DATA_HITCHHIKER);
                    intent.putExtra(KEY_ID, mItem.getId());
                    mContext.startActivity(intent);
                    break;
            }
        }
    }
}
