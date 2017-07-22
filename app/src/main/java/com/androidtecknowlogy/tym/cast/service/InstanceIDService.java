package com.androidtecknowlogy.tym.cast.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by AGBOMA franklyn on 7/20/17.
 */

public class InstanceIDService extends FirebaseInstanceIdService {

    private final String LOG_TAG = InstanceIDService.class.getSimpleName();

    public InstanceIDService() {
    }


    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(LOG_TAG, "RefreshedToken: " + refreshedToken);
    }

    private void sendRegTokenToServer(String token){}
}
