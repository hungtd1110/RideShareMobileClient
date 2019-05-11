package com.example.admin.ridesharemobileclient.ui.detailmessage.itembinder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.config.Const;
import com.example.admin.ridesharemobileclient.entity.detailmessage.OtherMessage;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.multitype.ItemViewBinder;

public class OtherMessageBinder extends ItemViewBinder<OtherMessage, OtherMessageBinder.ViewHolder> {
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_other_message, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull OtherMessage item) {
        try {
            Picasso.get().
                    load(Const.PREFIX_IMAGE_ADDRESS + item.getImage()).
                    placeholder(R.drawable.ic_avatar_default).
                    error(R.drawable.ic_avatar_default).
                    into(holder.civImage);
            holder.tvMessage.setText(item.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView civImage;
        private TextView tvMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civImage = itemView.findViewById(R.id.civImage);
            tvMessage = itemView.findViewById(R.id.tvMessage);

            civImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
