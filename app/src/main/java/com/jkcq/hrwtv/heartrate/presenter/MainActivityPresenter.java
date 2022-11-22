package com.jkcq.hrwtv.heartrate.presenter;

import com.jkcq.hrwtv.base.mvp.BasePresenter;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.heartrate.model.MainActivityModel;
import com.jkcq.hrwtv.heartrate.model.MainActivityModelImp;
import com.jkcq.hrwtv.heartrate.model.MainActivityView;
import com.jkcq.hrwtv.heartrate.model.resultmodel.TaskResultModel;
import com.jkcq.hrwtv.heartrate.model.resultmodel.TaskResultView;
import com.jkcq.hrwtv.http.bean.ClubInfo;
import com.jkcq.hrwtv.http.bean.CourseList;
import com.jkcq.hrwtv.http.bean.CourseUserInfo;

import java.util.ArrayList;
import java.util.List;

/*
 *
 *
 * @author mhj
 * Create at 2018/4/16 16:49
 */
public class MainActivityPresenter extends BasePresenter<MainActivityView> implements MainActivityView, MainActivityModel, TaskResultModel, TaskResultView {

    MainActivityModelImp model;

    public MainActivityPresenter() {
        model = new MainActivityModelImp(context, this);
    }


    @Override
    public void getClubSuccess(ClubInfo name) {
        if (isViewAttached()) {
            mActView.get().getClubSuccess(name);
        }
    }


    @Override
    public void getClubFunction() {
        model.getClubFunction();
    }


    @Override
    public void onRespondError(String message) {
        if (isViewAttached()) {
            mActView.get().onRespondError(message);
        }
    }


    @Override
    public void getCourseList(String clubId, String roomId) {
        model.getCourseList(clubId, roomId);
    }

    @Override
    public void getAllUser(String clubId, String roomId) {
        model.getAllUser(clubId, roomId);
    }


    @Override
    public void getCourseListSuccess(List<CourseList.CourseInfo> courseInfoList) {
        if (isViewAttached()) {
            mActView.get().getCourseListSuccess(courseInfoList);
        }
    }

    @Override
    public void getAllUserSuccess(List<CourseUserInfo> userInfo) {
        if (isViewAttached()) {
            mActView.get().getAllUserSuccess(userInfo);
        }
    }

    @Override
    public void uploadAllDataSuccess() {
        if (isViewAttached()) {
            mActView.get().uploadAllDataSuccess();
        }
    }


    @Override
    public void getHeartRateSuccess(ArrayList<DevicesDataShowBean> rates) {
        if (isViewAttached()) {
        }
    }

    @Override
    public void postHeartRateDataSuccess() {
        if (isViewAttached()) {
        }
    }


    @Override
    public void postPk(ArrayList<DevicesDataShowBean> redlist, ArrayList<DevicesDataShowBean> bluelist, String heartRateCourseId, String winGroup, String modeType, long endTime, double redSum, double blueSum) {

        model.postPk(redlist, bluelist, heartRateCourseId, winGroup, modeType, endTime, redSum, blueSum);
    }

    @Override
    public void postHwall(ArrayList<DevicesDataShowBean> list, String modeType) {
        model.postHwall(list, modeType);
    }

    @Override
    public void postCourse(ArrayList<DevicesDataShowBean> list, String heartRateCourseId, String modeType, long endTime) {
        model.postCourse(list, heartRateCourseId, modeType, endTime);
    }
}
