package com.jkcq.hrwtv.heartrate.model.allcoursemodel;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.base.mvp.BasePresenterModel;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;
import com.jkcq.hrwtv.heartrate.bean.TaskListBean;
import com.jkcq.hrwtv.okhttp.NetUtils;
import com.jkcq.hrwtv.okhttp.OnHttpRequestCallBack;
import com.jkcq.hrwtv.util.JsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * 
 * 
 * @author mhj
 * Create at 2018/4/17 10:45 
 */
public class CourseFragmentModelImp extends BasePresenterModel<HrCourseTypeFragmentView> implements CourseTypeFragmentModel {
    public CourseFragmentModelImp(Context context, HrCourseTypeFragmentView baseView) {
        super(context, baseView);
    }

    @Override
    public void getAllCourseType(Context context) {

        String url = AllocationApi.queryTask();
        HashMap<String, Object> parmes = new HashMap<>();
        parmes.put("userId", "0");
        parmes.put("interfaceId", String.valueOf(0));

        NetUtils.doGet(url, null, TaskListBean.class, new OnHttpRequestCallBack<TaskListBean>() {
            @Override
            public void onSuccess(TaskListBean bean) {
                baseView.getAllCourseTypeSuccess((ArrayList<TaskInfo>) bean.getList());
            }

            @Override
            public void onGetErrorCode(TaskListBean bean) {
                baseView.onRespondError(bean.getMessage());
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                baseView.onRespondError(msg);
            }
        });
//        String json = getJson("coureselist.json",context);
//        TaskListBean bean = JsonUtils.getInstance().parseObject(json,TaskListBean.class);
//        baseView.getAllCourseTypeSuccess((ArrayList<TaskInfo>) bean.getList());

    }


    public static String getJson(String fileName,Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
