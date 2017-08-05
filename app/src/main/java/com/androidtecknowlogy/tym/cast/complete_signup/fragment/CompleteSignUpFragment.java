package com.androidtecknowlogy.tym.cast.complete_signup.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.cast.activity_view.CastActivity;
import com.androidtecknowlogy.tym.cast.complete_signup.activity_view.GoogleSignInActivity;
import com.androidtecknowlogy.tym.cast.complete_signup.model.CompleteSignUpModel;
import com.androidtecknowlogy.tym.cast.complete_signup.presenter.CompleteSignUpPresenter;
import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.helper.io.CustomCalendar;
import com.androidtecknowlogy.tym.cast.helper.io.CustomDataFormat;
import com.androidtecknowlogy.tym.cast.helper.view.CircularTransform;
import com.androidtecknowlogy.tym.cast.helper.io.CustomTextWatcher;
import com.squareup.picasso.Picasso;

import java.util.Date;

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
    public final String DOB_ID = "dob";
    public final String SUMMARY_ID = "summary";
    private String getDateFromView = "";
    private String[] monthReadable = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    //get views
    @BindView(R.id.complete)
    RelativeLayout layoutComplete;

    @BindView(R.id.cast_image)
    ImageView castImage;

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

    @BindView(R.id.img_calendar)
    ImageView imgCalendar;
    @BindView(R.id.cast_dob)
    EditText userDob;

    @BindView(R.id.input_summary)
    TextInputLayout inputSummary;
    @BindView(R.id.cast_summary)
    EditText userSummary;

    RadioGroup rg;
    @BindView(R.id.rb_male)
    AppCompatRadioButton rbMale;
    @BindView(R.id.rb_female)
    AppCompatRadioButton rbFemale;

    @BindView(R.id.send_complete_sign_up)
    Button sendButton;

    @BindView(R.id.view_edit)
    View viewEdit;


    private Constant.CompleteSignUpToPresenter completeSignUpToPresenter;
    public KnowActivity knowActivity;

    public void setCompleteSignUpToPresenter(Constant.CompleteSignUpToPresenter
                                                     completeSignUpToPresenter) {
        this.completeSignUpToPresenter = completeSignUpToPresenter;
    }

    public interface KnowActivity {
        void activity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity().getClass().getSimpleName().equals("CastActivity")) {
            try {
                knowActivity = (KnowActivity) context;
            }
            catch (ClassCastException w) {
                throw new ClassCastException(context.toString());
            }
        }
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
            userSunName.setText(savedInstanceState.getString(SUN_NAME_ID));
            userOtherNames.setText(savedInstanceState.getString(OTHER_NAME_ID));
            userMobile.setText(savedInstanceState.getString(MOBILE_ID));
            userTitle.setText(savedInstanceState.getString(TITLE_ID));
            userEventPassword.setText(savedInstanceState.getString(PASSWORD_ID));
            userEventPasswordConfirm.setText(savedInstanceState.getString(CONFIRM_ID));
            userDob.setText(savedInstanceState.getString(DOB_ID));
            userSummary.setText(savedInstanceState.getString(SUMMARY_ID));
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
        outState.putString(SUN_NAME_ID, userSunName.getText().toString());
        outState.putString(OTHER_NAME_ID, userOtherNames.getText().toString());
        outState.putString(MOBILE_ID, userMobile.getText().toString());
        outState.putString(TITLE_ID, userTitle.getText().toString());
        outState.putString(PASSWORD_ID, userEventPassword.getText().toString());
        outState.putString(CONFIRM_ID, userEventPasswordConfirm.getText().toString());
        outState.putString(DOB_ID, userDob.getText().toString());
        outState.putString(SUMMARY_ID, userSummary.getText().toString());
        outState.putInt(RADIO_ID, rbId);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    private void init() {

        CompleteSignUpPresenter completeSignUpPresenter = new CompleteSignUpPresenter();
        setCompleteSignUpToPresenter(completeSignUpPresenter);
        //set up interface call for PresenterSendSignUpToModel
        //set up interface call for presenterSendSignUPToCompleteSignUpFragment
        CompleteSignUpModel model = new CompleteSignUpModel();
        completeSignUpPresenter.setPresenterSendSignUpToModel(model, model,this);

        completeText.setPaintFlags(completeText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //set text type face
        completeText.setTypeface(AppController.getDroidFace(context));
        userSunName.setTypeface(AppController.getProximaFace(context));
        userOtherNames.setTypeface(AppController.getProximaFace(context));
        userMobile.setTypeface(AppController.getProximaFace(context));
        userTitle.setTypeface(AppController.getProximaFace(context));
        userEventPassword.setTypeface(AppController.getProximaFace(context));
        userEventPasswordConfirm.setTypeface(AppController.getProximaFace(context));
        userDob.setTypeface(AppController.getProximaFace(context));
        userSummary.setTypeface(AppController.getProximaFace(context));
        sendButton.setTypeface(AppController.getDroidFace(context));

        rg = (RadioGroup) view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                rbId = rg.getCheckedRadioButtonId();
                Log.i(LOG_TAG, "rbId " + rbId);
            }
        });

        //users are not allowed to edit their emails got form google.
        userEmail.setEnabled(false);
        userDob.setEnabled(false);
        isNotEmpty();
        userSunName.addTextChangedListener(new CustomTextWatcher(inputSunName, SUN_NAME_ID));
        userOtherNames.addTextChangedListener(new CustomTextWatcher(inputOtherNames, OTHER_NAME_ID));
        userMobile.addTextChangedListener(new CustomTextWatcher(inputMobile, MOBILE_ID));
        userTitle.addTextChangedListener(new CustomTextWatcher(inputTitle, TITLE_ID));
        userEventPassword.addTextChangedListener(new CustomTextWatcher
                (inputPassword, PASSWORD_ID));
        userEventPasswordConfirm.addTextChangedListener(new CustomTextWatcher
                (inputConfirm, CONFIRM_ID));
        userDob.addTextChangedListener(new CustomTextWatcher(userDob,DOB_ID));
        userSummary.addTextChangedListener(new CustomTextWatcher(inputSummary, SUMMARY_ID));

        //get Arguments
        Bundle arg = getArguments();
        if(null != arg) {
            layoutComplete.setVisibility(View.GONE);//hind image view line.
            viewEdit.setVisibility(View.VISIBLE);//show space view.
            getSignInCredn(arg.getString("password"), arg.getString("uid"),
                    arg.getString("image"), arg.getString("name"), arg.getString("email"));
        }

    }

    private void isNotEmpty() {
        //check if it is empty, set invisible is true else set visible.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //delay a little to add animation effect on visibility.
                userDob.setVisibility(userDob.getText().toString().isEmpty()
                        ?View.INVISIBLE :View.VISIBLE);
            }
        }, 500);
    }

    @OnClick(R.id.img_calendar)
    public void onImgCalendarCliced() {
        //getSystemService on inflater.
        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //get resource views.
        View calendarView = layoutInflater.inflate(R.layout.calendar_layout, null, false);

        CustomCalendar calendar = (CustomCalendar)
                calendarView.findViewById(R.id.calender_picker);
        //set initial data format
        getDateFromView = new CustomDataFormat().getDateFormat(new Date(calendar.getDate()),
                "m/d/y");
        //get data format form users if changed.
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,
                                            int year, int month, int dayOfMonth) {
                //get month as string
                getDateFromView = monthReadable[month] +" " +dayOfMonth +", " + year;
            }
        });

        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context);
        dialog.customView(calendarView, true);

        dialog.positiveText("Set DOB")
                .positiveColor(getResources().getColor(R.color.colorAccent))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        userDob.setText(getDateFromView);
                        dialog.dismiss();
                        isNotEmpty();
                    }
                });
        dialog.negativeText("Discard")
                .negativeColor(getResources().getColor(R.color.colorAccent))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        //show material dialog view to get user details to add.
        MaterialDialog builder = dialog.build();
        builder.show();
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
                userMobile,userSunName, userOtherNames, userTitle,
                userEventPassword, userEventPasswordConfirm, userDob, userSummary, context);
    }

    /**
     * This method overloads getSignInCredn from GoogleSignInActivity interface
     * to view arg from CastActivity which was got from preference storage
     * @param password
     * @param uId
     * @param photo
     * @param name
     * @param email
     */
    private void getSignInCredn(String password, String uId,
                                String photo, String name, String email) {
        //set and hind password text field
        //so users cannot edit it on this page.
        userEventPassword.setText(password);
        userEventPasswordConfirm.setText(password);
        inputPassword.setVisibility(View.GONE);
        inputConfirm.setVisibility(View.GONE);

        performViewPresentation(uId, photo, name, email);
    }

    /**
     * This method override GoogleSignInActivity interface getSignInCredn
     * to view arg from Online storage.
     * @param uId
     * @param photo
     * @param name
     * @param email
     */
    @Override
    public void getSignInCredn(String uId, String photo, String name, String email) {
        //set and show password text field if not showing.
        userEventPassword.setText("");
        userEventPasswordConfirm.setText("");
        inputPassword.setVisibility(View.VISIBLE);
        inputConfirm.setVisibility(View.VISIBLE);

        performViewPresentation(uId, photo, name, email);
    }

    private void performViewPresentation(String uId, String photo, String name, String email) {

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
            Toast.makeText(context, getMessage[1], Toast.LENGTH_SHORT).show();
        }
        else if(getLength == 1) {
            if(message.equals("error"))
                Toast.makeText(context, "network error try again", Toast.LENGTH_SHORT).show();
            else {
                //split "." out of message
                getMessage = message.split("\\?");
                //save preference.
                prefEdit = pref.edit();
                prefEdit.putBoolean(Intent.EXTRA_TEXT, true);
                prefEdit.putString("uid", uId);
                prefEdit.putString("image", photo);
                prefEdit.putString("email", email);
                prefEdit.putBoolean("login", true);
                prefEdit.putString("name", getMessage[0]);
                prefEdit.putString("password", getMessage[1]);
                prefEdit.putString("gender", getMessage[2]);
                prefEdit.putString("dob", getMessage[3]);
                prefEdit.putString("mobile", getMessage[4]);
                prefEdit.putString("title", getMessage[5]);
                prefEdit.putString("summary", getMessage[6]);
                prefEdit.apply();
                /*if(getActivity().getClass().getSimpleName().equals("CastActivity"))
                    knowActivity.activity();*/
                if(null != getArguments())
                    knowActivity.activity();
                else {
                    getActivity().finish();
                    //start next Activity
                    startActivity(new Intent(getActivity(), CastActivity.class));
                }
            }
        }

    }
}
