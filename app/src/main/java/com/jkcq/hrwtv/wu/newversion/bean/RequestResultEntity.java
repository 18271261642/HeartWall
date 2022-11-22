package com.jkcq.hrwtv.wu.newversion.bean;

public class RequestResultEntity {
    //    {
    //      "averageRate": 0,
    //      "calorie": 0,
    //      "duration": 0,
    //      "empiricalValue": 0,
    //      "endTime": "",
    //      "exerciseDay": "",
    //      "groupType": 0,
    //      "heartRateArray": "",
    //      "heartRateCourseId": 0,
    //      "matchingRate": 0,
    //      "maxRate": 0,
    //      "modeType": 0,
    //      "ranking": 0,
    //      "startTime": ""
    //    }

    String memberId;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    int averageRate;
    int calorie;
    int duration;
    int empiricalValue;//经验值
    String endTime;
    String exerciseDay;
    String groupType;
    String heartRateArray;
    String heartRateCourseId;
    String matchingRate;//匹配度
    int maxRate;
    int minRate;
    String modeType;
    String ranking;
    String startTime;
    String averageStrength;
    int age;
    String sex;

    public String getAverageStrength() {
        return averageStrength;
    }

    public void setAverageStrength(String averageStrength) {
        this.averageStrength = averageStrength;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAverageRate() {
        return averageRate;
    }

    public void setAverageRate(int averageRate) {
        this.averageRate = averageRate;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getEmpiricalValue() {
        return empiricalValue;
    }

    public void setEmpiricalValue(int empiricalValue) {
        this.empiricalValue = empiricalValue;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExerciseDay() {
        return exerciseDay;
    }

    public void setExerciseDay(String exerciseDay) {
        this.exerciseDay = exerciseDay;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getHeartRateArray() {
        return heartRateArray;
    }

    public void setHeartRateArray(String heartRateArray) {
        this.heartRateArray = heartRateArray;
    }

    public String getHeartRateCourseId() {
        return heartRateCourseId;
    }

    public void setHeartRateCourseId(String heartRateCourseId) {
        this.heartRateCourseId = heartRateCourseId;
    }

    public String getMatchingRate() {
        return matchingRate;
    }

    public void setMatchingRate(String matchingRate) {
        this.matchingRate = matchingRate;
    }

    public int getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(int maxRate) {
        this.maxRate = maxRate;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getMinRate() {
        return minRate;
    }

    public void setMinRate(int minRate) {
        this.minRate = minRate;
    }

    @Override
    public String toString() {
        return "RequestResultEntity{" +
                "memberId='" + memberId + '\'' +
                ", averageRate=" + averageRate +
                ", calorie=" + calorie +
                ", duration=" + duration +
                ", empiricalValue=" + empiricalValue +
                ", endTime='" + endTime + '\'' +
                ", exerciseDay='" + exerciseDay + '\'' +
                ", groupType='" + groupType + '\'' +
                ", heartRateArray='" + heartRateArray + '\'' +
                ", heartRateCourseId='" + heartRateCourseId + '\'' +
                ", matchingRate='" + matchingRate + '\'' +
                ", maxRate=" + maxRate +
                ", minRate=" + minRate +
                ", modeType='" + modeType + '\'' +
                ", ranking='" + ranking + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }
}

