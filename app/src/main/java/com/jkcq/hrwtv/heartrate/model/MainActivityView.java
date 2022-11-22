package com.jkcq.hrwtv.heartrate.model;

import com.jkcq.hrwtv.base.mvp.BaseView;
import com.jkcq.hrwtv.http.bean.ClubInfo;
import com.jkcq.hrwtv.http.bean.CourseList;
import com.jkcq.hrwtv.http.bean.CourseUserInfo;

import java.util.List;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/16 16:47 
 */
public interface MainActivityView extends BaseView{

    void getClubSuccess(ClubInfo info);

    void getCourseListSuccess(List<CourseList.CourseInfo> courseInfoList);

    void getAllUserSuccess(List<CourseUserInfo> userInfos);

    void uploadAllDataSuccess();
}
