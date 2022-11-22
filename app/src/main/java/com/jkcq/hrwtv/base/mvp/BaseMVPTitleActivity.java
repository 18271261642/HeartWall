package com.jkcq.hrwtv.base.mvp;

import android.os.Bundle;

import com.jkcq.hrwtv.base.BaseTitleActivity;
import com.jkcq.hrwtv.util.ToastUtils;


/**
 * ClassName:BaseActActivity <br/>
 * Function: MVP模式 Activity基类，继承BaseActivity. T ：为View层的实现类。 P为Persenter的具体实现类<br/>
 * Date: 2016年12月23日 下午1:58:53 <br/>
 *
 * @author Administrator
 */
public abstract class BaseMVPTitleActivity<V extends BaseView, P extends BasePresenter<V>>
        extends BaseTitleActivity implements BaseView {

    // Persenter类的实例。
    public P mActPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActPresenter = createPresenter();
        super.onCreate(savedInstanceState);
        if (mActPresenter != null) {
            // BasePersenter类的方法。主要用于将View用弱引用赋值给P层的View对象
            mActPresenter.attach(context, (V) this);
        }
    }

    // 子类实现，具体类型创建具体P层对象。
    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        if (mActPresenter != null) {
            // BasePersenter类的方法。主要用于将View的引用清除。
            mActPresenter.detach();
        }
        super.onDestroy();
    }

    @Override
    public void onRespondError(String message) {
        netError(BaseMVPTitleActivity.this);
        ToastUtils.showToast(context, message);
    }
}