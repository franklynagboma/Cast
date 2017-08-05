package com.androidtecknowlogy.tym.cast.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.cast.activity_view.CastActivity;
import com.androidtecknowlogy.tym.cast.complete_signup.activity_view.GoogleSignInActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AGBOMA franklyn on 7/25/17.
 */

public class LoginActivity extends AppCompatActivity {

    private final String LOG_TAG = LoginActivity.class.getSimpleName();

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;
    private String getEmail, getPassword;
    private GoogleApiClient googleApiClient;
    private final String USERS_VALUE = "users_password";
    @BindView(R.id.cast_email)
    EditText castEmail;
    @BindView(R.id.cast_password)
    EditText castPassword;
    @BindView(R.id.input_password)
    TextInputLayout inputLayout;
    @BindView(R.id.btn_login)
    Button loginBtn;
    @BindView(R.id.not_reg)
    TextView notReg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //get stored preference email and password
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        getEmail = pref.getString("email", "");
        getPassword = pref.getString("password","");

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(LOG_TAG, " onConnectionFailed");
                        AppController.getInstance().toastMsg(LoginActivity.this,
                                "Fails, Check internet connection.");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, AppController.getInstance().googleSignInOptions())
                .build();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(USERS_VALUE, castPassword.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        castPassword.setText(savedInstanceState.getString(USERS_VALUE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        //set font type
        castEmail.setTypeface(AppController.getProximaFace(this));
        castPassword.setTypeface(AppController.getProximaFace(this));
        loginBtn.setTypeface(AppController.getDroidFace(this));
        notReg.setTypeface(AppController.getDroidFace(this));
        //setting up text area.
        castEmail.setEnabled(false);
        castEmail.setText(getEmail);
    }

    @OnClick(R.id.btn_login)
    public void onLogInButtonClicked() {
        String getTextPassword = castPassword.getText().toString();

        if(!getTextPassword.equals(getPassword))
            AppController.getInstance().toastMsg(this,"Wrong password");
        else {
            //save user login
            prefEdit = pref.edit();
            prefEdit.putBoolean("login", true);
            prefEdit.apply();
            startActivity("castActivity");
        }
    }

    @OnClick(R.id.user_not_reg)
    public void onUserNotRegClicked() {
        //sign out user first
        signOut();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //check is the status is not successful,the recall method.
                        if(!status.isSuccess())
                            signOut();
                        else {
                            prefEdit = pref.edit();
                            prefEdit.putBoolean(Intent.EXTRA_TEXT, false);
                            prefEdit.putBoolean("login", false);
                            prefEdit.putString("uid", "");
                            prefEdit.putString("image", "");
                            prefEdit.putString("email", "");
                            prefEdit.putString("name", "");
                            prefEdit.putString("password", "");
                            prefEdit.putString("gender", "");
                            prefEdit.putString("dob", "");
                            prefEdit.putString("mobile", "");
                            prefEdit.putString("title", "");
                            prefEdit.putString("summary", "");
                            prefEdit.apply();
                            //call google activity for user to do as which.
                            startActivity("googleActivity");
                        }
                    }
                });
    }


    private void startActivity(String activity) {
        this.finish();
        startActivity(new Intent(LoginActivity.this, activity.equals("castActivity")
                ?CastActivity.class :GoogleSignInActivity.class));
    }
}
