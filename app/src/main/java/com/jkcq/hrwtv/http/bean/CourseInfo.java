package com.jkcq.hrwtv.http.bean;

import java.util.ArrayList;
import java.util.List;

public class CourseInfo {
    /**
     * "courseName": "",
     * "coverImage": "",
     * "difficultyLevel": 0,
     * "duration": 0,
     * "id": 0,
     * "targetRateArray": "",
     * "targetType": 0
     */
    String courseName;
    String coverImage;
    int difficultyLevel;
    int duration;
    String id;
    List<CourseDetail> targetRateArray;
    String targetType;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CourseDetail> getTargetRateArray() {
        return targetRateArray;
    }

    public void setTargetRateArray(List<CourseDetail> targetRateArray) {
        this.targetRateArray = targetRateArray;
    }

    public void addTargetRateArray(CourseDetail targetRateArray) {
        if (this.targetRateArray == null) {
            this.targetRateArray = new ArrayList<>();
        }
        this.targetRateArray.add(targetRateArray);
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "courseName='" + courseName + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", duration='" + duration + '\'' +
                ", id='" + id + '\'' +
                ", targetRateArray='" + targetRateArray + '\'' +
                ", targetType='" + targetType + '\'' +
                '}';
    }
}
