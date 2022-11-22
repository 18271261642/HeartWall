package com.jkcq.hrwtv.heartrate.bean;

import com.jkcq.hrwtv.okhttp.BaseBean;

/*
 *
 *
 * @author mhj
 * Create at 2019/4/24 16:23
 */
public class DevicesTypeBean extends BaseBean{


    /**
     * id : 1115582172275798018
     * name : 心率墙
     */

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
