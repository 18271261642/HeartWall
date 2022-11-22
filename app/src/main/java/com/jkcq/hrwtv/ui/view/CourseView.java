package com.jkcq.hrwtv.ui.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.adapter.RecycleViewDivider;
import com.jkcq.hrwtv.adapter.TaskAdapter;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.eventBean.BackEvent;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;
import com.jkcq.hrwtv.heartrate.model.allcoursemodel.HrCourseTypeFragmentView;
import com.jkcq.hrwtv.heartrate.presenter.CourseAllFragmentPresenter;
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

public class CourseView extends LinearLayout implements HrCourseTypeFragmentView {
    public CourseAllFragmentPresenter mFragPresenter;
    Unbinder unbinder;
    @BindView(R.id.iv_back)
    ImageView btnBack;
    @BindView(R.id.recyclerview)
    com.jkcq.hrwtv.ui.view.recyclerview.TvRecyclerview recyclerview;
    private TaskAdapter taskAdapter;

    Context mContext;

    public CourseView(Context context) {
        super(context);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.fragment_other_menu_layout, this);
        ButterKnife.bind(this);
        mFragPresenter = new CourseAllFragmentPresenter();
        mFragPresenter.attach(context, this);
        mFragPresenter.getAllCourseType(context);
    }



    protected void initData() {
        //课程列表
        LinearLayoutManager taskLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(taskLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new RecycleViewDivider(mContext, HORIZONTAL, DisplayUtils.dip2px(mContext, 1), getResources().getColor(com.jkcq.hrwtv.base.R.color.white_30)));
        recyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AllocationApi.setCurrentTask(taskAdapter.getItem(position));
//                CourseDetailFragment taskFragment = new CourseDetailFragment();
                // (context).showFragment(taskFragment);
                listener.onViewClick(Constant.MENU_TASK);
            }
        });

    }

    private OnViewClickListener listener;

    public void setOnViewClickListener(OnViewClickListener listener){
        this.listener = listener;
    }


    public CourseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @OnClick({R.id.iv_back, R.id.recyclerview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                EventBus.getDefault().post(new BackEvent(true));
                break;
            case R.id.recyclerview:
                break;
        }
    }




    @Override
    public void onRespondError(String message) {

    }

    @Override
    public void getAllCourseTypeSuccess(ArrayList<TaskInfo> taskInfos) {
        initData();
        taskAdapter = new TaskAdapter(mContext);
        taskAdapter.setData(taskInfos);
        recyclerview.setAdapter(taskAdapter);
        if(taskInfos.size()!=0){
            recyclerview.requestFocusFromTouch();
            recyclerview.requestFocus();
        }
    }
}
