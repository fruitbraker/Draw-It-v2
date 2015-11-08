package com.pericstudio.drawit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudmine.api.CMObject;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private LayoutInflater inflater;
    private List<CMObject> data = Collections.emptyList();

    public RecyclerViewAdapter(Context context, List<CMObject> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_dashboard_item, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        Drawing current = (Drawing) data.get(position);
        holder.title.setText(current.getTitle());
        holder.des.setText(current.getDescription());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title, des;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_card_title);
            des = (TextView) itemView.findViewById(R.id.tv_card_des);

        }
    }
}
