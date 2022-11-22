package com.jkcq.hrwtv.base;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * classes : com.xingye.project.base
 *
 * @author 苗恒聚
 *         V 1.0.0
 *         Create at 16/7/31 16:31
 */
public abstract class BaseFragmentStatePagerAdapter<T> extends FragmentStatePagerAdapter {

    /**
     * 上下文参数
     */
    protected Context mContext;

    /**
     * 数据源《泛型》
     */
    protected List<T> listSource = new ArrayList<T>();

    public BaseFragmentStatePagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

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
    public int getCount() {
        return (null == listSource) ? 0 : listSource.size();
    }

    public Object getObject(int position) {
        return (null == listSource || position >= listSource.size()) ? null : listSource.get(position);
    }
}
