package com.example.admin.ridesharemobileclient.ui.message;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.respone.MessageRespone;
import com.example.admin.ridesharemobileclient.ui.detailmessage.DetailMessageActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.admin.ridesharemobileclient.config.Const.KEY_ID;
import static com.example.admin.ridesharemobileclient.config.Const.PREFIX_IMAGE_ADDRESS;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContext;

    private List<MessageRespone> mListMessage;

    MessageAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_message, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            Picasso.get().
                    load(PREFIX_IMAGE_ADDRESS + mListMessage.get(i).getImage()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(viewHolder.civImage);
            viewHolder.tvName.setText(mListMessage.get(i).getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mListMessage != null ? mListMessage.size() : 0;
    }

    public void setData(List<MessageRespone> listMessage) {
        try {
            if (listMessage != null) {
                mListMessage = listMessage;
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView civImage;
        private TextView tvName, tvLastMessage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            civImage = itemView.findViewById(R.id.civImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    default:
                        Intent intent = new Intent(mContext, DetailMessageActivity.class);
                        intent.putExtra(KEY_ID, mListMessage.get(getAdapterPosition()).getUserId() + "");
                        mContext.startActivity(intent);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
