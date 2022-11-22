package com.jkcq.hrwtv.http.bean;

/**
 * created by wq on 2019/5/17
 */

public  class ClubInfo {

    /**
     * uid : 10000
     * address : 深圳市宝安区松岗街道国际艺展中心艺术小镇T3-007.008.009.010
     * addressUrl :
     * characteristic : 发电跑步机、跑步机、椭圆机、发电动感单车、智能固定力量器械、自由力量器械
     * latitude : 22.788789
     * longitude : 113.8419
     * name : 健康传奇智能健身房
     * profile :
     * telephone : 0755-26400855
     * areaId : 440306
     * cityId : 4403
     * provinceId : 44
     * streetId : 440306024
     * villageId : null
     * headShotUrl : https://isportcloud.oss-cn-shenzhen.aliyuncs.com/manager/5.png
     * branchId : 1110380012529360897
     * branchName : 健康传奇
     * roomId : 1130310760111173633
     * roomName : 团操室
     * funcId : 1130306833433182209
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
    private String branchId;
    private String branchName;
    private String roomId;
    private String roomName;
    private String funcId;

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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
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
                ", branchId='" + branchId + '\'' +
                ", branchName='" + branchName + '\'' +
                ", roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", funcId='" + funcId + '\'' +
                '}';
    }
}