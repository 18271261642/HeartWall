package com.jkcq.hrwtv.heartrate.presenter;

import android.content.Context;

import com.jkcq.hrwtv.base.mvp.BasePresenter;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;
import com.jkcq.hrwtv.heartrate.model.coursedtailmode.CourseDetialFragmentModel;
import com.jkcq.hrwtv.heartrate.model.coursedtailmode.CourseDetialFragmentModelImp;
import com.jkcq.hrwtv.heartrate.model.coursedtailmode.CourseDetailFragmentView;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/19 17:32 
 */
public class CourseDetialFragmentPresenter extends BasePresenter<CourseDetailFragmentView> implements CourseDetailFragmentView,CourseDetialFragmentModel {

    CourseDetialFragmentModel model;

    public CourseDetialFragmentPresenter() {
        model = new CourseDetialFragmentModelImp(context,this);
    }

    @Override
    public void onRespondError(String message) {
        if(isViewAttached()){
            mActView.get().onRespondError(message);
        }
    }

    @Override
    public void getTaskDetails(Context context,String taskId, String userid) {
        model.getTaskDetails(context,taskId,userid);
    }

    @Override
    public void getTaskSuccess(TaskInfo info) {
        if(isViewAttached()){
            mActView.get().getTaskSuccess(info);
        }
    }
}
