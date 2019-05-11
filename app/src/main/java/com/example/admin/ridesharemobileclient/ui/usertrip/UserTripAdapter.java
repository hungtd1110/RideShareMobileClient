package com.example.admin.ridesharemobileclient.ui.usertrip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.respone.UserRespone;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.admin.ridesharemobileclient.config.Const.PREFIX_IMAGE_ADDRESS;

public class UserTripAdapter extends RecyclerView.Adapter<UserTripAdapter.ViewHolder> {
    private Context mContext;

    private List<UserRespone> mListUser;

    UserTripAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_user_trip, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            if (!TextUtils.isEmpty(mListUser.get(i).getAvatar())) {
                Picasso.get().
                        load(PREFIX_IMAGE_ADDRESS + mListUser.get(i).getAvatar()).
                        placeholder(R.drawable.ic_avatar_default).
                        error(R.drawable.ic_avatar_default).
                        into(viewHolder.ivAvatar);
            }

            viewHolder.tvName.setText(mListUser.get(i).getUsername());

            if (i == mListUser.size() - 1) {
                viewHolder.vSeparate.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mListUser != null ? mListUser.size() : 0;
    }

    public void setData(List<UserRespone> listUser) {
        try {
            mListUser = listUser;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivAvatar;
        private TextView tvName, tvMessage, tvDetail;
        private View vSeparate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            vSeparate = itemView.findViewById(R.id.vSeparate);

            tvMessage.setOnClickListener(this);
            tvDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserRespone userRespone = mListUser.get(getAdapterPosition());
            switch (v.getId()) {
                case R.id.tvMessage:
                    //Nhắn tin
                    break;
                case R.id.tvDetail:
                    //Xem chi tiết user
                    break;
            }
        }
    }
}
