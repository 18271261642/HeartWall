package com.jkcq.hrwtv.base;

import android.view.View;

/**
 * ClassName:BaseHolder <br/>
 * Function: BaseHolder的基类. <br/>
 * Date: 2016年12月2日 下午5:39:51 <br/>
 *
 * @author Administrator
 */
public abstract class BaseHolder<T> {

    public abstract int getInflateLayoutId();

    //给子类具体实现。根据list的具体状况和具体的postion返回相应的View item 条目。
    public abstract void inflateViewAndFindView(View view, int position);

    //bindView方法，讲Item所需要的数据以参数的形式传入，在此方法中给具体的item中的View赋值。
    public abstract void bindView(T item, int position);

    public abstract BaseHolder<T> onCreateViewHolder(View contentView);
}
