package com.jkcq.hrwtv.wu.mvp;

import android.content.Context;

import com.jkcq.hrwtv.app.BaseApp;
import com.jkcq.hrwtv.base.mvp.BasePresenter;
import com.jkcq.hrwtv.configure.Constant;
import com.jkcq.hrwtv.http.bean.AloneClubInfoBean;
import com.jkcq.hrwtv.http.widget.BaseObserver;
import com.jkcq.hrwtv.util.DeviceUtil;
import com.jkcq.hrwtv.util.SharedPreferencesUtil;
import com.jkcq.hrwtv.http.RetrofitHelper;
import com.jkcq.hrwtv.http.bean.BaseResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FlashPresenter extends BasePresenter<FlashContract.FlashView> implements FlashContract.IFlashModel {


    private FlashContract.FlashView mView;

    @Override
    public void attach(Context context, FlashContract.FlashView view) {
        mView = view;
        this.context = context;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public void getClubInfo() {
        HashMap<String, String> para = new HashMap<>();
        para.put("mac", DeviceUtil.getMac(BaseApp.getApp()));
        para.put("type", "0");
        RetrofitHelper.INSTANCE.getNoAuthservice().refreshLoginInfo(para)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResponse<AloneClubInfoBean>>(mView) {
                    @Override
                    public void onSuccess(BaseResponse<AloneClubInfoBean> BaseResponse) {
                        AloneClubInfoBean clubInfo = BaseResponse.getData();
                        if (mView != null) {
                            mView.getClubSuccess(clubInfo);

                            SharedPreferencesUtil.saveToSharedPreferences(Constant.CLUB_NAME, clubInfo.getClubName());
                            SharedPreferencesUtil.saveToSharedPreferences(Constant.CLUB_Id, clubInfo.getClubId());
                        }
                    }

                    @Override
                    public void onNullData() {
                        if (mView != null) {
                            mView.getClubFailed();
                        }
                    }
                });
    }
}