package com.jkcq.hrwtv.eventBean;

/**
 * Created by peng on 2018/5/17.
 */

public class ClubRegSuccess {
    String clubName;

    public ClubRegSuccess(){

    }

    public ClubRegSuccess(String clubName) {
        this.clubName = clubName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}
