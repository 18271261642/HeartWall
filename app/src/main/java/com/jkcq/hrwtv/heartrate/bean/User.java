package com.jkcq.hrwtv.heartrate.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Created by peng on 2018/4/21.
 */

public class User implements Parcelable {

    /**
     * userId : 1110074077168619522
     * createTime : 2019-03-25 15:00:58
     * birthday : 1991-01-01
     * city : null
     * description : null
     * email : null
     * gender : Female
     * height : 160
     * mobile : 18664328616
     * password : null
     * status : 0
     * weight : 50
     * interest : null
     * motionStatus : 0
     * occupation : null
     * openId : null
     * targetSteps : null
     * type : null
     * weixinId : null
     * headshotUrl : https://isportcloud.oss-cn-shenzhen.aliyuncs.com/headShot1110074077168619522.jpg?1555574445000
     * inviteCode : null
     * isDeleted : 0
     * isPause : 0
     * isRegidit : 1
     * nickName : 186****8616
     * lastLoginTime : 2019-03-24T16:00:00.000+0000
     * qqId : null
     * healthPack : null
     * sportPurposes : null
     * sportTimes : null
     * mobileAreaCode : null
     * userName : 186****8616
     * headshotUrlS : null
     * sn : null
     * deviceToken : 146b9c1a9c76
     */

    private String userId;
    private String createTime;
    private String birthday;
    private String city;
    private String description;
    private String email;
    private String gender;
    private int height;
    private String mobile;
    private String password;
    private int status;
    private int weight;
    private String interest;
    private int motionStatus;
    private String occupation;
    private String openId;
    private String targetSteps;
    private String type;
    private String weixinId;
    private String headShotUrl;
    private String inviteCode;
    private int isDeleted;
    private int isPause;
    private int isRegidit;
    private String nickName;
    private String lastLoginTime;
    private String qqId;
    private String healthPack;
    private String sportPurposes;
    private String sportTimes;
    private String mobileAreaCode;
    private String userName;
    private String headshotUrlS;
    private String sn;
    private String deviceToken;
    private int age;
    private boolean isShow;
    private boolean pause;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.gender);
        dest.writeString(this.mobile);
        dest.writeString(this.nickName);
        dest.writeString(this.userName);
        dest.writeInt(this.height);
        dest.writeInt(this.weight);
        dest.writeString(this.createTime);
        dest.writeString(this.occupation);
        dest.writeInt(this.motionStatus);
        dest.writeString(this.lastLoginTime);
        dest.writeString(this.birthday);
        dest.writeByte(this.pause ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isShow ? (byte) 1 : (byte) 0);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.gender = in.readString();
        this.mobile = in.readString();
        this.nickName = in.readString();
        this.userName = in.readString();
        this.height = in.readInt();
        this.weight = in.readInt();
        this.createTime = in.readString();
        this.occupation = in.readString();
        this.motionStatus = in.readInt();
        this.lastLoginTime = in.readString();
        this.birthday = in.readString();
        this.pause = in.readByte() != 0;
        this.isShow = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
       /* super.equals(obj);*/
        User user = (User) obj;
        if (user == null) {
            return false;
        }
        if (!TextUtils.isEmpty(user.getUserId())) {
            if (user.getUserId().equals(this.getUserId())) {
                return true;
            } else {
                return false;
            }
        } else {
            if (user.getSn().equals(this.getSn())) {
                return true;
            } else {
                return false;
            }
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public int getMotionStatus() {
        return motionStatus;
    }

    public void setMotionStatus(int motionStatus) {
        this.motionStatus = motionStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTargetSteps() {
        return targetSteps;
    }

    public void setTargetSteps(String targetSteps) {
        this.targetSteps = targetSteps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public String getHeadshotUrl() {
        return headShotUrl;
    }

    public void setHeadshotUrl(String headshotUrl) {
        this.headShotUrl = headshotUrl;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getIsPause() {
        return isPause;
    }

    public void setIsPause(int isPause) {
        this.isPause = isPause;
    }

    public int getIsRegidit() {
        return isRegidit;
    }

    public void setIsRegidit(int isRegidit) {
        this.isRegidit = isRegidit;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public String getHealthPack() {
        return healthPack;
    }

    public void setHealthPack(String healthPack) {
        this.healthPack = healthPack;
    }

    public String getSportPurposes() {
        return sportPurposes;
    }

    public void setSportPurposes(String sportPurposes) {
        this.sportPurposes = sportPurposes;
    }

    public String getSportTimes() {
        return sportTimes;
    }

    public void setSportTimes(String sportTimes) {
        this.sportTimes = sportTimes;
    }

    public String getMobileAreaCode() {
        return mobileAreaCode;
    }

    public void setMobileAreaCode(String mobileAreaCode) {
        this.mobileAreaCode = mobileAreaCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadshotUrlS() {
        return headshotUrlS;
    }

    public void setHeadshotUrlS(String headshotUrlS) {
        this.headshotUrlS = headshotUrlS;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
