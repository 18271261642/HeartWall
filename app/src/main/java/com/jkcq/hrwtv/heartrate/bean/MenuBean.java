package com.jkcq.hrwtv.heartrate.bean;

/**
 * Created by peng on 2018/5/10.
 */

public class MenuBean {
    private int ResId;
    private String name;
    private String menuType;

    public int getResId() {
        return ResId;
    }

    public void setResId(int resId) {
        ResId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
}
