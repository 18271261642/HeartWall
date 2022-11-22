package com.jkcq.hrwtv.heartrate.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jkcq.hrwtv.base.mvp.BasePresenterModel;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.heartrate.bean.DevicesDataShowBean;
import com.jkcq.hrwtv.heartrate.model.resultmodel.TaskResultModel;
import com.jkcq.hrwtv.http.widget.BaseObserver;
import com.jkcq.hrwtv.util.CacheDataUtil;
import com.jkcq.hrwtv.util.HeartRateConvertUtils;
import com.jkcq.hrwtv.wu.newversion.bean.RequestDynamicBean;
import com.jkcq.hrwtv.wu.newversion.bean.RequestResultEntity;
import com.jkcq.hrwtv.http.RetrofitHelper;
import com.jkcq.hrwtv.http.bean.BaseResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Handler;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/*
 * @author mhj
 * Create at 2018/4/16 16:57
 */
public class MainActivityModelImp extends BasePresenterModel<MainActivityView> implements MainActivityModel, TaskResultModel {

    public MainActivityModelImp(Context context, MainActivityView baseView) {
        super(context, baseView);

    }

    @Override
    public void getClubFunction() {

    }

    @Override
    public void getCourseList(String clubId, String roomId) {
    }

    @Override
    public void getAllUser(String clubId, String roomId) {
    }


    public void postHeartRateData(ArrayList<DevicesDataShowBean> list, String heartRateCourseId, String winGroup, String modeType, long endTime) {
//        Collections.sort(list, new Comparator<DevicesDataShowBean>() {
//            @Override
//            public int compare(DevicesDataShowBean devicesDataShowBean, DevicesDataShowBean t1) {
//                return String.valueOf(t1.getPoint()).compareTo(String.valueOf(devicesDataShowBean.getPoint()));
//            }
//        });

//        try {
//            list.sort(Comparator.comparing(DevicesDataShowBean::getPoint).reversed().thenComparing(DevicesDataShowBean::getCal).reversed());
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        //list.sort(Comparator.comparing(DevicesDataShowBean::getPoint));
        ArrayList<RequestResultEntity> requestResultEntities = new ArrayList<>();
        String strendTime = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        SimpleDateFormat formatYYMMDD = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        strendTime = format.format(new Date(endTime));
        String exerciseDay = formatYYMMDD.format(new Date(endTime));
        String startTime = "";
        int duration = 0;
        DevicesDataShowBean srcBean;
        RequestResultEntity desBean;
        Gson gson = new Gson();
        int avgHr = 0;
        int maxHr = 0;
        int minHr = 0;
        String matchingRate = "";
        String groupType = "";//红队是0 蓝队是1
        String ranking = "";

        for (int j = 0; j < list.size(); j++) {
            srcBean = list.get(j);
            if (srcBean.getRanking() == 0) {
                ranking = (j + 1) + "";
            } else {
                ranking = srcBean.getRanking() + "";
            }
            duration = (int) ((endTime - list.get(j).getJoinTime()) / 1000);
            desBean = new RequestResultEntity();
            if (srcBean.getPkTeam() == Constant.MODE_PK_BLUE) {
                groupType = Constant.PK_BLUE + "";
            } else if (srcBean.getPkTeam() == Constant.MODE_PK_RED) {
                groupType = Constant.PK_RED + "";
            }
            // groupType = srcBean.getPkTeam() + "";
            desBean.setMemberId(srcBean.getUserId());
            if (CacheDataUtil.mCurrentRange > 5) {
                matchingRate = "";
            } else {
                matchingRate = srcBean.getMatchRate() + "";
            }
            startTime = format.format(new Date(srcBean.getJoinTime()));
            /*int size = srcBean.getAllHrList().size();
            int sum = 0;
            int len = 0;
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    if (srcBean.getAllHrList().get(i) > 30) {
                        sum += srcBean.getAllHrList().get(i);
                        len++;
                    }
                }
            }
            if (len > 0 && sum > 0) {
                srcBean.getMinHrList().add(sum / len);
            }*/
            int size = srcBean.getMinHrList().size();
            int sum = 0;
            int len = 0;

            ArrayList<Integer> tempMinList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (srcBean.getMinHrList().get(i) > 30) {
                    sum += srcBean.getMinHrList().get(i);
                    len++;
                    tempMinList.add(srcBean.getMinHrList().get(i));
                }
            }
            //  Log.e("上传数据:", "分钟数据" + srcBean.getMinHrList() + ",心率数据集合=" + srcBean.getAllHrList());

            if (sum == 0) {
                avgHr = 0;
                maxHr = 0;
                minHr = 0;
            } else {
                avgHr = sum / len;
                maxHr = Collections.max(srcBean.getMinHrList());
                if (tempMinList.size() > 0) {
                    minHr = Collections.min(tempMinList);
                }
            }

            //结果页面需要
            srcBean.setAverageHeartRate(avgHr);
            int precent = (int) HeartRateConvertUtils.hearRate2Percent(
                    avgHr,
                    (double) HeartRateConvertUtils.getMaxHeartRate(srcBean.getAge())
            );
            Log.e("上传数据:", "心率强度 avgHr" + avgHr + ",srcBean.getAge()=" + srcBean.getAge() + "precent=" + precent);
            srcBean.setAverageHeartPercent(precent);
            desBean.setAverageStrength(precent + "");
            desBean.setAge(srcBean.getAge());
            desBean.setSex(srcBean.getSex());
            desBean.setCalorie((int) srcBean.getCal());
            desBean.setDuration(duration);
            desBean.setAverageRate(avgHr);
            desBean.setEmpiricalValue((int) srcBean.getPoint());
            desBean.setEndTime(strendTime);
            desBean.setExerciseDay(exerciseDay);
            desBean.setGroupType(groupType);
            desBean.setHeartRateArray(gson.toJson(srcBean.getMinHrList()));
            desBean.setStartTime(startTime);
            desBean.setMatchingRate(matchingRate);
            desBean.setMaxRate(maxHr);
            desBean.setHeartRateCourseId(heartRateCourseId);
            desBean.setModeType(modeType);
            desBean.setMinRate(minHr);
            desBean.setRanking(ranking);
            desBean.setStartTime(startTime);
            Log.e("desBean", desBean.toString());
            requestResultEntities.add(desBean);
        }
        if (!modeType.equals("0")) {
            CacheDataUtil.sUploadHeartData.clear();
            CacheDataUtil.sUploadHeartData.addAll(list);
        }
        if (requestResultEntities.size() == 0) {
            baseView.uploadAllDataSuccess();
            CacheDataUtil.clearCourseList();
            return;
        }
        RequestDynamicBean requset = new RequestDynamicBean();
        requset.setDatlist(requestResultEntities);
        if (!TextUtils.isEmpty(winGroup)) {
            requset.setWinGroup(winGroup);
        }
        if (!TextUtils.isEmpty(heartRateCourseId)) {
            requset.setHeartRateCourseId(heartRateCourseId);
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(requset));
        RetrofitHelper.INSTANCE.getService().report(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<Boolean>>(baseView) {
                    @Override
                    public void onSuccess(BaseResponse<Boolean> baseResponse) {
                        baseView.uploadAllDataSuccess();
                        CacheDataUtil.clearCourseList();
                    }
                });
    }


    @Override
    public void postPk
            (ArrayList<DevicesDataShowBean> redlist, ArrayList<DevicesDataShowBean> bluelist, String
                    heartRateCourseId, String winGroup, String modeType, long endTime, double redSum,
             double blueSum) {


        double maxRedCal = 0.0;
        double maxBlueCal = 0.0;
        DevicesDataShowBean bean = null;
        for (int i = 0; i < redlist.size(); i++) {
            bean = redlist.get(i);
            bean.setSortType(Constant.TYPE_POINT);
        }
        for (int i = 0; i < bluelist.size(); i++) {
            bean = bluelist.get(i);
            bean.setSortType(Constant.TYPE_POINT);
        }
        Collections.sort(redlist);
        Collections.sort(bluelist);
        for (int i = 0; i < redlist.size(); i++) {
            bean = redlist.get(i);
            bean.setRanking(i + 1);
            if (bean.getCal() > maxRedCal) {
                maxRedCal = bean.getCal();
            }
        }
        for (int i = 0; i < bluelist.size(); i++) {
            bean = bluelist.get(i);
            bean.setRanking(i + 1);
            if (bean.getCal() > maxBlueCal) {
                maxBlueCal = bean.getCal();
            }
        }
        if (redSum == blueSum) {
            if (maxRedCal > maxBlueCal) {
                winGroup = Constant.PK_RED + "";
                Constant.WIM_TEAM = Constant.PK_RED;
            } else {
                winGroup = Constant.PK_BLUE + "";
                Constant.WIM_TEAM = Constant.PK_BLUE;
            }
        } else if (redSum > blueSum) {
            winGroup = Constant.PK_RED + "";
            Constant.WIM_TEAM = Constant.PK_RED;
        } else {
            winGroup = Constant.PK_BLUE + "";
            Constant.WIM_TEAM = Constant.PK_BLUE;
        }
        Constant.sumRedCal = redSum;
        Constant.sumBlueCal = blueSum;

        ArrayList<DevicesDataShowBean> list = new ArrayList<>();
        list.addAll(redlist);
        list.addAll(bluelist);
        postHeartRateData(list, heartRateCourseId, winGroup, modeType, endTime);

    }

    @Override
    public void postHwall(ArrayList<DevicesDataShowBean> sumList, String modeType) {
        long endTime = System.currentTimeMillis();
        int duration = 0;
        ArrayList<DevicesDataShowBean> list = new ArrayList<>();
        for (int i = 0; i < sumList.size(); i++) {
            duration = (int) ((endTime - sumList.get(i).getJoinTime()) / 1000);
            Log.e("上传数据:", "心率强度 list" + list.size() + ",duration=" + duration);
            if (duration <= 2 * 60) {
                Log.e("上传数据:", "时间不够" + duration + "");
                continue;
            }
            sumList.get(i).setSortType(Constant.TYPE_CAL);
            list.add(sumList.get(i));

        }
        Collections.sort(list);
        Log.e("上传数据:", "心率强度 list" + list.size() + ",endTime=" + endTime);

        postHeartRateData(list, "", "", modeType, endTime);
    }

    @Override
    public void postCourse(ArrayList<DevicesDataShowBean> sumList, String
            heartRateCourseId, String modeType, long endTime) {
        for (int i = 0; i < sumList.size(); i++) {
            sumList.get(i).setSortType(Constant.TYPE_POINT);
        }
        Collections.sort(sumList);

        postHeartRateData(sumList, heartRateCourseId, "", modeType, endTime);
    }

}
