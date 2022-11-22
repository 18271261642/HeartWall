package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.adapter.RecycleViewDivider;
import com.jkcq.hrwtv.adapter.TimerViewAdapter;
import com.jkcq.hrwtv.eventBean.BackEvent;
import com.jkcq.hrwtv.heartrate.bean.TimerBean;
import com.jkcq.hrwtv.util.DisplayUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by peng on 2018/5/9.
 */

public class TimerSetView extends LinearLayout {
    Unbinder unbinder;

    ArrayList<TimerBean> timerBeans = new ArrayList<>();
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.btn_not_all)
    TextView btnNotAll;
    @BindView(R.id.recyclerview)
    com.jkcq.hrwtv.ui.view.recyclerview.TvRecyclerview recyclerview;
    @BindView(R.id.rl_layout)
    LinearLayout rlLayout;

    TimerViewAdapter adapter;
    LinearLayoutManager layoutManager;


    public TimerSetView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fragment_other_menu_layout, this);
        ButterKnife.bind(this);
        adapter = new TimerViewAdapter(context);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerview.addItemDecoration(new RecycleViewDivider(context, HORIZONTAL, DisplayUtils.dip2px(context, 1), getResources().getColor(com.jkcq.hrwtv.base.R.color.white_30)));
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.requestFocusFromTouch();
        recyclerview.requestFocus();
        intData();
    }

    public TimerSetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);

    }

    public TimerSetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void intData() {
        TimerBean timerBean1 = new TimerBean();
        timerBean1.setTime("00: 30");
        timerBean1.setHour(0);
        timerBean1.setMin(0);
        timerBean1.setMills(30);
        timerBeans.add(timerBean1);
        TimerBean timerBean2 = new TimerBean();
        timerBean2.setTime("01: 00");
        timerBean2.setHour(0);
        timerBean2.setMin(1);
        timerBean2.setMills(0);
        timerBeans.add(timerBean2);
        TimerBean timerBean3 = new TimerBean();
        timerBean3.setTime("01: 30");
        timerBean3.setHour(0);
        timerBean3.setMin(1);
        timerBean3.setMills(30);
        timerBeans.add(timerBean3);
        TimerBean timerBean4 = new TimerBean();
        timerBean4.setTime("02: 00");
        timerBean4.setHour(0);
        timerBean4.setMin(2);
        timerBean4.setMills(0);
        timerBeans.add(timerBean4);
        TimerBean timerBean5 = new TimerBean();
        timerBean5.setTime("03: 00");
        timerBean5.setHour(0);
        timerBean5.setMin(3);
        timerBean5.setMills(0);
        timerBeans.add(timerBean5);
        TimerBean timerBean6 = new TimerBean();
        timerBean6.setTime("05: 00");
        timerBean6.setHour(0);
        timerBean6.setMin(5);
        timerBean6.setMills(0);
        timerBeans.add(timerBean6);
        TimerBean timerBean7 = new TimerBean();
        timerBean7.setTime("10: 00");
        timerBean7.setHour(0);
        timerBean7.setMin(10);
        timerBean7.setMills(00);
        timerBeans.add(timerBean7);
        TimerBean timerBean8 = new TimerBean();
        timerBean8.setTime("15: 00");
        timerBean8.setHour(0);
        timerBean8.setMin(15);
        timerBean8.setMills(0);
        timerBeans.add(timerBean8);
        adapter.setData(timerBeans);
        recyclerview.setAdapter(adapter);

    }

   /* @OnClick({R.id.iv_back, R.id.btn_30min, R.id.btn_one_hours, R.id.btn_one_hours_30min, R.id.btn_two_hours, R.id.btn_three_hours, R.id.btn_five_hours, R.id.btn_ten_hours, R.id.btn_fiveteen_hours, R.id.rl_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(new BackEvent(true, Constant.MENU_TIMER));
                break;
            case R.id.btn_30min:
                */

    /**
     * 跳转到计时的fragment中去
     *//*
                sendEvent(timerBeans.get(0));
                break;
            case R.id.btn_one_hours:
                sendEvent(timerBeans.get(1));
                break;
            case R.id.btn_one_hours_30min:
                sendEvent(timerBeans.get(2));
                break;
            case R.id.btn_two_hours:
                sendEvent(timerBeans.get(3));
                break;
            case R.id.btn_three_hours:
                sendEvent(timerBeans.get(4));
                break;
            case R.id.btn_five_hours:
                sendEvent(timerBeans.get(5));
                break;
            case R.id.btn_ten_hours:
                sendEvent(timerBeans.get(6));
                break;
            case R.id.btn_fiveteen_hours:
                sendEvent(timerBeans.get(7));
                break;
            case R.id.rl_layout:
                break;
        }
    }
*/
    @OnClick({R.id.iv_back, R.id.rl_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(new BackEvent(true));
                break;
            case R.id.rl_layout:
                break;
        }
    }
}
