package com.pericstudio.drawit.aesthetics;

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

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewDecorator extends RecyclerView.ItemDecoration{

    public RecyclerViewDecorator() {

    }

    /**
     * Used to have the child view of the RecyclerView to alternate colors.
     * @param outRect not used
     * @param view not used
     * @param parent the RecyclerView that holds the individual elements
     * @param state not used
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        L.d("RecyclerViewDecorator", parent.getChildAdapterPosition(view) + "");
        if(parent.getChildAdapterPosition(view)%2 == 0) {
            //White
            view.setBackgroundColor(Color.rgb(255, 255, 255));
        } else {
            //Light Blue
            view.setBackgroundColor(Color.rgb(240, 255, 255));
        }

    }
}
