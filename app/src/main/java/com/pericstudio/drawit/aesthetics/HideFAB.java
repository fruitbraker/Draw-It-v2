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

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.pericstudio.drawit.MyApplication;

public class HideFAB extends FloatingActionButton.Behavior {

    private int negTicks = 0 , posTicks = 0;
    private RecyclerView rv;

    public HideFAB(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

//        L.d("negTicks", negTicks + "");
//        L.d("posTIcks", posTicks + "");

        if(!child.isShown() && negTicks > MyApplication.TICK_THRESHOLD) {
            child.show();
            negTicks = 0;
            posTicks = 0;
        } else if(child.isShown() && posTicks > MyApplication.TICK_THRESHOLD) {
            child.hide();
            negTicks = 0;
            posTicks = 0;
        }

        if(dyConsumed > 0) {
            posTicks++;
        } else if( dyConsumed < 0) {
            negTicks++;
        }

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
