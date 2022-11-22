package com.jkcq.hrwtv.wu.newversion.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.wu.newversion.adapter.ManItemDecoration;
import com.jkcq.hrwtv.wu.newversion.adapter.ManSelectAdapter;
import com.jkcq.hrwtv.wu.newversion.view.tvrecyclerview.TvRecyclerView;

public class SelectPopupWindow extends PopupWindow {

    private static final String TAG = SelectPopupWindow.class.getSimpleName();
    private Activity mActivity;
    private TvRecyclerView tv_recyclerview;
    private View mContentView;
    private ImageView iv_close;
    private ImageView iv_point;
    private LinearLayout ll_change_view;

    public SelectPopupWindow(Activity activity) {
        mActivity = activity;
        init();
    }


    private void init() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.popup_man_select, null);
        setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(true);
        setFocusable(true);

        iv_close = mContentView.findViewById(R.id.iv_close);
        iv_point = mContentView.findViewById(R.id.iv_point);
        ll_change_view = mContentView.findViewById(R.id.ll_change_view);
        iv_close.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iv_close.setImageResource(R.mipmap.icon_close_selected);
                } else {
                    iv_close.setImageResource(R.mipmap.icon_close_unselected);
                }
            }
        });
        ll_change_view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iv_point.setImageResource(R.mipmap.icon_hide_point_select);
                } else {
                    iv_point.setImageResource(R.mipmap.icon_hide_point_unselect);
                }
            }
        });
        initRecyclerView();
    }


    public void initRecyclerView() {
        tv_recyclerview = mContentView.findViewById(R.id.tv_recyclerview);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 7);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        tv_recyclerview.setLayoutManager(manager);
        tv_recyclerview.addItemDecoration(new ManItemDecoration(10));

        ManSelectAdapter mAdapter = new ManSelectAdapter(mActivity);
        tv_recyclerview.setAdapter(mAdapter);
        setTV();
    }


    /**
     * 设置TV相关属性
     */
    private void setTV() {

//        tv_recyclerview.setOnItemStateListener(new TvRecyclerView.OnItemStateListener() {
//            @Override
//            public void onItemViewClick(View view, int position) {
//                Log.i(TAG, "you click item position: " + position);
//            }
//
//            @Override
//            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
//                Log.i(TAG, "onItemViewFocusChanged " + position);
//            }
//        });
    }

    public void showDefault() {
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

}
