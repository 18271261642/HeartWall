package com.jkcq.hrwtv.ui.view;


import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.adapter.MainMenuAdater;
import com.jkcq.hrwtv.adapter.RecycleViewDivider;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.eventBean.BackEvent;
import com.jkcq.hrwtv.heartrate.bean.MenuBean;
import com.jkcq.hrwtv.util.DisplayUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by peng on 2018/5/9.
 */

public class MenuView extends LinearLayout {
    @BindView(R.id.recyclerview)
    com.jkcq.hrwtv.ui.view.recyclerview.TvRecyclerview recyclerview;
    @BindView(R.id.rl_layout)
    LinearLayout rlLayout;
    RecyclerView.LayoutManager layoutManager;
    Context mContext;
    MainMenuAdater menuAdapter;
    ArrayList<MenuBean> list;

    public MenuView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.main_menu_layout, this);
        mContext = context;
        ButterKnife.bind(this);
        intData();
        intView();
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.main_menu_layout, this);
        ButterKnife.bind(this);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void intData() {
        list = new ArrayList<>();
        MenuBean bean1 = new MenuBean();
        bean1.setResId(R.drawable.icon_menu_couser_selector);
        bean1.setMenuType(Constant.MENU_TASK);
        bean1.setName("心率课程");

        MenuBean bean2 = new MenuBean();
        bean2.setResId(R.drawable.icon_menu_timer_selector);
        bean2.setMenuType(Constant.MENU_TIMER);
        bean2.setName("计时器");

        MenuBean bean3 = new MenuBean();
        bean3.setResId(R.drawable.icon_menu_display_selector);
        bean3.setMenuType(Constant.MENU_DISPLAY);
        bean3.setName("显示主数据");

        MenuBean bean4 = new MenuBean();
        bean4.setResId(R.drawable.icon_menu_sort_selector);
        bean4.setMenuType(Constant.MENU_SORT);
        bean4.setName("排序规则");

        MenuBean bean5 = new MenuBean();
        bean5.setMenuType(Constant.MENU_CLUB_NAME);
        bean5.setResId(R.drawable.icon_menu_setting_selector);
        bean5.setName("设置");

        MenuBean bean6 = new MenuBean();
        bean6.setMenuType(Constant.MENU_FILTER_USER);
        bean6.setResId(R.drawable.icon_menu_filter_user_selector);
        bean6.setName("筛选显示");

        list.add(bean5);
        list.add(bean6);
        list.add(bean4);
        list.add(bean3);
        list.add(bean2);
        list.add(bean1);


    }

    private void intView() {

        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, true);
        recyclerview.setLayoutManager(layoutManager);
        menuAdapter = new MainMenuAdater(mContext);
        menuAdapter.setData(list);
        recyclerview.addItemDecoration(new RecycleViewDivider(mContext, HORIZONTAL, DisplayUtils.dip2px(mContext, 1), getResources().getColor(com.jkcq.hrwtv.base.R.color.white_30)));
        recyclerview.setAdapter(menuAdapter);
        if(list.size()!=0){
            recyclerview.requestFocusFromTouch();
            recyclerview.requestFocus();
        }

    }


    private void sendEvent(String tofragment) {
        BackEvent event = new BackEvent();
        event.setToFragment(tofragment);
        EventBus.getDefault().post(event);
    }
}
