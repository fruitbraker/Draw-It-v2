package com.pericstudio.drawit.adapters;

/*
 * Copyright 2016 Eric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pericstudio.drawit.MyApplication;
import com.pericstudio.drawit.R;
import com.pericstudio.drawit.pojo.Drawing;
import com.pericstudio.drawit.utils.T;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapterDrawing extends RecyclerView.Adapter<RecyclerViewAdapterDrawing.RecyclerViewHolder> {

    private LayoutInflater inflater;
    private List<Drawing> data = Collections.emptyList();
    private String userID;

    private int lastPosition = -1;

    public RecyclerViewAdapterDrawing(Context context, List<Drawing> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.userID = MyApplication.userID;
    }

    /**
     * Sets the data for the RecyclerView.
     * @param drawings The used to populate the RecyclerView.
     */
    public void setData(ArrayList<Drawing> drawings) {
        data = drawings;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewAdapterDrawing.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_dashboard_item, parent, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterDrawing.RecyclerViewHolder holder, final int position) {
        final Drawing current = (Drawing) data.get(position);
        final String itemID = current.getObjectId();
        holder.title.setText(current.getTitle());
        holder.des.setText(current.getDescription());

        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                T.showShortDebug(MyApplication.getContext(), position + "");
//                LocallySavableCMObject.searchObjects(context, SearchQuery.filter("ownerID")
//                                .equal(userID).searchQuery(),
//                        new Response.Listener<CMObjectResponse>() {
//                            @Override
//                            public void onResponse(CMObjectResponse response) {
//                                List<CMObject> filler = response.getObjects();
//                                if (filler.size() > 0) {
//                                    UserObjectIDs objectIDs = (UserObjectIDs) filler.get(0);
//                                    objectIDs.removeInProgress(itemID);
//                                    objectIDs.save(context, new Response.Listener<ObjectModificationResponse>() {
//                                        @Override
//                                        public void onResponse(ObjectModificationResponse modificationResponse) {
//                                            Log.d("RecyclerAdapterDrawing", "Object removed! (Tho still on server)");
//                                        }
//                                    }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError volleyError) {
//                                            Log.e("RecyclerAdapterDrawing", "Failed delete", volleyError);
//                                        }
//                                    });
//                                } else {
//                                    //TODO: App code
//                                }
//                            }
//                        });
            }
        });
        setAnimation(holder.container, position);
    }



    @Override
    public void onViewDetachedFromWindow(RecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.clearAnimation();
    }

    /**
     * Animates each RecyclerView layouts.
     * @param viewToAnimate The particular layout to animate.
     * @param position The position of the layout.
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if(position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(MyApplication.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title, des;
        ImageButton button;
        RelativeLayout container;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_card_title);
            des = (TextView) itemView.findViewById(R.id.tv_card_des);
            button = (ImageButton) itemView.findViewById(R.id.bt_card_dashboard);
            container = (RelativeLayout) itemView.findViewById(R.id.dashboard_item);
        }

        public void clearAnimation() {
            container.clearAnimation();
        }
    }

}
