package com.jkcq.hrwtv.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.eventBean.InvisibleMenuLayoutEvent;
import com.jkcq.hrwtv.eventBean.TimerBeanEvent;
import com.jkcq.hrwtv.eventBean.UpdateMenuEvent;
import com.jkcq.hrwtv.heartrate.bean.TimerBean;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.ui.view.recyclerview.adapter.BaseCommonRefreshRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by peng on 2018/5/5.
 */

public class TimerViewAdapter extends BaseCommonRefreshRecyclerAdapter<TimerBean, TimerViewAdapter.MyViewHolder> {


    public TimerViewAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_task_layout;
    }

    @Override
    protected MyViewHolder bindBaseViewHolder(View contentView) {
        return new MyViewHolder(contentView);
    }

    @Override
    protected void initData(MyViewHolder viewHolder, int position, TimerBean item) {
        viewHolder.itemView.setFocusable(true);
        viewHolder.taskName.setText(item.getTime());
    }

    @Override
    protected void updatItemData(MyViewHolder viewHolder, int position, TimerBean item) {

    }

    @Override
    protected void initEvent(MyViewHolder viewHolder, int position, final TimerBean item) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEvent(item);
            }
        });
    }

    public class MyViewHolder extends BaseViewHolder {
        @BindView(R.id.task_name)
        TextView taskName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    private void sendEvent(TimerBean timerBean) {
        CacheDataUtil.saveTimer(true, false);//存缓存
        //数据带到页面
        EventBus.getDefault().post(new TimerBeanEvent(timerBean));
        EventBus.getDefault().post(new InvisibleMenuLayoutEvent());
        /**
         * 刷新界面的UI
         */
        EventBus.getDefault().post(new UpdateMenuEvent(true));
    }
}
