package com.androidtecknowlogy.tym.cast.helper.pojo;

/**
 * Created by AGBOMA franklyn on 7/29/17.
 */

public class Settings {


    private String showDob, castUpdate;

    public Settings() {
    }

    public Settings(String showDob, String castUpdate) {
        this.showDob = showDob;
        this.castUpdate = castUpdate;
    }

    public String getShowDob() {
        return showDob;
    }

    public void setShowDob(String showDob) {
        this.showDob = showDob;
    }

    public String getCastUpdate() {
        return castUpdate;
    }

    public void setCastUpdate(String castUpdate) {
        this.castUpdate = castUpdate;
    }
}
