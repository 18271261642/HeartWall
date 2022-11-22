package com.jkcq.hrwtv.ui.view;


import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.eventBean.BackEvent;
import com.jkcq.hrwtv.eventBean.InvisibleMenuLayoutEvent;
import com.jkcq.hrwtv.eventBean.ShowMainFragmentEvent;
import com.jkcq.hrwtv.util.CacheDataUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by peng on 2018/5/9.
 */

public class TaskResultBackView extends LinearLayout {
    Context mContext;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.btn_not_all)
    TextView btnNotAll;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_stay_current_page)
    TextView tvStayCurrentPage;
    @BindView(R.id.rl_layout)
    LinearLayout rlLayout;

    public TaskResultBackView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.menu_task_result_back_layout, this);
        mContext = context;
        ButterKnife.bind(this);
        intView();
    }

    public TaskResultBackView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.main_menu_layout, this);
        ButterKnife.bind(this);
    }

    public TaskResultBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    private void intView() {

        ivBack.setVisibility(INVISIBLE);

    }



    /*@OnClick({R.id.menu_task, R.id.menu_timer, R.id.menu_display, R.id.menu_order, R.id.menu_club_name, R.id.menu_filter, R.id.rl_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.menu_task:
                sendEvent(Constant.MENU_TASK);
                break;
            case R.id.menu_timer:
                sendEvent(Constant.MENU_TIMER);
                break;
            case R.id.menu_display:
                sendEvent(Constant.MENU_DISPLAY);
                break;
            case R.id.menu_order:
                sendEvent(Constant.MENU_SORT);
                break;
            case R.id.menu_club_name:
                sendEvent(Constant.MENU_CLUB_NAME);
                break;
            case R.id.menu_filter:
                sendEvent(Constant.MENU_FILTER_USER);
                break;
            case R.id.rl_layout:
                break;
        }
    }*/

    private void sendEvent(String tofragment) {
        BackEvent event = new BackEvent();
        event.setToFragment(tofragment);
        EventBus.getDefault().post(event);
    }

    @OnClick({R.id.tv_back, R.id.tv_stay_current_page, R.id.rl_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                /**
                 * 跳转到主页自由页
                 */
                EventBus.getDefault().post(new InvisibleMenuLayoutEvent());
                EventBus.getDefault().post(new ShowMainFragmentEvent());
                CacheDataUtil.clearTask();
                break;
            case R.id.tv_stay_current_page:
                EventBus.getDefault().post(new InvisibleMenuLayoutEvent());
                break;
            case R.id.rl_layout:
                break;
        }
    }
}
