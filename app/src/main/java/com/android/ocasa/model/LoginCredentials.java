package com.android.ocasa.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ignacio on 27/07/16.
 */
public class LoginCredentials {

    private String user;
    private String password;
    private String imei;

    @SerializedName("app_version")
    private String appVersion;

    @SerializedName("android_version")
    private String androidVersion;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }
}
