package com.jkcq.hrwtv.wu.newversion.activity.mamager;


import com.jkcq.managersetting.BaseView;
import com.jkcq.managersetting.bean.VersionInfo;

/*
 *
 *
 * @author mhj
 * Create at 2019/1/29 14:52
 */public interface ManagerMainView extends BaseView {


    void onCheckUpdateSuccess(VersionInfo versionInfo);

    void onClearCacheSuccess();


}
