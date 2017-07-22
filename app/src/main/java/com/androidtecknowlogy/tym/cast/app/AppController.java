package com.androidtecknowlogy.tym.cast.app;

import android.app.Application;

import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AGBOMA franklyn on 6/19/17.
 */

public class AppController extends Application {

    private static AppController instance;
    public static FirebaseDatabase firebaseDatabase;
    public static final String CASTS = "Casts";
    public static final String EVENTS = "Events";
    public static final String ADMINS = "Admins";
    public static DatabaseReference castsData;
    public static DatabaseReference adminsData;
    public static DatabaseReference eventList;
    public static List<CastItems> detailsCastItems;
    public static String getLast10Digit = "";
    public static String getSeparatedDigit = "";

    //preference key
    public static final String getBoolean = "pref";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //initialize fire base database
        firebaseDatabase = FirebaseDatabase.getInstance();
        //initialize fire base database
        castsData = firebaseDatabase.getReference()
                .child(AppController.CASTS);
        adminsData = firebaseDatabase.getReference()
                .child(AppController.ADMINS);
        eventList = firebaseDatabase.getReference()
                .child(AppController.EVENTS);

        detailsCastItems = new ArrayList<>();

    }

    public static synchronized AppController getInstance(){
        return instance;
    }

}
