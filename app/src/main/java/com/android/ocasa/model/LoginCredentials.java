package com.android.ocasa.model;

/**
 * Created by ignacio on 27/07/16.
 */
public class LoginCredentials {

    private String user;
    private String password;
    private String imei;

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
}
