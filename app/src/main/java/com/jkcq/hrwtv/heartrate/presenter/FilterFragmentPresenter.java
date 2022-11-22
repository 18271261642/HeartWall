package com.jkcq.hrwtv.heartrate.presenter;

import com.jkcq.hrwtv.base.mvp.BasePresenter;
import com.jkcq.hrwtv.heartrate.bean.User;
import com.jkcq.hrwtv.heartrate.model.filtershowmodel.FilterFragmentModel;
import com.jkcq.hrwtv.heartrate.model.filtershowmodel.FilterFragmentModelImp;
import com.jkcq.hrwtv.heartrate.model.filtershowmodel.FilterFragmentView;

import java.util.List;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/19 17:32 
 */
public class FilterFragmentPresenter extends BasePresenter<FilterFragmentView> implements FilterFragmentView, FilterFragmentModel {

    FilterFragmentModel model;

    public FilterFragmentPresenter() {
        model = new FilterFragmentModelImp(context, this) ;
    }

    @Override
    public void getUserInfos(String deviceId) {

    }

    @Override
    public void onRespondError(String message) {

    }

    @Override
    public void getUserInfos(List<User> info) {

    }
}
