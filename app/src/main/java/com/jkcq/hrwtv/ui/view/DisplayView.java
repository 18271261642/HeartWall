package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.adapter.RecycleViewDivider;
import com.jkcq.hrwtv.adapter.SortOrShowFragmentAdapter;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.eventBean.BackEvent;
import com.jkcq.hrwtv.heartrate.bean.TypeBean;
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

public class DisplayView extends LinearLayout {
    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView btnBack;
    @BindView(R.id.recyclerview)
    com.jkcq.hrwtv.ui.view.recyclerview.TvRecyclerview recyclerview;
    ArrayList arrayList = new ArrayList();

    SortOrShowFragmentAdapter adapter;
    LinearLayoutManager layoutManager;

    public DisplayView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.fragment_other_menu_layout, this);
        ButterKnife.bind(this);
        adapter = new SortOrShowFragmentAdapter(context);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerview.addItemDecoration(new RecycleViewDivider(context, HORIZONTAL, DisplayUtils.dip2px(context, 1), getResources().getColor(com.jkcq.hrwtv.base.R.color.white_30)));
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.smoothScrollToPosition(0);
        recyclerview.requestFocusFromTouch();
        recyclerview.requestFocus();
        initData();

    }

    public DisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);

    }

    public DisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    protected void initData() {

//        DeviceBean bean = AllocationApi.getUserInfo();
//        String sortType;
//        if (bean != null) {
//            sortType = bean.getSortType();
//        } else {
//            sortType = Constant.TYPE_POINT;
//        }

        TypeBean bean1 = new TypeBean();
        bean1.setResID(R.drawable.icon_menu_sort_precent_selector);
        bean1.setName("百分比");
        bean1.setSortType(Constant.TYPE_PERCENT);
        TypeBean bean2 = new TypeBean();
        bean2.setResID(R.drawable.icon_menu_sort_point_selector);
        bean2.setName("点数");
        bean2.setSortType(Constant.TYPE_POINT);
        TypeBean bean3 = new TypeBean();
        bean3.setResID(R.drawable.icon_menu_sort_hr_selector);
        bean3.setSortType(Constant.TYPE_HR);
        bean3.setName("心率值");
        TypeBean bean4 = new TypeBean();
        bean4.setSortType(Constant.TYPE_CAL);
        bean4.setResID(R.drawable.icon_menu_sort_cal_selector);
        bean4.setName("卡路里");
        arrayList.add(bean1);
        arrayList.add(bean2);
        arrayList.add(bean3);
        arrayList.add(bean4);
        adapter.setData(arrayList);
        recyclerview.setAdapter(adapter);
    }


    @OnClick({R.id.iv_back, R.id.recyclerview, R.id.rl_layout})
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
