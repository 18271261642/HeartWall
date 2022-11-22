package com.jkcq.hrwtv.http.bean;

/**
 * created by wq on 2019/5/18
 */

public  class ADInfo {
    /**
     * uid : 1119047008426971138
     * createTime : 2019-04-19 09:16:12
     * page : 1
     * size : 10
     * adName : 亚马逊
     * businessName : 体检机
     * endTime : 2019-10-16 00:00:00
     * machineUrl : http://isportcloud.oss-cn-shenzhen.aliyuncs.com/manager/Koala.jpg
     * period : 180
     * startTime : 2019-04-19 00:00:00
     * status : 1
     * type : 0
     * deviceTypeId : 1115581636696731649
     */

    private String uid;
    private String createTime;
    private int page;
    private int size;
    private String adName;
    private String businessName;
    private String endTime;
    private String machineUrl;
    private int period;
    private String startTime;
    private int status;
    private int type;
    private String deviceTypeId;

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

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMachineUrl() {
        return machineUrl;
    }

    public void setMachineUrl(String machineUrl) {
        this.machineUrl = machineUrl;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    @Override
    public String toString() {
        return "ADInfo{" +
                "uid='" + uid + '\'' +
                ", createTime='" + createTime + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", adName='" + adName + '\'' +
                ", businessName='" + businessName + '\'' +
                ", endTime='" + endTime + '\'' +
                ", machineUrl='" + machineUrl + '\'' +
                ", period=" + period +
                ", startTime='" + startTime + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", deviceTypeId='" + deviceTypeId + '\'' +
                '}';
    }
}