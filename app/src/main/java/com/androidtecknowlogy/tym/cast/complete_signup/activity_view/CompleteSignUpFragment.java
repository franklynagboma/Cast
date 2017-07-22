package com.androidtecknowlogy.tym.cast.complete_signup.activity_view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.cast.activity_view.CastActivity;
import com.androidtecknowlogy.tym.cast.faces.Constant;
import com.androidtecknowlogy.tym.cast.helper.view.CircularTransform;
import com.androidtecknowlogy.tym.cast.helper.io.CustomTextWatcher;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AGBOMA franklyn on 6/26/17.
 */

public class CompleteSignUpFragment extends Fragment implements GoogleSignInActivity.Google,
        Constant.PresenterSendsSignUpCompletedToCompleteSignUpFragment{

    private final String LOG_TAG = CompleteSignUpFragment.class.getSimpleName();

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEdit;
    private View view;
    private Context context;
    private String uId, photo, email;
    private int rbId = -1;
    public final String SUN_NAME_ID = "sun_name";
    public final String OTHER_NAME_ID = "other_name";
    public final String MOBILE_ID = "mobile";
    public final String TITLE_ID = "title";
    public final String PASSWORD_ID = "password";
    public final String CONFIRM_ID = "confirm";
    public final String RADIO_ID = "radio";

    //get views
    @BindView(R.id.complete_sign_up)
    TextView completeText;

    @BindView(R.id.users_sun_name)
    EditText userSunName;
    @BindView(R.id.input_last_name)
    TextInputLayout inputSunName;

    @BindView(R.id.users_others_name)
    EditText userOtherNames;
    @BindView(R.id.input_first_name)
    TextInputLayout inputOtherNames;

    @BindView(R.id.users_email)
    EditText userEmail;

    @BindView(R.id.users_mobile)
    EditText userMobile;
    @BindView(R.id.input_mobile)
    TextInputLayout inputMobile;

    @BindView(R.id.users_title)
    EditText userTitle;
    @BindView(R.id.input_title)
    TextInputLayout inputTitle;

    @BindView(R.id.users_event_password)
    EditText userEventPassword;
    @BindView(R.id.input_password)
    TextInputLayout inputPassword;

    @BindView(R.id.users_event_password_confirm)
    EditText userEventPasswordConfirm;
    @BindView(R.id.input_confirm)
    TextInputLayout inputConfirm;

    RadioGroup rg;
    @BindView(R.id.rb_male)
    AppCompatRadioButton rbMale;
    @BindView(R.id.rb_female)
    AppCompatRadioButton rbFemale;
    @BindView(R.id.send_complete_sign_up)
    Button sendButton;
    @BindView(R.id.cast_image)
    ImageView castImage;


    private Constant.CompleteSignUpToPresenter completeSignUpToPresenter;

    public void setCompleteSignUpToPresenter(Constant.CompleteSignUpToPresenter
                                                     completeSignUpToPresenter) {
        this.completeSignUpToPresenter = completeSignUpToPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        //get default preference
        pref =  PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup_complete, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            userMobile.setText(savedInstanceState.getString(MOBILE_ID));
            userTitle.setText(savedInstanceState.getString(TITLE_ID));
            userEventPassword.setText(savedInstanceState.getString(PASSWORD_ID));
            userEventPasswordConfirm.setText(savedInstanceState.getString(CONFIRM_ID));
            int getRadio = savedInstanceState.getInt(RADIO_ID);
            if(getRadio > -1) {
                if(getRadio == rbMale.getId())
                    rbMale.setChecked(true);
                else
                    rbFemale.setChecked(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MOBILE_ID, userMobile.getText().toString());
        outState.putString(TITLE_ID, userTitle.getText().toString());
        outState.putString(PASSWORD_ID, userEventPassword.getText().toString());
        outState.putString(CONFIRM_ID, userEventPasswordConfirm.getText().toString());
        outState.putInt(RADIO_ID, rbId);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    private void init() {

        completeText.setPaintFlags(completeText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        rg = (RadioGroup) view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                rbId = rg.getCheckedRadioButtonId();
                Log.i(LOG_TAG, "rbId " + rbId);
            }
        });
        //userMobile = (EditText) view.findViewById(R.id.users_mobile);
        //users are not allowed to edit their emails got form google.
        userEmail.setEnabled(false);
        userSunName.addTextChangedListener(new CustomTextWatcher(inputSunName, SUN_NAME_ID));
        userOtherNames.addTextChangedListener(new CustomTextWatcher(inputOtherNames, OTHER_NAME_ID));
        userMobile.addTextChangedListener(new CustomTextWatcher(inputMobile, MOBILE_ID));
        userTitle.addTextChangedListener(new CustomTextWatcher(inputTitle, TITLE_ID));
        userEventPassword.addTextChangedListener(new CustomTextWatcher
                (inputPassword, PASSWORD_ID));
        userEventPasswordConfirm.addTextChangedListener(new CustomTextWatcher
                (inputConfirm, CONFIRM_ID));

    }


    @OnClick(R.id.send_complete_sign_up)
    public void onSendButtonClicked() {

        //get radio button id string
        String gender = "";
        if(rbId == rbMale.getId())
            gender = rbMale.getText().toString().trim();
        else if(rbId == rbFemale.getId())
            gender = rbFemale.getText().toString().trim();
        else
            gender = "Male";
        //set field to presenter
        completeSignUpToPresenter.userSignUp(uId, photo, email, gender,
                userMobile,userSunName, userOtherNames,
                userTitle, userEventPassword, userEventPasswordConfirm, context);
    }

    @Override
    public void getSignInCredn(String uId, String photo, String name, String email) {
        //get credentials from GoogleSignInActivity
        this.uId = uId;
        this.photo = photo;
        this.email = email;
        Picasso.with(context).load(photo).transform(new CircularTransform())
                .placeholder(R.mipmap.ic_cast_person).error(R.mipmap.ic_cast_person)
                .into(castImage);
        //set names
        name = name.trim();
        String[] getAllNames = name.split(" ");
        //set the last name
        if(getAllNames.length == 1)
            userSunName.setText(getAllNames[0]);
        //set both last and first name.
        if(getAllNames.length > 1) {
            //set last name
            userSunName.setText(getAllNames[0]);
            name = "";//set name to empty.
            for(int count =1; count <getAllNames.length; count ++)
                name = name +" "+ getAllNames[count] + " ";//use name variable to get other names.
            //set first name
            userOtherNames.setText(name);
        }
        //set emails
        userEmail.setText(email);

    }

    @Override
    public void startActivity(String message) {
        String[] getMessage = message.split("/");
        int getLength = getMessage.length;
        Log.i(LOG_TAG, "getMessage on startActivity " + getLength);
        //check if it was error message
        if(getLength >1) {
            Log.i(LOG_TAG, "getMessage " + getMessage[0]);
            if(getMessage[0].equals("password")) {
                Toast.makeText(context, getMessage[1], Toast.LENGTH_SHORT).show();
            }
            else if(getMessage[0].equals("empty")) {
                Toast.makeText(context, getMessage[1], Toast.LENGTH_SHORT).show();
            }
        }
        else if(getLength == 1) {

            //start next Activity
            if(message.equals("error"))
                Toast.makeText(context, "network error try again", Toast.LENGTH_SHORT).show();
            else {
                getActivity().finish();
                //save preference.
                prefEdit = pref.edit();
                prefEdit.putBoolean(Intent.EXTRA_TEXT, true);
                prefEdit.putString("name", message);
                prefEdit.putString("image", photo);
                prefEdit.apply();
                startActivity(new Intent(getActivity(), CastActivity.class));
            }
        }

    }
}
