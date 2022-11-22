package com.jkcq.hrwtv.http.bean;

import java.util.ArrayList;
import java.util.List;

public class HeartRateClassInfo {

    private ArrayList<ListBean> list;

    public ArrayList<ListBean> getList() {
        return list;
    }

    public void setList(ArrayList<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private String heartRateClassName;
        private int point;
        private Long time;
        private String iconUrl;
        private int strength;//强度0：初级 1：中级 2：高级


        private List<HeartClassUnitListBean> heartClassUnitList;

        public String getHeartRateClassName() {
            return heartRateClassName;
        }

        public void setHeartRateClassName(String heartRateClassName) {
            this.heartRateClassName = heartRateClassName;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public int getStrength() {
            return strength;
        }

        public void setStrength(int strength) {
            this.strength = strength;
        }

        public List<HeartClassUnitListBean> getHeartClassUnitList() {
            return heartClassUnitList;
        }

        public void setHeartClassUnitList(List<HeartClassUnitListBean> heartClassUnitList) {
            this.heartClassUnitList = heartClassUnitList;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "heartRateClassName='" + heartRateClassName + '\'' +
                    ", point=" + point +
                    ", time=" + time +
                    ", iconUrl='" + iconUrl + '\'' +
                    ", strength=" + strength +
                    ", heartClassUnitList=" + heartClassUnitList +
                    '}';
        }

        public static class HeartClassUnitListBean {

            private Long duration;
            private String heartRange;
            private int sequence;

            public Long getDuration() {
                return duration;
            }

            public void setDuration(Long duration) {
                this.duration = duration;
            }

            public String getHeartRange() {
                return heartRange;
            }

            public void setHeartRange(String heartRange) {
                this.heartRange = heartRange;
            }

            public int getSequence() {
                return sequence;
            }

            public void setSequence(int sequence) {
                this.sequence = sequence;
            }

            @Override
            public String toString() {
                return "HeartClassUnitListBean{" +
                        "duration=" + duration +
                        ", heartRange='" + heartRange + '\'' +
                        ", sequence=" + sequence +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "HeartRateClassInfo{" +
                "list=" + list +
                '}';
    }
}
