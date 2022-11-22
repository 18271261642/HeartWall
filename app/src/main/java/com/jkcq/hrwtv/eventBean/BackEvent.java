package com.jkcq.hrwtv.eventBean;

import com.jkcq.hrwtv.heartrate.bean.User;

import java.util.List;


/**
 *
 */
public class BackEvent {

    public BackEvent() {

    }

    public BackEvent(boolean isBack) {
        this.isBack = isBack;
    }

    public BackEvent(boolean isBack, String type) {
        this.isBack = isBack;
        this.type = type;
    }

    private boolean isBack;
    private String type;
    private String toFragment;
    private boolean isFilterOk;
    private List<User> userinfos;

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToFragment() {
        return toFragment;
    }

    public void setToFragment(String toFragment) {
        this.toFragment = toFragment;
    }

    public boolean isFilterOk() {
        return isFilterOk;
    }

    public void setFilterOk(boolean filterOk) {
        isFilterOk = filterOk;
    }

    public List<User> getUserinfos() {
        return userinfos;
    }

    public void setUserinfos(List<User> userinfos) {
        this.userinfos = userinfos;
    }
}
