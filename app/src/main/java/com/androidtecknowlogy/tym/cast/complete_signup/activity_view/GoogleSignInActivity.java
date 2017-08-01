package com.androidtecknowlogy.tym.cast.complete_signup.activity_view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.helper.io.ConnectionReceiver;
import com.androidtecknowlogy.tym.cast.login.LoginActivity;
import com.androidtecknowlogy.tym.cast.complete_signup.fragment.CompleteSignUpFragment;
import com.androidtecknowlogy.tym.cast.complete_signup.model.CompleteSignUpModel;
import com.androidtecknowlogy.tym.cast.complete_signup.presenter.CompleteSignUpPresenter;
import com.androidtecknowlogy.tym.cast.cast.activity_view.CastActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

public class GoogleSignInActivity extends AppCompatActivity{

    private final String LOG_TAG = GoogleSignInActivity.class.getSimpleName();

    private GoogleApiClient googleApiClient;
    private FirebaseAuth authFirebase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private final String COUSANT = "COUSANT";
    private final int RC_SIGN_IN = 1001;
    @BindView(R.id.sign_in) SignInButton signInButton;
    @BindView(R.id.circular_progress_bar) ProgressBar progressBar;
    @BindView(R.id.guess_button) AppCompatButton guessButton;
    @BindView(R.id.sign_in_layout) FrameLayout signInLayout;
    @BindView(R.id.sign_up_layout) FrameLayout signUpLayout;
    private CompleteSignUpFragment completeSignUpFragment;
    private ProgressDialog loading;

    public Google googleDetails;
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;
    private boolean getPrefBooleanSignUp;
    private boolean getPrefBooleanLogin;
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

        //get default preference
        pref =  PreferenceManager.getDefaultSharedPreferences(this);
        getPrefBooleanSignUp = pref.getBoolean(Intent.EXTRA_TEXT, false);
        getPrefBooleanLogin = pref.getBoolean("login", false);
        Log.i(LOG_TAG, "getBooleanSignUp " + getPrefBooleanSignUp
                + " getBooleanLogin " + getPrefBooleanLogin);

        /**
         * if getPrefBooleanSignUp = true and getPrefBooleanLogin = true --> start CastActivity
         * because user has signIn and login before.
         */
        if(getPrefBooleanSignUp && getPrefBooleanLogin)
            startNextActivity("castActivity");
        /**
         * if getPrefBooleanSignUp = true but getPrefBooleanLogin = false --> start loginActivity
         * user only signIn but not login.
         */
        if(getPrefBooleanSignUp && !getPrefBooleanLogin)
            startNextActivity("loginActivity");



        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        guessButton.setTypeface(AppController.getDroidFace(this));
        changeSignInText(signInButton);


        //client to access the google sign in api
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(LOG_TAG, " onConnnectionFailed");
                        Toast.makeText(getApplicationContext(), "Fails, Check internet connection.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, AppController.getInstance().googleSignInOptions())
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
                    stopLoading();
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
        //set up Fragment and interfaces
        init();
    }

    protected void changeSignInText(SignInButton signInButton) {

        for(int count = 0; count < signInButton.getChildCount(); count ++) {
            View signInView = signInButton.getChildAt(count);

            if(signInView instanceof TextView) {
                TextView text = (TextView) signInView;
                text.setText(COUSANT);
                text.setTextColor(getResources().getColor(android.R.color.black));
                if(orientation < 600)
                    text.setTextSize(13f);
                if(orientation >= 600 && orientation <680)
                    text.setTextSize(15f);
                else if(orientation >= 680)
                    text.setTextSize(17f);
                text.setPadding(4,4,4,4);
                text.setTypeface(AppController.getDroidFace(this));
                return;
            }
        }
    }

    private void setProgressBar(boolean mes){
        //set progressBar and indeterminate.
        if(!mes){
            progressBar.setVisibility(View.GONE);
            progressBar.setIndeterminate(false);
            signInButton.setVisibility(View.VISIBLE);
        }
        else {
            signInButton.setVisibility(View.GONE);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
        }
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
        if(loading != null && loading.isShowing())
            loading.dismiss();
    }

    private void startNextActivity(String activity) {
        this.finish();
        startActivity(new Intent(GoogleSignInActivity.this, activity.equals("castActivity")
                ?CastActivity.class : LoginActivity.class));
    }
    @OnClick(R.id.sign_in)
    public void onSignInClicked() {
        Log.i(LOG_TAG, "sign in clicked");
        if(!ConnectionReceiver.isConnected()) {
            setProgressBar(false);
            snackMsg("Check internet connection","wifi","long");
        }
        else {
            setProgressBar(true);
            signIn();
        }
    }

    @OnClick(R.id.guess_button)
    public void onGuessClicked () {
        AppController.isGuest = true;
        if(!ConnectionReceiver.isConnected())
            snackMsg("Check internet connection","wifi","long");
        else
            startNextActivity("castActivity");
    }

    private void init() {
        completeSignUpFragment = (CompleteSignUpFragment) getSupportFragmentManager()
                .findFragmentById(R.id.complete_sign_up_fragment);

        //set up interface call for GoogleSignInActivity.
        setGoogleDetails(completeSignUpFragment);
        //set up interface call for CompleteSignUpPresenter
        //CompleteSignUpPresenter completeSignUpPresenter = new CompleteSignUpPresenter();
        //completeSignUpFragment.setCompleteSignUpToPresenter(completeSignUpPresenter);
        //set up interface call for PresenterSendSignUpToModel
        //set up interface call for presenterSendSignUPToCompleteSignUpFragment
        //CompleteSignUpModel model = new CompleteSignUpModel();
        //completeSignUpPresenter.setPresenterSendSignUpToModel(model, model,completeSignUpFragment);
        //set up interface call for presenterSendSignUPToCompleteSignUpFragment
        /*completeSignUpPresenter.setPresenterSendsSignUpCompletedToCompleteSignUpFragment
                (completeSignUpFragment);*/

    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
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

    private void restoreSignInProcess() {
        //call fire base authentication sign out
        authFirebase.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //check is the status is not successful,the recall method.
                        if(!status.isSuccess())
                            restoreSignInProcess();
                    }
                });
    }
    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.e(LOG_TAG, "Account id: " + account.getId());

        //sign in for testing.
        startLoading("Cousant signIn", "loading Gmail data...");
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
                            toastMsg("Authentication fails");
                        }
                    }
                });
        /*//check if email account is cousant's mail.
        if(!account.getEmail().contains("@cousant.com")) {
            restoreSignInProcess();
            snackMsg("Use Cousant mail.","info", "long");
        }
        else {
            startLoading("Cousant signIn", "loading Gmail data...");
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
                                toastMsg("Authentication fails");
                            }
                        }
                    });
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check requestCode is okay
        if(requestCode == RC_SIGN_IN) {
            //get the result of email clicked from intent and send to google
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()) {
                setProgressBar(false);
                //get the account details and send to firebase
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            }
            else {
                setProgressBar(false);
                snackMsg("Check internet connection","wifi","long");
            }
        }
        else {
            setProgressBar(false);
            toastMsg("Sign in with Cousant mail.");
        }
    }

    private void toastMsg (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void snackMsg (String msg, String iconType,String length) {
        Snackbar snackbar = Snackbar.make(signInButton.getRootView(), "",
                length.equals("long")?Snackbar.LENGTH_LONG :Snackbar.LENGTH_SHORT);
        //get snack bar view and customize with snackbar_layout.
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        //hide snack bar fix text view layout
        ((TextView) layout.findViewById(android.support.design.R.id.snackbar_text))
                .setVisibility(View.INVISIBLE);
        //attack view
        View snackView = getLayoutInflater().inflate(R.layout.snackbar_layout, null, false);
        TextView textView = (TextView) snackView.findViewById(R.id.snack_text);
        textView.setTypeface(AppController.getProximaFace(this));
        ImageView imageView = (ImageView) snackView.findViewById(R.id.snack_image);
        TextView settingBtn = (TextView) snackView.findViewById(R.id.snack_btn);
        settingBtn.setTypeface(AppController.getDroidFace(this));
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
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
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

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
        stopLoading();

        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();

    }
}

