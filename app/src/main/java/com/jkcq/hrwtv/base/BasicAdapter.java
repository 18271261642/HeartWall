package com.jkcq.hrwtv.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ListView的Adapter的基类。
 * @version 创建时间：
 */
public abstract class BasicAdapter<T> extends BaseAdapter {
    /**
     * 上下文参数
     */
    protected Context mContext;
    /**
     * 数据源《泛型》
     */
    protected List<T> listSource = new ArrayList<>();

    public BasicAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public BasicAdapter(Context mContext, List<T> listSource) {
        this.mContext = mContext;
        this.listSource = listSource;
    }

    // 提供一个公共的方法，用于更新ListView的list 数据，并刷新ListView。
    public void setData(List<T> mList) {
        this.listSource = mList;
        notifyDataSetChanged();
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

    public List<T> getData() {
        return listSource;
    }

    @Override
    public int getCount() {
        return (null == listSource) ? 0 : listSource.size();
    }

    @Override
    public T getItem(int position) {
        return (null == listSource || position >= listSource.size()) ? null : listSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup viewGroup) {

        BaseHolder<T> holder = null;
        if (contentView == null) {
            holder = getBaseHolder(position);

            int layoutResID = holder.getInflateLayoutId();
            if (layoutResID <= 0) {
                throw new NullPointerException("view id is 0.");
            }
            contentView = LayoutInflater.from(mContext).inflate(layoutResID,
                    viewGroup, false);
            holder.inflateViewAndFindView(contentView, position);
            contentView.setTag(holder);
        } else {
            holder = (BaseHolder<T>) contentView.getTag();
        }
        T item = getItem(position);
        holder.bindView(item, position);
        return contentView;
    }

    // 子类实现，根据具体的场景。提供具体的BaseHolder实现类。
    public abstract BaseHolder<T> getBaseHolder(int position);
}