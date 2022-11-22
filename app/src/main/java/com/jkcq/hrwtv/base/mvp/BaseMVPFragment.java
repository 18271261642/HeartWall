
package com.jkcq.hrwtv.base.mvp;

import android.content.Context;

import com.jkcq.hrwtv.base.BaseFragment;
import com.jkcq.hrwtv.util.ToastUtils;


/**
 * ClassName:BaseMVPFragment <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2017年1月10日 下午4:13:18 <br/>
 *
 * @author Administrator
 */
public abstract class BaseMVPFragment<V extends BaseView, P extends BasePresenter<V>>
        extends BaseFragment implements BaseView {
    // Persenter类的实例。
    public P mFragPresenter;

    @Override
    public void onAttach(Context context) {
        mFragPresenter = createPersenter();
        if (mFragPresenter != null) {
            // BasePersenter类的方法。主要用于将View用弱引用赋值给P层的View对象
            mFragPresenter.attach(context, (V) this);
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        if (mFragPresenter != null) {
            // BasePersenter类的方法。主要用于将View的引用清除。
            mFragPresenter.detach();
        }
        super.onDetach();
    }

    // 子类实现，具体类型创建具体P层对象。
    protected abstract P createPersenter();

    @Override
    public void onRespondError(String message) {
        netError();
        ToastUtils.showToast(context, message);
    }
}