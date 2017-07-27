package com.androidtecknowlogy.tym.cast.helper.pojo;

/**
 * Created by AGBOMA franklyn on 6/18/17.
 */

public class CastItems {

    private String castImage, castName, castTitle, castGender,
            castMobile,castEmail,castSummary, castPassword,  castDob, castJoined;

    public CastItems() {
    }

    public CastItems(String castImage, String castName, String castTitle,
                     String castGender, String castMobile, String castEmail,
                     String castSummary, String castPassword, String castDob, String castJoined) {
        this.castImage = castImage;
        this.castName = castName;
        this.castTitle = castTitle;
        this.castGender = castGender;
        this.castMobile = castMobile;
        this.castEmail = castEmail;
        this.castSummary = castSummary;
        this.castPassword = castPassword;
        this.castDob = castDob;
        this.castJoined = castJoined;
    }

    public String getCastImage() {
        return castImage;
    }

    public void setCastImage(String castImage) {
        this.castImage = castImage;
    }

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getCastTitle() {
        return castTitle;
    }

    public void setCastTitle(String castTitle) {
        this.castTitle = castTitle;
    }

    public String getCastGender() {
        return castGender;
    }

    public void setCastGender(String castGender) {
        this.castGender = castGender;
    }

    public String getCastMobile() {
        return castMobile;
    }

    public void setCastMobile(String castMobile) {
        this.castMobile = castMobile;
    }

    public String getCastEmail() {
        return castEmail;
    }

    public void setCastEmail(String castEmail) {
        this.castEmail = castEmail;
    }

    public String getCastSummary() {
        return castSummary;
    }

    public void setCastSummary(String castSummary) {
        this.castSummary = castSummary;
    }

    public String getCastPassword() {
        return castPassword;
    }

    public void setCastPassword(String castPassword) {
        this.castPassword = castPassword;
    }

    public String getCastDob() {
        return castDob;
    }

    public void setCastDob(String castDob) {
        this.castDob = castDob;
    }

    public String getCastJoined() {
        return castJoined;
    }

    public void setCastJoined(String castJoined) {
        this.castJoined = castJoined;
    }
}
