package com.jkcq.hrwtv.eventBean;

/**
 * Created by peng on 2018/5/18.
 */

public class UpdateMenuEvent {

    private boolean isShow;

    public UpdateMenuEvent(){

    }

    public UpdateMenuEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
