package com.jkcq.hrwtv.wu.newversion.adapter;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by peng on 2018/4/20.
 */

public class ManItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //不是第一个的格子都设一个左边和底部的间距
        outRect.right = mSpace;
        outRect.left = mSpace;
        outRect.top = mSpace * 2;

        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        /*if (parent.getChildLayoutPosition(view) %4==0) {
            outRect.left = 0;
        }*/
        /**
         * 设置每个item的边距，把第一个item不要设置top的高度
         */
//        if (parent.getChildAdapterPosition(view) == 0) {
//            outRect.top = 0;
//        }

    }

    public ManItemDecoration(int space) {
        this.mSpace = space;
    }

}
