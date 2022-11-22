package com.jkcq.hrwtv.heartrate.model.filtershowmodel;

import com.jkcq.hrwtv.base.mvp.BaseView;
import com.jkcq.hrwtv.heartrate.bean.User;

import java.util.List;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/19 17:30 
 */
public interface FilterFragmentView extends BaseView{
    void getUserInfos(List<User> info);
}
