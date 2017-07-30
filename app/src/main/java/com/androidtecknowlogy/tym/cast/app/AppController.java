package com.androidtecknowlogy.tym.cast.app;

import android.app.Application;
import android.os.Build;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;
import com.androidtecknowlogy.tym.cast.helper.pojo.Settings;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AGBOMA franklyn on 6/19/17.
 */

public class AppController extends Application {

    private final String LOG_TAG = AppController.class.getSimpleName();

    private static AppController instance;
    public static final String currentDevice = Build.MANUFACTURER.toUpperCase() +" "+ Build.MODEL;
    public static FirebaseDatabase firebaseDatabase;
    public final String CASTS = "Casts";
    public final String EVENTS = "Events";
    public final String ADMINS = "Admins";
    public final String SETTINGS = "Settings";
    public static final String SHOW_DOB = "showDob";
    public static final String CAST_UPDATE = "castUpdate";
    public static DatabaseReference castsData;
    public static DatabaseReference adminsData;
    public static DatabaseReference eventList;
    public static DatabaseReference settingsData;
    public static HashMap<String, Settings> settingMap;
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
                .child(CASTS);
        adminsData = firebaseDatabase.getReference()
                .child(ADMINS);
        eventList = firebaseDatabase.getReference()
                .child(EVENTS);
        settingsData = firebaseDatabase.getReference()
                .child(SETTINGS);

        detailsCastItems = new ArrayList<>();
        settingMap = new HashMap<>();

    }

    public static synchronized AppController getInstance(){
        return instance;
    }

    public GoogleSignInOptions googleSignInOptions() {
        /**
         * creating sign in option --> user not recognize.
         */
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return gso;
    }


}
