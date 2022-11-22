package com.jkcq.hrwtv.wu.newversion.adapter;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by peng on 2018/4/20.
 */

public class CourseSelectItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    public CourseSelectItemDecoration(int space) {
        this.mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) < 3) {
            outRect.top = mSpace;
        }
        if ((parent.getChildAdapterPosition(view) + 1) % 3 == 0) {
            outRect.right = mSpace;
        }

    }

}
