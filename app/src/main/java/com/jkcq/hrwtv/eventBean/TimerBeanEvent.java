package com.jkcq.hrwtv.eventBean;

import com.jkcq.hrwtv.heartrate.bean.TimerBean;

/**
 * Created by peng on 2018/5/7.
 */

public class TimerBeanEvent {


    TimerBean timerBean;

    public TimerBeanEvent() {

    }

    public TimerBeanEvent(TimerBean timerBean) {

        this.timerBean = timerBean;
    }

    public TimerBean getTimerBean() {
        return timerBean;
    }

    public void setTimerBean(TimerBean timerBean) {
        this.timerBean = timerBean;
    }


}
