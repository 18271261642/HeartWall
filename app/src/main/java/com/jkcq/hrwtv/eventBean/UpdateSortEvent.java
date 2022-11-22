package com.jkcq.hrwtv.eventBean;

/**
 * Created by peng on 2018/5/7.
 */

public class UpdateSortEvent {
    String sortType;

    public UpdateSortEvent(){

    }

    public UpdateSortEvent(String sortType) {
        this.sortType = sortType;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
