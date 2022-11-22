package com.jkcq.hrwtv.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.jkcq.hrwtv.heartrate.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) 2016,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：
 * 类的描述：
 * 创建人员：Robi
 * 创建时间：2017/03/01 11:58
 * 修改人员：Robi
 * 修改时间：2017/03/01 11:58
 * 修改备注：
 * Version: 1.0.0
 */
public class RLoopRecyclerView extends RecyclerView {
    private static final String TAG = "angcyo";

    public RLoopRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RLoopRecyclerView(Context context) {
        super(context);
    }

    public RLoopRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initView();
    }

    @Override
    public LoopAdapter getAdapter() {
        return (LoopAdapter) super.getAdapter();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof LoopAdapter)) {
            throw new IllegalArgumentException("adapter must  instanceof LoopAdapter!");
        }
        super.setAdapter(adapter);
        //scrollToPosition(getAdapter().getItemRawCount() * 10000);//开始时的偏移量
    }

    private void initView() {
      /*  new RPagerSnapHelper().setOnPageListener(new RPagerSnapHelper.OnPageListener() {
            @Override
            public void onPageSelector(int position) {
                Log.e(TAG, "onPageSelector: " + position % getAdapter().getItemRawCount());
            }
        }).attachToRecyclerView(this);*/
    }

    public static abstract class LoopAdapter<T extends ViewHolder> extends Adapter<T> {

        List<DeviceBean> datas = new ArrayList<>();

        public void setDatas(List<DeviceBean> datas) {
            this.datas = datas;
            notifyDataSetChanged();
        }

        /**
         * 真实数据的大小
         */
        public int getItemRawCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        final public int getItemViewType(int position) {
            return getLoopItemViewType(position % getItemRawCount());
        }

        protected int getLoopItemViewType(int position) {
            return 0;
        }

        @Override
        final public void onBindViewHolder(T holder, int position) {
            onBindLoopViewHolder(holder, position % getItemRawCount());
        }

        public abstract void onBindLoopViewHolder(T holder, int position);

        @Override
        final public int getItemCount() {
            int rawCount = getItemRawCount();
            if (rawCount >8) {
                return Integer.MAX_VALUE;
            }
            return rawCount;
        }
    }
}
