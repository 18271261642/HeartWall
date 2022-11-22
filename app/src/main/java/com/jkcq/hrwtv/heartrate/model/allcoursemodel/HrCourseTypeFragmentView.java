package com.jkcq.hrwtv.heartrate.model.allcoursemodel;

import com.jkcq.hrwtv.base.mvp.BaseView;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;

import java.util.ArrayList;

/*
 * 
 * 
 * @author fly
 * Create at 2018/4/17 10:45 
 */
public interface HrCourseTypeFragmentView extends BaseView {
    void getAllCourseTypeSuccess(ArrayList<TaskInfo> taskInfos);
}
