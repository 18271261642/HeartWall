package com.jkcq.hrwtv.heartrate.bean;

import com.jkcq.hrwtv.okhttp.BaseBean;

import java.util.List;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/17 10:58 
 */
public class TaskInfo extends BaseBean{
    /**
     * uid : 1120571548758085633
     * createTime : 2019-04-23 14:14:10
     * page : 1
     * size : 10
     * name : string
     * sectionNum : 2
     * totalTime : 110
     * point : 60
     * createUserId : null
     * createUserName : null
     * status : 0
     * unitList : null
     * startTime : null
     * endTime : null
     */

    private String uid;
    private String createTime;
    private int page;
    private int size;
    private String name;
    private int sectionNum;
    private int totalTime;
    private int point;
    private String createUserId;
    private String createUserName;
    private int status;
    private String startTime;
    private String endTime;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(int sectionNum) {
        this.sectionNum = sectionNum;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Object getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    /**
     * courseId : 3
     * time : 18
     * point : 34
     * courseName : 测试
     * isDelete : false
     * units : []
     * delete : false
     */

//    private int courseId;
//    private int time;
//    private int point;
//    private String courseName;
//    private boolean isDelete;
//    private boolean delete;
    private List<TaskUnits> unitList;
//
//    public int getCourseId() {
//        return courseId;
//    }
//
//    public void setCourseId(int courseId) {
//        this.courseId = courseId;
//    }
//
//    public int getTime() {
//        return time;
//    }
//
//    public void setTime(int time) {
//        this.time = time;
//    }
//
//    public int getPoint() {
//        return point;
//    }
//
//    public void setPoint(int point) {
//        this.point = point;
//    }
//
//    public String getCourseName() {
//        return courseName;
//    }
//
//    public void setCourseName(String courseName) {
//        this.courseName = courseName;
//    }
//
//    public boolean isIsDelete() {
//        return isDelete;
//    }
//
//    public void setIsDelete(boolean isDelete) {
//        this.isDelete = isDelete;
//    }
//
//    public boolean isDelete() {
//        return delete;
//    }
//
//    public void setDelete(boolean delete) {
//        this.delete = delete;
//    }
//
    public List<TaskUnits> getUnits() {
        return unitList;
    }

    public void setUnits(List<TaskUnits> units) {
        this.unitList = units;
    }
}
