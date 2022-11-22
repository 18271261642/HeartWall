package com.jkcq.hrwtv.heartrate.model.filtershowmodel;

import android.content.Context;

import com.jkcq.hrwtv.base.mvp.BasePresenterModel;

/*
 * @author mhj
 * Create at 2018/4/19 17:30 
 */
public class FilterFragmentModelImp extends BasePresenterModel<FilterFragmentView> implements FilterFragmentModel {

    public FilterFragmentModelImp(Context context, FilterFragmentView baseView) {
        super(context, baseView);
    }

    @Override
    public void getUserInfos(String deviceId) {

    }



    /*@Override
    public void getTaskDetails(String taskId) {

        String url = AllocationApi.queryTaskDetails(taskId);
        HashMap<String, Object> parmes = new HashMap<>();
        parmes.put("userId", "");
        parmes.put("interfaceId", String.valueOf(0));

        NetUtils.doPost(url, parmes, TaskInfo.class, new OnHttpRequestCallBack<TaskInfo>() {
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
    }*/
}
