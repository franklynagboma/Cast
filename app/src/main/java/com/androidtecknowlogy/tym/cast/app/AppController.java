package com.androidtecknowlogy.tym.cast.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.helper.io.ConnectionReceiver;
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
    public static boolean isGuest;
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
        //initialize connection receiver
        new ConnectionReceiver().onReceive(getInstance(), new Intent());
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

    public void toastMsg (Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public void snackMsg (LayoutInflater inflater, View proposed,
                           Context context, String msg, String iconType, String length) {
        Snackbar snackbar = Snackbar.make(proposed.getRootView(), "",
                length.equals("long")?Snackbar.LENGTH_LONG :Snackbar.LENGTH_SHORT);
        //get snack bar view and customize with snackbar_layout.
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        //hide snack bar fix text view layout
        ((TextView) layout.findViewById(android.support.design.R.id.snackbar_text))
                .setVisibility(View.INVISIBLE);
        //attack view
        View snackView = inflater.inflate(R.layout.snackbar_layout, null, false);
        TextView textView = (TextView) snackView.findViewById(R.id.snack_text);
        textView.setTypeface(AppController.getProximaFace(context));
        ImageView imageView = (ImageView) snackView.findViewById(R.id.snack_image);
        TextView settingBtn = (TextView) snackView.findViewById(R.id.snack_btn);
        settingBtn.setTypeface(AppController.getDroidFace(context));
        settingBtn.setVisibility(View.INVISIBLE);

        if(iconType.equalsIgnoreCase("info"))
            imageView.setImageResource(R.drawable.info);
        if(iconType.equalsIgnoreCase("wifi")) {
            imageView.setImageResource(R.drawable.wifi);
            //text settings onClick.
            settingBtn.setVisibility(View.VISIBLE);
            settingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    if(intent.resolveActivity(getPackageManager()) != null)
                        startActivity(intent);
                }
            });
        }
        textView.setText(msg);

        //set on click for network setting.
        //show custom snack bar.
        layout.addView(snackView);
        layout.setBackgroundResource(R.color.colorPrimary);
        snackbar.show();

    }

    //set up fonts for text.
    public static Typeface getDroidFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/DroidSerif-Bold.ttf");
    }
    public static Typeface getProximaFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/proxima-nova-regular.ttf");
    }
    public static Typeface getRalewayBoldFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/Raleway-Bold.ttf");
    }
    public static Typeface getRalewayMediumFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/Raleway-Medium.ttf");
    }
    public static Typeface getAndroidFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/AndroidClock.ttf");
    }


}
