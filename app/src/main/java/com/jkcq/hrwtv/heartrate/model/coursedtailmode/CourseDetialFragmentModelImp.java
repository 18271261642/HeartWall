package com.jkcq.hrwtv.heartrate.model.coursedtailmode;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.jkcq.hrwtv.AllocationApi;
import com.jkcq.hrwtv.base.mvp.BasePresenterModel;
import com.jkcq.hrwtv.heartrate.bean.TaskInfo;
import com.jkcq.hrwtv.okhttp.NetUtils;
import com.jkcq.hrwtv.okhttp.OnHttpRequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/*
 * @author mhj
 * Create at 2018/4/19 17:30 
 */
public class CourseDetialFragmentModelImp extends BasePresenterModel<CourseDetailFragmentView> implements CourseDetialFragmentModel {

    public CourseDetialFragmentModelImp(Context context, CourseDetailFragmentView baseView) {
        super(context, baseView);
    }

    @Override
    public void getTaskDetails(Context context,String taskId,String userId) {

        String url = AllocationApi.queryTaskDetails(taskId);
        HashMap<String, Object> parmes = new HashMap<>();
        parmes.put("userId", userId);
        parmes.put("interfaceId", String.valueOf(0));

        NetUtils.doGet(url, null, TaskInfo.class, new OnHttpRequestCallBack<TaskInfo>() {
            @Override
            public void onSuccess(TaskInfo bean) {
                baseView.getTaskSuccess(bean);
            }

            @Override
            public void onGetErrorCode(TaskInfo bean) {
                baseView.onRespondError(bean.getMessage());
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                baseView.onRespondError(msg);
            }
        });
//        String json = getJson("course.json",context);
//        TaskInfo bean = new Gson().fromJson(json,TaskInfo.class);
//        baseView.getTaskSuccess(bean);

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
