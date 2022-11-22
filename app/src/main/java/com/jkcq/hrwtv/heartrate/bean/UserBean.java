package com.jkcq.hrwtv.heartrate.bean;

import android.text.TextUtils;

import com.jkcq.hrwtv.okhttp.BaseBean;

/**
 * Created by peng on 2018/4/21.
 */

public class UserBean extends BaseBean {


    /**
     * avatar	头像地址	string
     * birthday	出生日期	string(date-time)
     * height	身高（cm）	integer(int32)
     * id	用户id	integer(int64)
     * nickname	用户昵称	string
     * weight	体重（kg）	integer(int32)
     */

    private String sn;
    //  private User userInfo;
    private long inAndOutId;
    private long joinTime;//设备连接的时间
    private int currentMod;//大厅 0,1课程 2红队 3蓝队
    private boolean isSelect;
    private String avatar;
    private long birthday;
    private int height;
    private float weight;
    private String sex;//1:男 2：女
    private String id;
    private String nickname;
    private int age;

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }


    public long getInAndOutId() {
        return inAndOutId;
    }

    public void setInAndOutId(long inAndOutId) {
        this.inAndOutId = inAndOutId;
    }


    @Override
    public boolean equals(Object obj) {
        super.equals(obj);
        UserBean ob = (UserBean) obj;
        if (ob.sn.equals(this.sn)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getCurrentMod() {
        return currentMod;
    }

    public void setCurrentMod(int currentMod) {
        this.currentMod = currentMod;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }
}
