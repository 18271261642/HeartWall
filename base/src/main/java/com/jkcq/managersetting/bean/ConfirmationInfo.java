package com.jkcq.managersetting.bean;

/**
 * created by wq on 2019/6/13
 */
public class ConfirmationInfo {
    private boolean confirmation;

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    @Override
    public String toString() {
        return "LogoutInfo{" +
                "confirmation=" + confirmation +
                '}';
    }
}
