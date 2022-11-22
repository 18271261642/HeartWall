package com.jkcq.hrwtv.http.bean;

import java.util.List;

public class HeartRateClass {

    /**
     * pageNum : 1
     * pageSize : 10
     * total : 3
     * pages : 1
     * list : [{"uid":"1215227278711762946","createTime":"null","page":1,"size":10,"heartRateClassDescribe":"string","heartRateClassName":"string","iscancel":"null","isdelete":"null","ispublished":true,"point":50,"time":60,"managerId":"123","managerName":"lyy","iconId":"www.baidu.com","descriptionpageUrl":"www.baidu.com","subtitle":"hahha","heartClassUnitList":[{"uid":"1215227278766288898","createTime":"null","page":1,"size":10,"duration":20,"heartRange":"null","sequence":0,"heartRateClassId":"1215227278711762946","isdelete":false},{"uid":"1215227278778871810","createTime":"null","page":1,"size":10,"duration":40,"heartRange":"null","sequence":0,"heartRateClassId":"1215227278711762946","isdelete":false}]}]
     * isFirstPage : true
     * isLastPage : true
     */

    private int pageNum;
    private int pageSize;
    private int total;
    private int pages;
    private boolean isFirstPage;
    private boolean isLastPage;
    private List<ListBean> list;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * uid : 1215227278711762946
         * createTime : null
         * page : 1
         * size : 10
         * heartRateClassDescribe : string
         * heartRateClassName : string
         * iscancel : null
         * isdelete : null
         * ispublished : true
         * point : 50
         * time : 60
         * managerId : 123
         * managerName : lyy
         * iconId : www.baidu.com
         * descriptionpageUrl : www.baidu.com
         * subtitle : hahha
         * heartClassUnitList : [{"uid":"1215227278766288898","createTime":"null","page":1,"size":10,"duration":20,"heartRange":"null","sequence":0,"heartRateClassId":"1215227278711762946","isdelete":false},{"uid":"1215227278778871810","createTime":"null","page":1,"size":10,"duration":40,"heartRange":"null","sequence":0,"heartRateClassId":"1215227278711762946","isdelete":false}]
         */

        private String uid;
        private String createTime;
        private int page;
        private int size;
        private String heartRateClassDescribe;
        private String heartRateClassName;
        private String iscancel;
        private String isdelete;
        private boolean ispublished;
        private int point;
        private int time;
        private String managerId;
        private String managerName;
        private String iconId;
        private String descriptionpageUrl;
        private String subtitle;
        private List<HeartClassUnitListBean> heartClassUnitList;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getHeartRateClassDescribe() {
            return heartRateClassDescribe;
        }

        public void setHeartRateClassDescribe(String heartRateClassDescribe) {
            this.heartRateClassDescribe = heartRateClassDescribe;
        }

        public String getHeartRateClassName() {
            return heartRateClassName;
        }

        public void setHeartRateClassName(String heartRateClassName) {
            this.heartRateClassName = heartRateClassName;
        }

        public String getIscancel() {
            return iscancel;
        }

        public void setIscancel(String iscancel) {
            this.iscancel = iscancel;
        }

        public String getIsdelete() {
            return isdelete;
        }

        public void setIsdelete(String isdelete) {
            this.isdelete = isdelete;
        }

        public boolean isIspublished() {
            return ispublished;
        }

        public void setIspublished(boolean ispublished) {
            this.ispublished = ispublished;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getManagerId() {
            return managerId;
        }

        public void setManagerId(String managerId) {
            this.managerId = managerId;
        }

        public String getManagerName() {
            return managerName;
        }

        public void setManagerName(String managerName) {
            this.managerName = managerName;
        }

        public String getIconId() {
            return iconId;
        }

        public void setIconId(String iconId) {
            this.iconId = iconId;
        }

        public String getDescriptionpageUrl() {
            return descriptionpageUrl;
        }

        public void setDescriptionpageUrl(String descriptionpageUrl) {
            this.descriptionpageUrl = descriptionpageUrl;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public List<HeartClassUnitListBean> getHeartClassUnitList() {
            return heartClassUnitList;
        }

        public void setHeartClassUnitList(List<HeartClassUnitListBean> heartClassUnitList) {
            this.heartClassUnitList = heartClassUnitList;
        }

        public static class HeartClassUnitListBean {
            /**
             * uid : 1215227278766288898
             * createTime : null
             * page : 1
             * size : 10
             * duration : 20
             * heartRange : null
             * sequence : 0
             * heartRateClassId : 1215227278711762946
             * isdelete : false
             */

            private String uid;
            private String createTime;
            private int page;
            private int size;
            private int duration;
            private String heartRange;
            private int sequence;
            private String heartRateClassId;
            private boolean isdelete;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getPage() {
                return page;
            }

            public void setPage(int page) {
                this.page = page;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
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

            public String getHeartRateClassId() {
                return heartRateClassId;
            }

            public void setHeartRateClassId(String heartRateClassId) {
                this.heartRateClassId = heartRateClassId;
            }

            public boolean isIsdelete() {
                return isdelete;
            }

            public void setIsdelete(boolean isdelete) {
                this.isdelete = isdelete;
            }
        }
    }
}
