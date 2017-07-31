package com.androidtecknowlogy.tym.cast.helper.pojo;

/**
 * Created by AGBOMA franklyn on 7/29/17.
 */

public class Settings {


    private String showDob, device;

    public Settings() {
    }

    public Settings(String showDob, String device) {
        this.showDob = showDob;
        this.device = device;
    }

    public String getShowDob() {
        return showDob;
    }

    public void setShowDob(String showDob) {
        this.showDob = showDob;
    }


    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
