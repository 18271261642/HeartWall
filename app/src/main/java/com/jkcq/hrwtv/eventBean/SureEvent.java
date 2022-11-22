package com.jkcq.hrwtv.eventBean;

import com.jkcq.hrwtv.heartrate.bean.User;

import java.util.List;

/**
 * Created by peng on 2018/4/20.
 */

public class SureEvent {
    private String type;
    List<User> list;

    public SureEvent(){

    }
    public List<User> getList() {
        return list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
