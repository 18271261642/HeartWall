package com.jkcq.hrwtv.heartrate.bean;

import com.jkcq.hrwtv.okhttp.BaseBean;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/19 16:44 
 */
public class TaskUnits extends BaseBean{
    /**
     * uid : 1120571549110407169
     * createTime : null
     * page : 1
     * size : 10
     * duration : 60
     * sequence : null
     * heartRange : RANGE1
     * courseId : 1120571548758085633
     */

    private String uid;
    private long createTime;
    private int page;
    private int size;
    private int duration;
    private int sequence;
    private String heartRange;
    private String courseId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getHeartRange() {
        return heartRange;
    }

    public void setHeartRange(String heartRange) {
        this.heartRange = heartRange;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }


    /**
     * courseUnitId : 4
     * duration : 2
     * heartRange : RANGE3
     * sequence : 1
     * range : 3
     */

//    private int courseUnitId;
//    private int duration;
//    private String heartRange;
//    private int sequence;
//    private int range;
//
//    public int getCourseUnitId() {
//        return courseUnitId;
//    }
//
//    public void setCourseUnitId(int courseUnitId) {
//        this.courseUnitId = courseUnitId;
//    }
//
//    public int getDuration() {
//        return duration;
//    }
//
//    public void setDuration(int duration) {
//        this.duration = duration;
//    }
//
//    public String getHeartRange() {
//        return heartRange;
//    }
//
//    public void setHeartRange(String heartRange) {
//        this.heartRange = heartRange;
//    }
//
//    public int getSequence() {
//        return sequence;
//    }
//
//    public void setSequence(int sequence) {
//        this.sequence = sequence;
//    }
//
//    public int getRange() {
//        return range;
//    }
//
//    public void setRange(int range) {
//        this.range = range;
//    }
}
