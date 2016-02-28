package com.pericstudio.drawit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pericstudio.drawit.R;
import com.pericstudio.drawit.pojo.DrawerInfo;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapterNav  extends RecyclerView.Adapter<RecyclerViewAdapterNav.RecyclerViewHolder> {

    private LayoutInflater inflater;
    private List<DrawerInfo> data = Collections.emptyList();

    public RecyclerViewAdapterNav(Context context, List<DrawerInfo> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_nav_bar, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        DrawerInfo current = data.get(position);
        holder.title.setText(current.des);
        holder.icon.setImageResource(current.iconId);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView icon;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_custom_row);
            icon = (ImageView) itemView.findViewById(R.id.custom_row_icon);
        }
    }
}