package com.jkcq.hrwtv.heartrate.bean;

/**
 * Created by peng on 2018/5/5.
 */

public class ShowBean {
    private String ceter;
    private String one;
    private String two;
    private String three;
    private String four;

    public ShowBean() {

    }

    public ShowBean(String ceter,String one, String two, String three, String four) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.ceter=ceter;
    }


    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getCeter() {
        return ceter;
    }

    public void setCeter(String ceter) {
        this.ceter = ceter;
    }
}
