package com.jkcq.hrwtv.heartrate.bean;

/**
 * Created by peng on 2018/5/5.
 */

public class TypeBean {
    private int resID;
    private String name;
    private String sortType;
    private boolean isSort;

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSort() {
        return isSort;
    }

    public void setSort(boolean sort) {
        isSort = sort;
    }
}
