package com.jkcq.hrwtv.wu.newversion.bean;

import java.util.List;

public class RequestDynamicBean {
    List<RequestResultEntity> dataList;
    String winGroup;
    String heartRateCourseId;

    @Override
    public String toString() {
        return "RequestDynamicBean{" +
                "datlist=" + dataList +
                ", winGroup='" + winGroup + '\'' +
                ", heartRateCourseId='" + heartRateCourseId + '\'' +
                '}';
    }

    public List<RequestResultEntity> getDatlist() {
        return dataList;
    }

    public void setDatlist(List<RequestResultEntity> datlist) {
        this.dataList = datlist;
    }

    public String getWinGroup() {
        return winGroup;
    }

    public void setWinGroup(String winGroup) {
        this.winGroup = winGroup;
    }

    public String getHeartRateCourseId() {
        return heartRateCourseId;
    }

    public void setHeartRateCourseId(String heartRateCourseId) {
        this.heartRateCourseId = heartRateCourseId;
    }
}
