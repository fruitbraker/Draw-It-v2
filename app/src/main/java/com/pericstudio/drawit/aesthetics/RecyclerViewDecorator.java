package com.pericstudio.drawit.aesthetics;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewDecorator extends RecyclerView.ItemDecoration{

    public RecyclerViewDecorator() {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        L.d("RecyclerViewDecorator", parent.getChildAdapterPosition(view) + "");
        if(parent.getChildAdapterPosition(view)%2 == 0) {
            //White
            view.setBackgroundColor(Color.rgb(255, 255, 255));
        } else {
            //Light Blue
            view.setBackgroundColor(Color.rgb(209, 246, 252));
        }

    }
}
