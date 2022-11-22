package com.jkcq.managersetting.bean;

/**
 * created by wq on 2019/6/28
 */
public class ClassRoomInfo {


    private String id;
    private String name;
    private String funcId;

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

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    @Override
    public String toString() {
        return "ClassroomInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", funcId='" + funcId + '\'' +
                '}';
    }
}
