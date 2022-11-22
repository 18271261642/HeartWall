package com.jkcq.hrwtv.wu.newversion.bean;

import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.wu.newversion.view.PKItemView;

public class PKData {
    PKItemView itemView;
    DevicesDataShowBean bean;

    public PKData() {
    }

    public PKData(PKItemView itemView, DevicesDataShowBean bean) {
        this.itemView = itemView;
        this.bean = bean;
    }

    public PKItemView getItemView() {
        return itemView;
    }

    public void setItemView(PKItemView itemView) {
        this.itemView = itemView;
    }

    public DevicesDataShowBean getBean() {
        return bean;
    }

    public void setBean(DevicesDataShowBean bean) {
        this.bean = bean;
    }
}
