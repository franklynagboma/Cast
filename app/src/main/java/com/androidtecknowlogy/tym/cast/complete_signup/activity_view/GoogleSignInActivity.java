package com.androidtecknowlogy.tym.cast.complete_signup.activity_view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.complete_signup.model.CompleteSignUpModel;
import com.androidtecknowlogy.tym.cast.complete_signup.presenter.CompleteSignUpPresenter;
import com.androidtecknowlogy.tym.cast.cast.activity_view.CastActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AGBOMA franklyn on 6/17/17.
 */

public class GoogleSignInActivity extends AppCompatActivity {

    private final String LOG_TAG = GoogleSignInActivity.class.getSimpleName();

    private GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth authFirebase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private final int RC_SIGN_IN = 1001;
    @BindView(R.id.sign_in) SignInButton signInButton;
    @BindView(R.id.sign_in_layout) FrameLayout signInLayout;
    @BindView(R.id.sign_up_layout) FrameLayout signUpLayout;
    private CompleteSignUpFragment completeSignUpFragment;
    private ProgressDialog loading;

    public Google googleDetails;
    private SharedPreferences pref;
    private boolean getPrefBoolean;
    private int orientation;
    private  boolean isTab;

    public interface Google {
        void getSignInCredn(String uId, String photo, String name, String email);
    }

    //setup interface
    public void setGoogleDetails(Google googleDetails) {
        this.googleDetails = googleDetails;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientation = getResources().getConfiguration().screenWidthDp;
        Log.i(LOG_TAG, "onCreate " + orientation);

        /*if(getResources().getConfiguration().screenWidthDp < 600)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);*/
        /*isTab = getResources().getBoolean(R.bool.isTab);
        if(!isTab)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/

        //get default preference
        pref =  PreferenceManager.getDefaultSharedPreferences(this);
        getPrefBoolean = pref.getBoolean(Intent.EXTRA_TEXT, false);
        Log.i(LOG_TAG, "getBoolean " +getPrefBoolean);
        /**
         * getPrefBoolean = true --> start CastActivity
         * because user has signIn before
         */
        if(getPrefBoolean) {
            startNextActivity();
        }

        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);


        //set up Fragment and interfaces
        init();


        /**
         * creating sign in option --> user not recognize.
         */
        gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //client to access the google sign in api
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //
                        Log.e(LOG_TAG, " onConnnectionFailed");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //initializing setting authentication listener for fire base.
        authFirebase = FirebaseAuth.getInstance();
        //once signIn with credential is true, then come and get the user details
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.e(LOG_TAG, "onAuthStateChanged: SignIn: " +
                            "\nToken" + user.getIdToken(true)
                            + " \nUid" + user.getUid());
                    if(user.getDisplayName() != null) {
                        Log.e(LOG_TAG, "User image: " +user.getPhotoUrl());
                        Log.e(LOG_TAG, "User name: " +user.getDisplayName());
                        Log.e(LOG_TAG, "User email: " +user.getEmail());
                        String uId = user.getUid();
                        String photo = user.getPhotoUrl().toString();
                        String name = user.getDisplayName();
                        String email = user.getEmail();
                        //get user details to Complete signUp to get complete details from users.
                        stopLoading();
                        signInLayout.setVisibility(View.GONE);
                        signUpLayout.setVisibility(View.VISIBLE);
                        googleDetails.getSignInCredn(uId, photo, name, email);
                        //move this to final signUp
                        /*AppController.castsData.child(uId)
                                .setValue(new CastItems(photo, name,null,null,null,
                                        email,null,null,null));*/
                    }
                    else {
                        signUpLayout.setVisibility(View.GONE);
                        signInLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        authFirebase.addAuthStateListener(authStateListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void startLoading(String title, String message){
        if(null == loading){
            loading = new ProgressDialog(this);
            loading.setCancelable(false);
        }
        loading.setTitle(title);
        loading.setMessage(message);
        loading.show();
    }
    public void stopLoading(){
        if(loading.isShowing())
            loading.dismiss();
    }

    private void startNextActivity() {
        this.finish();
        startActivity(new Intent(GoogleSignInActivity.this, CastActivity.class));
    }
    @OnClick(R.id.sign_in)
    public void onSignInClicked() {
        Log.i(LOG_TAG, "sign in clicked");
        signIn();
    }

    @OnClick(R.id.sign_out)
    public void onSignOutClicked() {
        signOut();
    }

    private void init() {
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        completeSignUpFragment = (CompleteSignUpFragment) getSupportFragmentManager()
                .findFragmentById(R.id.complete_sign_up_fragment);

        //set up interface call for GoogleSignInActivity.
        setGoogleDetails(completeSignUpFragment);
        //set up interface call for CompleteSignUpPresenter
        CompleteSignUpPresenter completeSignUpPresenter = new CompleteSignUpPresenter();
        completeSignUpFragment.setCompleteSignUpToPresenter(completeSignUpPresenter);
        //set up interface call for PresenterSendSignUpToModel
        //set up interface call for presenterSendSignUPToCompleteSignUpFragment
        CompleteSignUpModel model = new CompleteSignUpModel();
        completeSignUpPresenter.setPresenterSendSignUpToModel(model, model,completeSignUpFragment);
        //set up interface call for presenterSendSignUPToCompleteSignUpFragment
        /*completeSignUpPresenter.setPresenterSendsSignUpCompletedToCompleteSignUpFragment
                (completeSignUpFragment);*/

    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }
    private void signOut() {
        //call fire base authentication sign out
        authFirebase.signOut();
        //if the above does not work properly use = FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.e(LOG_TAG, "user signOut");
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null)
            authFirebase.removeAuthStateListener(authStateListener);
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.e(LOG_TAG, "Account id: " + account.getId());
        startLoading("TymFrontiers signIn", "loading Gmail data...");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authFirebase.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if sign in fails, display a message to tell so else,
                        //notify the authStateListener to get credentials.
                        if(!task.isSuccessful()) {
                            Log.e(LOG_TAG, task.getException().toString());
                            Log.e(LOG_TAG, "Authentication fails");
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check requestCode is okay
        if(requestCode == RC_SIGN_IN) {
            //get the result of email clicked from intent and send to google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()) {
                //get the account details and send to firebase
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            }
            else {
                //perform UI expected.
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
        stopLoading();

    }
}

