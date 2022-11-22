package com.jkcq.hrwtv.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;
import com.jkcq.hrwtv.ui.view.recyclerview.adapter.BaseCommonRefreshRecyclerAdapter;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/19 10:55 
 */
public class TaskAdapter extends BaseCommonRefreshRecyclerAdapter<TaskInfo, TaskAdapter.TaskViewHolder> {


    public TaskAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_task_layout;
    }

    @Override
    protected TaskViewHolder bindBaseViewHolder(View contentView) {
        return new TaskViewHolder(contentView);
    }

    @Override
    protected void initData(TaskViewHolder viewHolder, int position, TaskInfo item) {
//        viewHolder.icon.setImageBitmap();
        viewHolder.itemView.setFocusable(true);
        viewHolder.taskName.setText(item.getName());

    }

    @Override
    protected void updatItemData(TaskViewHolder viewHolder, int position, TaskInfo item) {

    }

    @Override
    protected void initEvent(TaskViewHolder viewHolder, int position, TaskInfo item) {

    }

    public class TaskViewHolder extends BaseViewHolder {

        private TextView taskName;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
        }
    }


}
