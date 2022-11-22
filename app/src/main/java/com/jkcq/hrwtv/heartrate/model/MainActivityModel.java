package com.jkcq.hrwtv.heartrate.model;

/*
 *
 *
 * @author mhj
 * Create at 2018/4/16 16:45
 */
public interface MainActivityModel {

    void getClubFunction();

    void getCourseList(String clubId, String roomId);

    void getAllUser(String clubId, String roomId);
}
