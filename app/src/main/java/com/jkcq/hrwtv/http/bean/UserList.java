package com.jkcq.hrwtv.http.bean;

import com.jkcq.hrwtv.heartrate.bean.User;
import com.jkcq.hrwtv.heartrate.bean.UserBean;

import java.util.List;

/**
 * created by wq on 2019/5/31
 */
public class UserList {

    private List<UserBean> list;

    public List<UserBean> getList() {
        return list;
    }

    public void setList(List<UserBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "UserList{" +
                "list=" + list +
                '}';
    }
}
