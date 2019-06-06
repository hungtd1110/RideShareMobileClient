package com.example.admin.ridesharemobileclient.ui.routestep;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.ridesharemobileclient.R;
import com.example.admin.ridesharemobileclient.entity.RouteStep;
import com.example.admin.ridesharemobileclient.utils.PlaceUtils;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by admin on 6/1/2019.
 */

public class ItemRouteStepBinder extends ItemViewBinder<RouteStep, ItemRouteStepBinder.ViewHolder> {
    private CallBack mCallBack;
    private ProgressDialog mProgressDialog;

    public interface CallBack {
        void onEdit(int position);
    }

    public ItemRouteStepBinder(Context context, CallBack callBack) {
        mCallBack = callBack;
        mProgressDialog = new ProgressDialog(context);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.item_route_step, parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull RouteStep item) {
        PlaceUtils.setFullNamePosition(item.getLatitude(), item.getLongitude(), holder.tvName, mProgressDialog);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ivEdit = itemView.findViewById(R.id.ivEdit);

            ivEdit.setOnClickListener(new EditClick());
        }

        class EditClick implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                mCallBack.onEdit(getAdapterPosition());
            }
        }
    }
}
