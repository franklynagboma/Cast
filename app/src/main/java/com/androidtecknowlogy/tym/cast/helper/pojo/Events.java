package com.androidtecknowlogy.tym.cast.helper.pojo;

/**
 * Created by AGBOMA franklyn on 6/19/17.
 */

public class Events {

    private String posterName, createdDate, eventTitle, eventText, eventDay_Time;

    public Events() {
    }

    public Events(String posterName, String createdDate,
                  String eventTitle, String eventText, String eventDay_Time) {
        this.posterName = posterName;
        this.createdDate = createdDate;
        this.eventTitle = eventTitle;
        this.eventText = eventText;
        this.eventDay_Time = eventDay_Time;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    public String getEventDay_Time() {
        return eventDay_Time;
    }

    public void setEventDay_Time(String eventDay_Time) {
        this.eventDay_Time = eventDay_Time;
    }
}
