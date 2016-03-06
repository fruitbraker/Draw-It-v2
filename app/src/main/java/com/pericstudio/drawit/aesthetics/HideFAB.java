package com.pericstudio.drawit.aesthetics;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.pericstudio.drawit.utils.L;

public class HideFAB extends FloatingActionButton.Behavior {

    private int negTicks = 0 , posTicks = 0;

    public HideFAB(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                               View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        L.d("negTicks", negTicks + "");
        L.d("posTIcks", posTicks + "");

        if(!child.isShown() && negTicks > 20) {
            child.show();
            negTicks = 0;
            posTicks = 0;
        } else if(child.isShown() && posTicks > 20) {
            child.hide();
            negTicks = 0;
            posTicks = 0;
        }

        if(child.isShown() && dyConsumed > 0) {
            posTicks++;
        } else if(!child.isShown() && dyConsumed < 0) {
            negTicks++;
        }

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
