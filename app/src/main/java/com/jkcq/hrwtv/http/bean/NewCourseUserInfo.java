package com.jkcq.hrwtv.http.bean;

import java.util.List;

/**
 * created by wq on 2019/5/31
 */
public class NewCourseUserInfo {

    /**
     * nickName : 石头
     * userName : 186****8982
     * headShotUrl : https://isportcloud.oss-cn-shenzhen.aliyuncs.com/headShot1134361277196054530.jpg?1560240650283
     * sn : 0010000012
     * gender : Male
     * weight : 61
     * height : 174
     * userId : 1134361277196054530
     * age : 30
     * courseList : [{"courseId":"545","courseName":"黄宗华团课1","courseStartTime":1561186800960,"courseEndTime":1561190400960,"roomId":"1130310760111173633"}]
     */

    private String nickName;
    private String userName;
    private String headShotUrl;
    private String sn;
    private String gender;
    private int weight;
    private int height;
    private String userId;
    private int age;
    private List<CourseListBean> courseList;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadShotUrl() {
        return headShotUrl;
    }

    public void setHeadShotUrl(String headShotUrl) {
        this.headShotUrl = headShotUrl;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<CourseListBean> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<CourseListBean> courseList) {
        this.courseList = courseList;
    }

    public static class CourseListBean {
        /**
         * courseId : 545
         * courseName : 黄宗华团课1
         * courseStartTime : 1561186800960
         * courseEndTime : 1561190400960
         * roomId : 1130310760111173633
         */

        private String courseId;
        private String courseName;
        private long courseStartTime;
        private long courseEndTime;
        private String roomId;

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public long getCourseStartTime() {
            return courseStartTime;
        }

        public void setCourseStartTime(long courseStartTime) {
            this.courseStartTime = courseStartTime;
        }

        public long getCourseEndTime() {
            return courseEndTime;
        }

        public void setCourseEndTime(long courseEndTime) {
            this.courseEndTime = courseEndTime;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }
    }

    @Override
    public String toString() {
        return "CourseUserInfo{" +
                "nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", headShotUrl='" + headShotUrl + '\'' +
                ", sn='" + sn + '\'' +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", userId='" + userId + '\'' +
                ", age=" + age +
                ", courseList=" + courseList.size() +
                ", courseStartTime=" + courseStartTime +
                ", courseEndTime=" + courseEndTime +
                '}';
    }

    //自己添加
    private long courseStartTime;
    private long courseEndTime;

    public long getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(long courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public long getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(long courseEndTime) {
        this.courseEndTime = courseEndTime;
    }
}
