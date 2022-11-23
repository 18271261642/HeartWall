package com.jkcq.hrwtv.heartrate.bean;

import androidx.annotation.NonNull;
import android.util.Log;

import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.http.bean.CourseDetail;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.util.HeartRateConvertUtils;

import java.util.ArrayList;
import java.util.List;

/*
 *
 *
 * @author mhj
 * Create at 2018/7/11 15:14
 */
public class DevicesDataShowBean implements Comparable {


    private int pkTeam = 0;//0为红队，1为蓝队

    private int ranking = 0;//排名数据

    private long time; //掉线的时间
    private String devicesSN;

    private int courseTime;//上课时长

    private String userId;
    private int age;
    private float weight;
    private int height;
    private String headUrl;
    private String nikeName;
    private Long joinTime;
    private String sex;


    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    private double cal;//总卡路里值,累加值
    private int liveHeartRate;//实时心率值
    private int averageHeartRate;//平均心率值
    private int averageHeartPercent;//平均心率强度


    private double point;//总点数，累加值
    private String precent;//实时百分比，根据实时心率计算

    //匹配度
    private int totalCount;
    private int matchCount;
    private int matchRate = 100;

    // private double mTaskPoint;//课程时间内的点数,累加值
    // private double mTaskCal;//课程时间内的卡路里，累加值


    private String sortType;//主页面排序的类型
    private String courseSortType;//课程排序的类型


    private String courseId;//课程id


    public String getDeviceTypeName() {
        return "心率墙";
    }


    private boolean isOnline;

    public int getPkTeam() {
        return pkTeam;
    }

    public void setPkTeam(int pkTeam) {
        this.pkTeam = pkTeam;
    }

    public Long getJoinTime() {
        return joinTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setJoinTime(Long joinTime) {
        this.joinTime = joinTime;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    private ArrayList<Integer> mIntervalHearts = new ArrayList<>();//最新10s的平均心率

    // private ArrayList<Integer> mStageHeart = new ArrayList<>();//存储10s的心率，用于进度条显示

    private ArrayList<Integer> hrTaskList = new ArrayList<>();//课程期间的心率


    private ArrayList<Integer> hrList = new ArrayList<>();//十秒钟画一次


    private ArrayList<Integer> tempList = new ArrayList<>();

    private ArrayList<Integer> allHrList = new ArrayList<>();//2秒一个数据
    private ArrayList<Integer> calAllHrList = new ArrayList<>();//1分钟进行数据的整合


    public ArrayList<Integer> getCalAllHrList() {
        return calAllHrList;
    }

    public void setCalAllHrList(ArrayList<Integer> calAllHrList) {
        this.calAllHrList.clear();

        this.calAllHrList.addAll(calAllHrList);
    }


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private ArrayList<Integer> minHrList = new ArrayList<>();////1分钟进行数据的整合

    public ArrayList<Integer> getMinHrList() {
        return minHrList;
    }

    public void addMinHrList(Integer minHr) {
        this.minHrList.add(minHr);
    }

    public void setMinHrList(ArrayList<Integer> minHrList) {
        this.minHrList = minHrList;
    }

    private Long mAnimateTime = 0L;

    public ArrayList<Integer> getAllHrList() {
        return allHrList;
    }

    public void setAllHrList(ArrayList<Integer> hrList){
        if (allHrList == null) {
            allHrList = new ArrayList<>();
        }
        allHrList.clear();
        this.allHrList.addAll(hrList);
    }

    public void setAllHrList(Integer hr) {
        if (allHrList == null) {
            allHrList = new ArrayList<>();
        }
        this.allHrList.add(hr);
    }

    public Long getmAnimateTime() {
        return mAnimateTime;
    }

    public void setmAnimateTime(Long mAnimateTime) {
        this.mAnimateTime = mAnimateTime;
    }


    public int getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(int courseTime) {

        this.courseTime = courseTime;
    }

    public String getDevicesSN() {
        return devicesSN;
    }

    public void setDevicesSN(String devicesSN) {
        this.devicesSN = devicesSN;
    }

    public double getCal() {
        return cal;
    }

    //累加卡路里
    public void setCal(double cal) {
        this.cal = this.cal + cal;
    }

    public int getLiveHeartRate() {
        return liveHeartRate;
    }

    public void setLiveHeartRate(int liveHeartRate) {
        this.liveHeartRate = liveHeartRate;
    }

    public double getPoint() {
        return point;
    }

    //累加点数
    public void setPoint(double point) {
        this.point = point;
    }

    public String getPrecent() {
        return precent;
    }

    public void setPrecent(String precent) {
        this.precent = precent;
    }


    public ArrayList<Integer> getHrList() {
        return hrList;
    }


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount() {
        this.totalCount++;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount() {
        this.matchCount++;
    }

   /* public double getmTaskPoint() {
        return mTaskPoint;
    }

    //累加
    public void setmTaskPoint(double mTaskPoint) {
        this.mTaskPoint = this.mTaskPoint + mTaskPoint;
    }

    public double getmTaskCal() {
        return mTaskCal;
    }

    //累加
    public void setmTaskCal(double mTaskCal) {
        this.mTaskCal = this.mTaskCal + mTaskCal;
    }*/


    public ArrayList<Integer> getHrTaskList() {
        return hrTaskList;
    }


   /* public ArrayList<Integer> getmStageHeart() {
        return mStageHeart;
    }*/


    int currentPoint = 0;
    int lastPoint = 0;

    public void addStageHeart(String sn, int hearStrength) {

        Log.e("addStageHeart", "addStageHeart" + hearStrength);

        int tempPoint = 0;
        if (hearStrength == -1) {
            tempPoint = 0;
        } else if (hearStrength < 50) {
            tempPoint = 0;
        } else if (hearStrength >= 50 && hearStrength < 60) {
            tempPoint = 1;
        } else if (hearStrength >= 60 && hearStrength < 70) {
            tempPoint = 2;
        } else if (hearStrength >= 70 && hearStrength < 80) {
            tempPoint = 3;
        } else if (hearStrength >= 80 && hearStrength < 90) {
            tempPoint = 4;
        } else if (hearStrength >= 90) {
            tempPoint = 5;
        }
        if (this.mDatas.size() == 0) {
            this.mDatas.add(new CourseDetail(0, 1, tempPoint));
        } else {
            CourseDetail detail = mDatas.get(mDatas.size() - 1);
            if (detail.getTargetRange() == tempPoint) {
                detail.setEnd(detail.getEnd() + 1);
            } else {
                this.mDatas.add(new CourseDetail(detail.getEnd(), detail.getEnd() + 1, tempPoint));
            }
        }
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    private int lastStreng;
    //数据集合
    private List<CourseDetail> mDatas = new ArrayList<>();


    public List<CourseDetail> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<CourseDetail> mDatas) {
        this.mDatas = mDatas;
    }

    public void addStageHeart(int stageHeart) {
        if (this.mDatas.size() == 0) {
            this.mDatas.add(new CourseDetail(0, 0, stageHeart));
        } else {
            CourseDetail detail = mDatas.get(mDatas.size() - 1);
            if (detail.getTargetRange() == stageHeart) {
                detail.setEnd(detail.getEnd());
            } else {
                this.mDatas.add(new CourseDetail(0, 0, stageHeart));
            }
        }
    }

    public ArrayList<Integer> getmIntervalHearts() {
        return mIntervalHearts;
    }

    public void AddmIntervalHearts(int rate) {
        if (this.mIntervalHearts.size() >= 10) {
            this.mIntervalHearts.remove(0);
        }
        this.mIntervalHearts.add(rate);
    }


    public int getMatchRate() {
        return matchRate;
    }

    public void setMatchRate(int matchRate) {
        this.matchRate = matchRate;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getCourseSortType() {
        return courseSortType;
    }

    public void setCourseSortType(String courseSortType) {
        this.courseSortType = courseSortType;
    }


    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public void setAverageHeartRate() {
        int totalHeart = 0;
        if (tempList != null && tempList.size() > 0) {
            for (int heart : tempList) {
                totalHeart = totalHeart + heart;
            }
            this.averageHeartRate = totalHeart / tempList.size();
            this.averageHeartPercent = HeartRateConvertUtils.hearRate2Percent(averageHeartRate, age);
        } else {
            this.averageHeartRate = 0;
            this.averageHeartPercent = 0;
        }

    }

    public int getAverageHeartPercent() {
        return averageHeartPercent;
    }

    public void setAverageHeartPercent(int averageHeartPercent) {
        this.averageHeartPercent = averageHeartPercent;
    }


    public ArrayList<Integer> getTempList() {
        return tempList;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        DevicesDataShowBean deviceBean = (DevicesDataShowBean) o;
        String type;
        /**
         * 对主界面的数据还是对课程的数据进行排序
         */
        if (CacheDataUtil.getShowFragment().equals(Constant.TASK_FRAGMENT)) {
            type = deviceBean.getCourseSortType();
        } else {
            type = deviceBean.getSortType();
        }
        int count = 0;
        switch (type) {
            /**
             * 默认排序方式
             * 按照加入的时间排序
             */
            case Constant.TYPE_DEF:
                if ((this.getJoinTime() / 1000) > (deviceBean.getJoinTime() / 1000)) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
            /**
             * 根据cal路里进行排序
             */
            case Constant.TYPE_CAL:
                if (this.getCal() > deviceBean.getCal()) {
                    count = -1;
                } else if (this.getCal() < deviceBean.getCal()) {
                    count = 1;
                } else {
                    if (this.getMatchRate() > deviceBean.getMatchRate()) {
                        count = -1;
                    } else if (this.getMatchRate() < deviceBean.getMatchRate()) {
                        count = 1;
                    } else {
                        if (this.getPoint() > deviceBean.getPoint()) {
                            count = -1;
                        } else if (this.getPoint() < deviceBean.getPoint()) {
                            count = 1;
                        } else {
                            count = 1;

                        }
                    }
                }
                break;
            /**
             * 根据心率值从高到底进行排序
             */
            case Constant.TYPE_HR:
                Integer hr1 = this.getLiveHeartRate();
                Integer hr2 = deviceBean.getLiveHeartRate();
                if (hr1 > hr2) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
            /**
             * 根据百分比从高到底进行排序
             */
            case Constant.TYPE_PERCENT:
                Integer precent1 = Integer.parseInt(this.getPrecent());
                Integer precent2 = Integer.parseInt(deviceBean.getPrecent());
                if (precent1 > precent2) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
            /**
             * 根据点数从高到底进行排序
             */
            case Constant.TYPE_POINT:
                if (this.getPoint() > deviceBean.getPoint()) {
                    count = -1;
                } else {
                    count = 1;
                }
                break;
        }
        return count;
    }


   /* public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
*/


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DevicesDataShowBean) {
            return ((DevicesDataShowBean) obj).getDevicesSN().equals(this.devicesSN);
        }
        return false;
    }

    @Override
    public String toString() {
        return "DevicesDataShowBean{" +
                "pkTeam=" + pkTeam +
                ", ranking=" + ranking +
                ", time=" + time +
                ", devicesSN='" + devicesSN + '\'' +
                ", courseTime=" + courseTime +
                ", userId='" + userId + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", headUrl='" + headUrl + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", joinTime=" + joinTime +
                ", sex='" + sex + '\'' +
                ", cal=" + cal +
                ", liveHeartRate=" + liveHeartRate +
                ", averageHeartRate=" + averageHeartRate +
                ", averageHeartPercent=" + averageHeartPercent +
                ", point=" + point +
                ", precent='" + precent + '\'' +
                ", totalCount=" + totalCount +
                ", matchCount=" + matchCount +
                ", matchRate=" + matchRate +
                ", sortType='" + sortType + '\'' +
                ", courseSortType='" + courseSortType + '\'' +
                ", courseId='" + courseId + '\'' +
                ", isOnline=" + isOnline +
                ", mIntervalHearts=" + mIntervalHearts +
                ", hrTaskList=" + hrTaskList +
                ", hrList=" + hrList +
                ", tempList=" + tempList +
                ", allHrList=" + allHrList +
                ", calAllHrList=" + calAllHrList +
                ", minHrList=" + minHrList +
                ", mAnimateTime=" + mAnimateTime +
                ", currentPoint=" + currentPoint +
                ", lastPoint=" + lastPoint +
                ", lastStreng=" + lastStreng +
                ", mDatas=" + mDatas +
                '}';
    }
}
