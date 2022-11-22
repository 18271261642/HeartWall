package com.jkcq.hrwtv.wu.newversion.bean;

public class SelectUserBean implements Comparable {

    String sn;
    String nickname;
    String avatar;
    int currentMod;
    boolean isSelect;
    long jointime;

    public SelectUserBean(String sn, String nickname, String avatar, int currentMod, boolean isSelect, long jointime) {
        this.sn = sn;
        this.nickname = nickname;
        this.avatar = avatar;
        this.currentMod = currentMod;
        this.isSelect = isSelect;
        this.jointime = jointime / 1000;
    }

    public long getJointime() {
        return jointime;
    }

    public void setJointime(long jointime) {
        this.jointime = jointime;
    }

    public SelectUserBean() {
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCurrentMod() {
        return currentMod;
    }

    public void setCurrentMod(int currentMod) {
        this.currentMod = currentMod;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    @Override
    public int compareTo(Object o) {
        SelectUserBean deviceBean = (SelectUserBean) o;
        int count = 0;
        if (this.getJointime() > deviceBean.getJointime()) {
            count = -1;
        } else {
            count = 1;
        }
        return count;
    }

}
