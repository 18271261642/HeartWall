package com.jkcq.managersetting.bean;

/**
 * created by wq on 2019/5/17
 */

public  class RegisterInfo {
    /**
     * uid : 10000
     * address : 深圳市宝安区西乡大道270号
     * addressUrl : string
     * characteristic : 跑步机、动感单车、智能储物柜、WIFI、淋浴
     * latitude : 113.86632
     * longitude : 22.595606
     * name : 宝体
     * profile : 可容纳200人同时健身，当前健身人数30人。热门项目是减脂 训练营和体能训练营。
     * telephone : 13510309030
     * areaId : 440306
     * cityId : 4403
     * provinceId : 44
     * streetId : 440306017
     * villageId : null
     * headShotUrl : http://isportcloud.oss-cn-shenzhen.aliyuncs.com/manager/2.png
     * branchId : null
     * branchName : 健康
     */

    private String uid;
    private String address;
    private String addressUrl;
    private String characteristic;
    private double latitude;
    private double longitude;
    private String name;
    private String profile;
    private String telephone;
    private String areaId;
    private String cityId;
    private String provinceId;
    private String streetId;
    private Object villageId;
    private String headShotUrl;
    private Object branchId;
    private String branchName;

    //新增
    private String classRoomId;
    private String funcId;

    public String getClassRoomId() {
        return classRoomId;
    }

    public void setClassRoomId(String classRoomId) {
        this.classRoomId = classRoomId;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressUrl() {
        return addressUrl;
    }

    public void setAddressUrl(String addressUrl) {
        this.addressUrl = addressUrl;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public Object getVillageId() {
        return villageId;
    }

    public void setVillageId(Object villageId) {
        this.villageId = villageId;
    }

    public String getHeadShotUrl() {
        return headShotUrl;
    }

    public void setHeadShotUrl(String headShotUrl) {
        this.headShotUrl = headShotUrl;
    }

    public Object getBranchId() {
        return branchId;
    }

    public void setBranchId(Object branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public String toString() {
        return "ClubInfo{" +
                "uid='" + uid + '\'' +
                ", address='" + address + '\'' +
                ", addressUrl='" + addressUrl + '\'' +
                ", characteristic='" + characteristic + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                ", telephone='" + telephone + '\'' +
                ", areaId='" + areaId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", streetId='" + streetId + '\'' +
                ", villageId=" + villageId +
                ", headShotUrl='" + headShotUrl + '\'' +
                ", branchId=" + branchId +
                ", branchName='" + branchName + '\'' +
                ", classRoomId='" + classRoomId + '\'' +
                ", funcId='" + funcId + '\'' +
                '}';
    }
}