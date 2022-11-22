package com.jkcq.hrwtv.heartrate.model.coursedtailmode;

import com.jkcq.hrwtv.base.mvp.BaseView;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/19 17:30 
 */
public interface CourseDetailFragmentView extends BaseView{
    void getTaskSuccess(TaskInfo info);
}
