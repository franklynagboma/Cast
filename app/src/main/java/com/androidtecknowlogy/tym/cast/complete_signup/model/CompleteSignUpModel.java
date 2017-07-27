package com.androidtecknowlogy.tym.cast.complete_signup.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.helper.pojo.CastItems;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AGBOMA franklyn on 6/27/17.
 */

public class CompleteSignUpModel implements Constant.PresenterSendSignUpToModel {

    private final String LOG_TAG = CompleteSignUpModel.class.getSimpleName();
    private Constant.ModelSendToPresentSignUpSaved modelSendToPresentSignUpSaved;
    private ProgressDialog loading;

    public CompleteSignUpModel() {
    }

    public void setModelSendToPresentSignUpSaved(Constant.ModelSendToPresentSignUpSaved
                                                         modelSendToPresentSignUpSaved) {
        this.modelSendToPresentSignUpSaved = modelSendToPresentSignUpSaved;
    }

    @Override
    public void signUpInfo(String uId, String photo, String email, String gender,
                           String month_year, String name, String mobile,
                           String title, String password,String dob, String summary,
                           Context context) {

        Log.i(LOG_TAG, "\nuId "+uId +"\nphoto "+photo +"\nemail "+email +"\ngender "+gender
                +"\nmonth_year "+month_year +"\nname "+name +"\nmobile "+mobile +"\ntitle "+title
                +"\npassword "+password +"\ndob "+dob +"\nsummary "+summary);

        //send to save online
        loading = new ProgressDialog(context);
        loading.setCancelable(false);
        loading.setTitle("Cast");
        loading.setMessage("saving data...");
        loading.show();

        AppController.castsData.child(uId)
                .setValue(new CastItems(photo, name,title,gender,mobile,
                        email,null,password, dob, month_year));
        AppController.castsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(null != dataSnapshot) {
                    if(loading.isShowing())
                        loading.dismiss();
                    modelSendToPresentSignUpSaved.saving(true);
                }
                else {
                    if(loading.isShowing())
                        loading.dismiss();
                    modelSendToPresentSignUpSaved.saving(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(loading.isShowing())
                    loading.dismiss();
                modelSendToPresentSignUpSaved.saving(false);
            }
        });
    }
}
