package com.jkcq.managersetting.bean;

/**
 * created by wq on 2019/6/27
 */
public class VersionInfo {

    /**
     * uid : 2
     * createTime : 2019-04-22 10:29:30
     * page : 1
     * size : 10
     * type : 1115581792112472065
     * appVersionCode : 8
     * url : https://isportcloud.oss-cn-shenzhen.aliyuncs.com/manager/app-release(1).apk
     * update : null
     * message : null
     * appVersionName : 2.1
     */

    private String deviceType;
    private String appVersionName;
    private int appVersionCode;
    private boolean isFocusUpdate;
    private String downloadUrl;
    private String upgradeTitle;
    private String upgradeDesc;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public boolean isFocusUpdate() {
        return isFocusUpdate;
    }

    public void setFocusUpdate(boolean focusUpdate) {
        isFocusUpdate = focusUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpgradeTitle() {
        return upgradeTitle;
    }

    public void setUpgradeTitle(String upgradeTitle) {
        this.upgradeTitle = upgradeTitle;
    }

    public String getUpgradeDesc() {
        return upgradeDesc;
    }

    public void setUpgradeDesc(String upgradeDesc) {
        this.upgradeDesc = upgradeDesc;
    }
}
