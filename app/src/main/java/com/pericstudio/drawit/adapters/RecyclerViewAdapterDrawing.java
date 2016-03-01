package com.pericstudio.drawit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.SearchQuery;
import com.cloudmine.api.db.LocallySavableCMObject;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.pojo.Drawing;
import com.pericstudio.drawit.pojo.UserObjectIDs;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapterDrawing extends RecyclerView.Adapter<RecyclerViewAdapterDrawing.RecyclerViewHolder> {

    private LayoutInflater inflater;
    private List<CMObject> data = Collections.emptyList();
    private Context context;
    private String userID;

    public RecyclerViewAdapterDrawing(Context context, List<CMObject> data, String userID) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        this.userID = userID;
    }

    @Override
    public RecyclerViewAdapterDrawing.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_dashboard_item, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterDrawing.RecyclerViewHolder holder, int position) {
        final Drawing current = (Drawing) data.get(position);
        final String itemID = current.getObjectId();
        holder.title.setText(current.getTitle());
        holder.des.setText(current.getDescription());
        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LocallySavableCMObject.searchObjects(context, SearchQuery.filter("ownerID")
                                .equal(userID).searchQuery(),
                        new Response.Listener<CMObjectResponse>() {
                            @Override
                            public void onResponse(CMObjectResponse response) {
                                List<CMObject> filler = response.getObjects();
                                if (filler.size() > 0) {
                                    UserObjectIDs objectIDs = (UserObjectIDs) filler.get(0);
                                    objectIDs.removeInProgress(itemID);
                                    objectIDs.save(context, new Response.Listener<ObjectModificationResponse>() {
                                        @Override
                                        public void onResponse(ObjectModificationResponse modificationResponse) {
                                            Log.d("RecyclerAdapterDrawing", "Object removed! (Tho still on server)");
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            Log.e("RecyclerAdapterDrawing", "Failed delete", volleyError);
                                        }
                                    });
                                } else {
                                    //TODO: App code
                                }
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title, des;
        Button button;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_card_title);
            des = (TextView) itemView.findViewById(R.id.tv_card_des);
            button = (Button) itemView.findViewById(R.id.bt_card_dashboard);
        }
    }
}
