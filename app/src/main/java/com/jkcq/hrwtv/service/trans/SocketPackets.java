package com.jkcq.hrwtv.service.trans;

/*
 *  示例： PBJ0000000100010000END22
 *
 * @author mhj
 * Create at 2018/1/15 11:33 
 */
public class SocketPackets {

    private String headChars = "PBJ";
    private String trailerChars = "END";
    private String dataChars = "";

    public String getDataChars() {
        return dataChars;
    }

    public void setDataChars(String dataChars) {
        this.dataChars = dataChars;
    }

    @Override
    public String toString() {
        return headChars+dataChars+trailerChars+(getDataChars().length()+6);
    }

}
