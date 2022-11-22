package com.jkcq.hrwtv.ui.view.recyclerview.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;


import com.jkcq.hrwtv.util.DisplayUtils;
import com.jkcq.hrwtv.util.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * 重写并继承RecyclerView<p/>
 * 添加header footer emptyView
 * com.snscity.globalexchange.view.recyclerview
 *
 * @author 苗恒聚 <br/>
 *         create at 2015-12-25 下午3:27:05
 */
public abstract class BaseCommonRefreshRecyclerAdapter<T, V extends BaseCommonRefreshRecyclerAdapter.BaseViewHolder> extends RecyclerView.Adapter<V> {

    /**
     * 上下文参数
     */
    protected Context mContext;

    public AdapterView.OnItemClickListener mOnItemClickListener;

    public AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    protected static final int TYPE_HEADER = 0;

    protected static final int TYPE_FOOTER = 1;

    protected static final int TYPE_ITEM = 2;

    protected static final int TYPE_EMPTY = 3;

    public View mHeaderView;

    public View mFooterView;

    public View mEmptyViewLayout;

    /**
     * 是否正在加载数据
     */
    private boolean isLoading = true;

    /**
     * 是否开启item列表动画 默认开启
     */
    private boolean animateItems = true;

    /**
     * 最后开启动画的position
     */
    private int lastAnimatedPosition = -1;

    public BaseCommonRefreshRecyclerAdapter(Context mContext) {
        super();
        this.mContext = mContext;

    }

    /**
     * 数据源《泛型》
     */
    protected List<T> listSource = new ArrayList<T>();

    public List<T> getData() {
        return listSource;
    }

    /**
     * 设置数据源 清空原有数据
     *
     * @param listSource
     */
    public void setData(List<T> listSource) {
        this.listSource = listSource;
    }

    /**
     * 追加数据 不清空原有数据
     *
     * @param listSource
     */
    public void appendData(List<T> listSource) {
        if (null == listSource || listSource.isEmpty()) {
            return;
        }
        if (null == this.listSource) {
            this.listSource = new ArrayList<T>();
        }
        this.listSource.addAll(listSource);
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER && mHeaderView != null) {
            return (V) new SimpleViewHolder(mHeaderView);
        } else if (viewType == TYPE_FOOTER && mFooterView != null) {
            return (V) new SimpleViewHolder(mFooterView);
        } else if (viewType == TYPE_EMPTY && mEmptyViewLayout != null) {
            return (V) new SimpleViewHolder(mEmptyViewLayout);
        }
        return (V) onRealCreateViewHolder(parent, viewType);
    }

//    protected abstract BaseViewHolder onRealCreateViewHolder(ViewGroup parent, int viewType);

    protected BaseViewHolder onRealCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(getLayoutId(), parent, false);
        return bindBaseViewHolder(view);
    }

    protected void onRealBindViewHolder(final BaseViewHolder viewHolder, int position) {
        T item = getItem(position);
        initData((V) viewHolder, position, item);
        initEvent((V) viewHolder, position, item);
    }

    @Override
    public int getItemCount() {
        int size = getRealItemCount();
        if (size == 0 && null != mEmptyViewLayout) {
            size = 1;
        } else {
            if (null != mHeaderView)
                size += getHeadViewSize();
            if (null != mFooterView)
                size += getFooterViewSize();
        }
        return size;
    }

    /**
     * 获取实际item count
     *
     * @return
     */
    protected int getRealItemCount() {
        return (null == listSource) ? 0 : listSource.size();
    }

    public T getItem(int position) {
        return (null == listSource || position >= listSource.size()) ? null : listSource.get(position);
    }

    public List<T> getAll() {
        return (null == listSource) ? null : listSource;
    }

    public void removeItem(int position) {
        if (position < listSource.size() && position >= 0) {
            listSource.remove(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        int size = getItemCount();
//        if (size == 0 && mEmptyViewLayout != null) {
//            return TYPE_EMPTY;
//        } else if (position < getHeadViewSize()) {
//            return TYPE_HEADER;
//        } else if (position >= getRealItemCount() + getHeadViewSize()) {
//            return TYPE_FOOTER;
//        }
//        return TYPE_ITEM;
        return super.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
//        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//        if (manager instanceof GridLayoutManager) {
//            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    DebugLog.e("onAttachedToRecyclerView position = " + position + " ,viewType = " + getItemViewType(position) + " ,spanCount = " + gridManager.getSpanCount());
//                    return (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
//                            ? 1 : gridManager.getSpanCount();
//                }
//            });
//        }
    }

    @Override
    public void onViewAttachedToWindow(V holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (null == lp || !(lp instanceof StaggeredGridLayoutManager.LayoutParams)) {
            return;
        }
        int position = holder.getLayoutPosition();
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    /**
     * 载入ViewHolder，这里仅仅处理header和footer视图的逻辑
     */
    @Override
    public void onBindViewHolder(final V viewHolder, int position) {
        if (getItemCount() == 0 && getItemCount() == 1 && null != mEmptyViewLayout && position == 0) {
            //处理emptyView
        } else if (null != mHeaderView && position < getHeadViewSize()) {
            //处理headView
        } else if (null != mFooterView && position >= getRealItemCount() + getHeadViewSize()) {
            //处理footView
        } else {
            if (mHeaderView != null) {
                position -= getHeadViewSize();
            }

            //runEnterAnimation(viewHolder.itemView, position);
            onRealBindViewHolder(viewHolder, position);

            final int pos = position;
            // 设置点击事件
            if (mOnItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(null, viewHolder.itemView, pos, pos);
                    }
                });
            }
            // 设置长按事件
            if (mOnItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return mOnItemLongClickListener.onItemLongClick(null, viewHolder.itemView, pos, pos);
                    }
                });
            }
        }
    }

    @Override
    public void onBindViewHolder(V holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //注意：payloads的size总为1
            String payload = (String) payloads.get(0);
            Logger.d(payload);
            T item = getItem(position);
            updatItemData((V) holder, position, item);
            //需要更新的UI控件
        }
    }

    protected void updateItems(boolean animated) {
        animateItems = animated;
    }

    private final void runEnterAnimation(View view, int position) {
        if (!animateItems || null == view) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            startItemAnim(view);
        }
    }

    protected void startItemAnim(View view) {
        view.setTranslationY(DisplayUtils.getScreenHeight(mContext));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(50)
                .start();
    }


//    protected abstract void onRealBindViewHolder(final BaseViewHolder viewHolder, int position);

    /**
     * 获取layout Id
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    /**
     * 绑定ViewHolder
     *
     * @param contentView
     * @return
     */
    protected abstract V bindBaseViewHolder(View contentView);

    /**
     * 数据处理
     *
     * @param viewHolder
     * @param position
     * @param item
     */
    protected abstract void initData(final V viewHolder, final int position, final T item);

    protected abstract void updatItemData(final V viewHolder, final int position, final T item);


    /**
     * 设置监听
     *
     * @param viewHolder
     * @param position
     * @param item
     */
    protected abstract void initEvent(final V viewHolder, final int position, final T item);

    public int getHeadViewSize() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewSize() {
        return mFooterView == null ? 0 : 1;
    }

    //remove a header from the adapter
    public void removeHeader(View header) {
        notifyItemRemoved(0);
        mHeaderView = null;
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class SimpleViewHolder extends BaseViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
