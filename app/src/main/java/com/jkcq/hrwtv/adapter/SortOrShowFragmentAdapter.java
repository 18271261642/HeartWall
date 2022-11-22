package com.jkcq.hrwtv.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkcq.hrwtv.R;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.eventBean.InvisibleMenuLayoutEvent;
import com.jkcq.hrwtv.eventBean.UpdateDisplayEvent;
import com.jkcq.hrwtv.eventBean.UpdateSortEvent;
import com.jkcq.hrwtv.heartrate.bean.ShowBean;
import com.jkcq.hrwtv.heartrate.bean.TypeBean;
import com.jkcq.hrwtv.util.ACache;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.ui.view.recyclerview.adapter.BaseCommonRefreshRecyclerAdapter;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by peng on 2018/5/5.
 */

public class SortOrShowFragmentAdapter extends BaseCommonRefreshRecyclerAdapter<TypeBean, SortOrShowFragmentAdapter.MyViewHolder> {

    private Gson mGson;

    public SortOrShowFragmentAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_text_and_image_layout;
    }

    @Override
    protected MyViewHolder bindBaseViewHolder(View contentView) {
        mGson = new Gson();
        return new MyViewHolder(contentView);
    }

    @Override
    protected void initData(MyViewHolder viewHolder, int position, TypeBean item) {
        viewHolder.itemView.setFocusable(true);
        viewHolder.tvName.setText(item.getName());
        viewHolder.ivIcon.setImageResource(item.getResID());
    }

    @Override
    protected void updatItemData(MyViewHolder viewHolder, int position, TypeBean item) {

    }

    @Override
    protected void initEvent(MyViewHolder viewHolder, int position, final TypeBean item) {
        /**
         * 排序跳转的
         */
        viewHolder.itemView.setFocusable(true);
        final boolean isSort = item.isSort();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSort) {
                    //1.需要重新把数据排序
                    //2需要把fragment隐藏
                    //TODO 需要加一个如果是课程时候的排序
                    if (CacheDataUtil.getShowFragment().equals(Constant.MAIN_FRAGMENT)) {
//                        ToastUtils.showToast(mContext, "点击了sort : "+item.getSortType());
                    }
                    EventBus.getDefault().post(new UpdateSortEvent(item.getSortType()));
                    EventBus.getDefault().post(new InvisibleMenuLayoutEvent());
                } else {
                    //TODO 需要加一个如果是课程时候的显示
                    ACache aCache = ACache.get(mContext);
                    ShowBean showBean;
                    if (CacheDataUtil.getShowFragment().equals(Constant.TASK_FRAGMENT)) {
//                        AllocationApi.updateCurseDisplay(item.getSortType());
                        showBean = CacheDataUtil.getTaskDisplayRule();
                    } else {
//                        AllocationApi.updateDisplay(item.getSortType());
                        showBean = CacheDataUtil.getDisplayRule();
                        //修改整个数据的状态
                    }
                    String str = showBean.getCeter();
                    showBean.setCeter(item.getSortType());
                    if (showBean.getOne().equals(item.getSortType())) {
                        showBean.setOne(str);
                    }
                    if (showBean.getTwo().equals(item.getSortType())) {
                        showBean.setTwo(str);
                    }
                    if (showBean.getThree().equals(item.getSortType())) {
                        showBean.setThree(str);
                    }
                    if (showBean.getFour().equals(item.getSortType())) {
                        showBean.setFour(str);
                    }
                    if (CacheDataUtil.getShowFragment().equals(Constant.TASK_FRAGMENT)) {
                        aCache.put(Constant.DISPLAY_COURSE_CACHE_NAME, mGson.toJson(showBean));
                    } else {
                        aCache.put(Constant.DISPLAY_CACHE_NAME, mGson.toJson(showBean));
                        //  ToastUtils.showToast(mContext, "点击了display");
                    }
                    EventBus.getDefault().post(new UpdateDisplayEvent());
                    EventBus.getDefault().post(new InvisibleMenuLayoutEvent());
                }
            }
        });

    }

    public class MyViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
