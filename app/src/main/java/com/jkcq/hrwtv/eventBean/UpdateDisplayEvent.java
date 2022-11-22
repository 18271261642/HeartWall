package com.jkcq.hrwtv.eventBean;

/**
 * Created by peng on 2018/5/7.
 */

public class UpdateDisplayEvent {
    String certerType;

    public UpdateDisplayEvent() {

    }

    public UpdateDisplayEvent(String certerType) {
        this.certerType = certerType;
    }

    public String getSortType() {
        return certerType;
    }

    public void setSortType(String sortType) {
        this.certerType = certerType;
    }
}
