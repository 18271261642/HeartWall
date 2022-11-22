package com.jkcq.hrwtv.wu.newversion.adapter;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by peng on 2018/4/20.
 */

public class HWallDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    public HWallDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = mSpace;
        outRect.bottom = mSpace;
        int size = parent.getAdapter().getItemCount();
        if (size <= 6) {
            if (parent.getChildAdapterPosition(view) < 3) {
                outRect.top = mSpace;
            }
        } else {
            if (parent.getChildAdapterPosition(view) <= 3) {
                outRect.top = mSpace;
            }
        }
        if ((parent.getChildAdapterPosition(view)+1) % 4 == 0) {
            outRect.right = mSpace;
        }

    }

}
