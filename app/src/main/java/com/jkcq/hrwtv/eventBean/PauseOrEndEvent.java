package com.jkcq.hrwtv.eventBean;

/**
 * Created by peng on 2018/5/10.
 */

public class PauseOrEndEvent {
    String type;

    public PauseOrEndEvent() {

    }

    public PauseOrEndEvent(String  type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
