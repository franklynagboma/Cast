package com.androidtecknowlogy.tym.cast.complete_signup.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.androidtecknowlogy.tym.cast.faces.Constant;
import com.androidtecknowlogy.tym.cast.complete_signup.model.CompleteSignUpModel;
import com.androidtecknowlogy.tym.cast.helper.io.CustomDataFormat;

import java.util.Date;

/**
 * Created by AGBOMA franklyn on 6/27/17.
 */

public class CompleteSignUpPresenter implements Constant.CompleteSignUpToPresenter,
        Constant.ModelSendToPresentSignUpSaved{

    private final String LOG_TAG = CompleteSignUpPresenter.class.getSimpleName();
    private String saveLastName = "";

    private Constant.PresenterSendSignUpToModel presenterSendSignUpToModel;
    private Constant.PresenterSendsSignUpCompletedToCompleteSignUpFragment
            presenterSendsSignUpCompletedToCompleteSignUpFragment;
    //private CompleteSignUpModel model;

    public CompleteSignUpPresenter() {
    }

    //set up presenter signUp logic to model
    public void setPresenterSendSignUpToModel(Constant.PresenterSendSignUpToModel
                                                    presenterSendSignUpToModel,
                                              CompleteSignUpModel model,
                                              Constant.PresenterSendsSignUpCompletedToCompleteSignUpFragment
                                                      presenterSendsSignUpCompletedToCompleteSignUpFragment) {
        this.presenterSendSignUpToModel = presenterSendSignUpToModel;
        model.setModelSendToPresentSignUpSaved(this);
        this.presenterSendsSignUpCompletedToCompleteSignUpFragment =
                presenterSendsSignUpCompletedToCompleteSignUpFragment;
    }
    //set up presenter signUp logic to completeSignUpFrgament


    /*public void setPresenterSendsSignUpCompletedToCompleteSignUpFragment(
            Constant.PresenterSendsSignUpCompletedToCompleteSignUpFragment
                    presenterSendsSignUpCompletedToCompleteSignUpFragment) {
        this.presenterSendsSignUpCompletedToCompleteSignUpFragment =
                presenterSendsSignUpCompletedToCompleteSignUpFragment;
    }*/

    @Override
    public void userSignUp(String uId, String photo, String email, String gender, EditText mobile,
                           EditText sunName, EditText otherNames, EditText title, EditText password,
                           EditText confirmPassword, Context context) {

        //get EditText strings
        String sName = sunName.getText().toString().trim();
        String oNames = otherNames.getText().toString().trim();
        String mMobile = mobile.getText().toString().trim();
        String tTitle = title.getText().toString().trim();
        String pPassword = password.getText().toString().trim();
        String cPassword = confirmPassword.getText().toString().trim();

        //check and preform logic operations
        if(TextUtils.isEmpty(sName) || TextUtils.isEmpty(oNames)
                || TextUtils.isEmpty(mMobile) || TextUtils.isEmpty(tTitle)
                || TextUtils.isEmpty(pPassword) || TextUtils.isEmpty(cPassword)) {
            presenterSendsSignUpCompletedToCompleteSignUpFragment
                    .startActivity("empty/Please fill all");
        }
        else {
            Log.i(LOG_TAG, "presenter get detail");
            //use / to send error
            if(!pPassword.equals(cPassword)) {
                Log.i(LOG_TAG, "password not equal " +pPassword +" " +cPassword);
                presenterSendsSignUpCompletedToCompleteSignUpFragment
                        .startActivity("password/Passwords not equal");
            }
            else {
                String getName = sName +" "+ oNames;// oNames may contain >1 names with space
                String[] getAllName = getName.split(" ");//split space to get names as a whole
                /**
                 * convert sun name to uppercase.
                 * As instructed, sun name should be the first name.
                 */
                getAllName[0] = getAllName[0].toUpperCase();
                getName = "";//set getName to empty
                for(int count =0; count <getAllName.length; count ++) {
                    //set getName to contains all names with sun name in uppercase.
                    getName = getName +" "+ getAllName[count];
                }
                // trim the space at the ending and send al to model
                getName = getName.trim();
                Log.i(LOG_TAG, "Names " + getName);
                saveLastName = getName;
                presenterSendSignUpToModel.signUpInfo(uId, photo, email, gender,
                        new CustomDataFormat().getDateFormat(new Date(),"m/y"),
                        getName,mMobile, tTitle, pPassword,context);
            }
        }

    }

    //data saving
    @Override
    public void saving(boolean value) {
        if(value)
            presenterSendsSignUpCompletedToCompleteSignUpFragment.startActivity(saveLastName);
        else
            presenterSendsSignUpCompletedToCompleteSignUpFragment
                    .startActivity("error");
    }
}
