package com.jkcq.hrwtv.heartrate.bean;

import com.jkcq.hrwtv.okhttp.BaseBean;

import java.util.List;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/17 11:35 
 */
public class TaskListBean extends BaseBean{

    private List<TaskInfo> list;

    public List<TaskInfo> getList() {
        return list;
    }

    public void setList(List<TaskInfo> list) {
        this.list = list;
    }
}
