package com.jkcq.hrwtv.heartrate.presenter;

import android.content.Context;

import com.jkcq.hrwtv.base.mvp.BasePresenter;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;
import com.jkcq.hrwtv.heartrate.model.allcoursemodel.CourseTypeFragmentModel;
import com.jkcq.hrwtv.heartrate.model.allcoursemodel.CourseFragmentModelImp;
import com.jkcq.hrwtv.heartrate.model.allcoursemodel.HrCourseTypeFragmentView;

import java.util.ArrayList;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/17 10:49 
 */
public class CourseAllFragmentPresenter extends BasePresenter<HrCourseTypeFragmentView> implements HrCourseTypeFragmentView, CourseTypeFragmentModel {

    CourseTypeFragmentModel hrCourseFragmentModel;

    public CourseAllFragmentPresenter() {
        hrCourseFragmentModel = new CourseFragmentModelImp(context, this);
    }

    @Override
    public void getAllCourseType(Context context) {
        hrCourseFragmentModel.getAllCourseType(context);
    }

    @Override
    public void getAllCourseTypeSuccess(ArrayList<TaskInfo> taskInfos) {
        if (isViewAttached()) {
            mActView.get().getAllCourseTypeSuccess(taskInfos);
        }
    }


    @Override
    public void onRespondError(String message) {
        if (isViewAttached()) {
            mActView.get().onRespondError(message);
        }
    }
}
