package com.androidtecknowlogy.tym.cast.helper.io;

import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by AGBOMA franklyn on 6/28/17.
 */

public class CustomDataFormat {

    private final String LOG_TAG = CustomDataFormat.class.getSimpleName();

    public CustomDataFormat() {
    }

    public String getDateFormat(Date data, String type) {
        String formatted = DateFormat.getDateInstance().format(data);
        Log.i(LOG_TAG, "Date initial format " + formatted);
        //get comma off
        String[] splitOne = formatted.split(",");
        //add all
        formatted = splitOne[0].trim() +" "+ splitOne[1].trim();
        //get space off
        String[] splitFormatted = formatted.split(" ");
        //check the type eg = month,day
        if(type.equals("m/d"))
            formatted = splitFormatted[0] +" "+ splitFormatted[1];
        //eg = month,year
        else if(type.equals("m/y"))
            formatted = splitFormatted[0] +" "+ splitFormatted[2];
        //eg = day,month,year. Still the same as initial but since it was override, insert again.
        else if(type.equals("m/d/y"))
            formatted = splitFormatted[0] +" "+ splitFormatted[1] +", "+ splitFormatted[2] +".";

        return formatted;
    }

    public String getTimeFormat(Date date, String type) {
        String formatted = DateFormat.getTimeInstance().format(date);
        Log.i(LOG_TAG, "Time initial format " + formatted);

        String[] splitFormatted = formatted.split(":");
        //check the type eg = 1:1
        if(type.matches("\\d+" +":"+ "\\d+")) {
            formatted = splitFormatted[0] +":"+ splitFormatted[1];
        }
        if(type.matches("\\d+")) {
            formatted = splitFormatted[0];
        }
        //check for day period.
        if(Integer.parseInt(splitFormatted[0]) >= 12)
            formatted = formatted + " PM";
        else
            formatted = formatted + " AM";

        return formatted;
    }

}
