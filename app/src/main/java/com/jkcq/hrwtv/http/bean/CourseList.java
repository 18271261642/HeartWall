package com.jkcq.hrwtv.http.bean;

import java.util.List;

/**
 * created by wq on 2019/5/31
 */
public class CourseList {

    private List<CourseInfo> list;

    public List<CourseInfo> getList() {
        return list;
    }

    public void setList(List<CourseInfo> list) {
        this.list = list;
    }
    @Override
    public String toString() {
        return "CourseList{" +
                "list=" + list +
                '}';
    }
    public static class CourseInfo {
        /**
         * courseName : 身心瑜伽
         * courseId : 461
         * courseStartTime : 1559304000454
         * courseEndTime : 1559307600454
         */

        private String courseName;
        private String courseId;
        private long courseStartTime;
        private long courseEndTime;

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
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

        @Override
        public String toString() {
            return "CourseInfo{" +
                    "courseName='" + courseName + '\'' +
                    ", courseId='" + courseId + '\'' +
                    ", courseStartTime=" + courseStartTime +
                    ", courseEndTime=" + courseEndTime +
                    '}';
        }
    }
}
