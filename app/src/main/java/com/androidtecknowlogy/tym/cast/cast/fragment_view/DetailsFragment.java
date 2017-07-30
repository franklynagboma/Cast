package com.androidtecknowlogy.tym.cast.cast.fragment_view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.app.AppController;
import com.androidtecknowlogy.tym.cast.cast.activity_view.CastActivity;
import com.androidtecknowlogy.tym.cast.helper.pojo.Settings;
import com.androidtecknowlogy.tym.cast.interfaces.Constant;
import com.androidtecknowlogy.tym.cast.helper.view.CircularTransform;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AGBOMA franklyn on 6/26/17.
 */

public class DetailsFragment extends Fragment implements Constant.PresenterSendToCastDetailsFragment,
        CastActivity.CastUserToDetailFragment{

    private final String LOG_TAG = DetailsFragment.class.getSimpleName();

    private Context context;
    private boolean user;
    private String getShowDob, getDevice;
    private String getPhoto = "" , getEmail = "", getGender = "",
            getMonth_year = "", getName = "", getMobile = "", getTitle = "";

    @BindView(R.id.image_holder)
    FrameLayout imageHolder;
    @BindView(R.id.cast_image)
    ImageView castImage;
    @BindView(R.id.cast_name)
    TextView castName;
    @BindView(R.id.cast_title)
    TextView castTitle;
    @BindView(R.id.cast_gender)
    TextView castGender;
    @BindView(R.id.cast_dob)
    TextView castDob;

    @BindView(R.id.mobile_details)
    TextView mobileText;
    @BindView(R.id.mobile_number)
    TextView castMobile;
    @BindView(R.id.phone_call)
    ImageView phoneCall;
    @BindView(R.id.sms)
    ImageView sms;

    @BindView(R.id.email)
    TextView emailText;
    @BindView(R.id.cast_email)
    TextView castEmail;
    @BindView(R.id.mail_message)
    ImageView mail;

    @BindView(R.id.summary)
    TextView summaryText;
    @BindView(R.id.cast_summary)
    TextView castSummary;

    public DetailsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(LOG_TAG, "onViewCreated");
        //onViewCreated on works for just an instance of a Fragment.
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void CastSendUsersToDetailFragment(String image, String name, String title, String dob,
                                              String gender, String mobile,
                                              String email, String summary) {

        user = true;
        showDetails(0, image, email, gender, "", name, mobile, title, dob, summary);
    }

    @Override
    public void details(final int itemPosition, final String photo, final String email,
                        final String gender, String month_year, final String name,
                        final String mobile, final String title, final String dob,
                        final String summary) {

        user = false;
        Log.i(LOG_TAG, "Details \n" + photo +"\n"+ email +"\n"+ gender
                +"\n"+ month_year +"\n"+ name +"\n"+ mobile +"\n"+ title);
        showDetails(itemPosition, photo,email,gender,month_year,name,mobile,title,dob,summary);

        //Important notice, at the point of overriding an abstract interface method,
        //the view has not be created yet reasons for sending the details to onViewCreated.
        /*getPhoto = photo;
        getEmail = email;
        getGender = gender;
        getMonth_year = month_year;
        getName = name;
        getMobile = mobile;
        getTitle = title;*/
    }

    private void showDetails(final int itemPosition, final String photo, final String email,
                             final String gender, String month_year, final String name,
                             final String mobile, final String title, final String dob,
                             final String summary) {

        String[] getEmailOffDot = email.split("\\.");
        //check if user has changed the settings on their devices
        if (AppController.settingMap.containsKey(getEmailOffDot[0]))
            getSettingsChangedVariables(getEmailOffDot[0]);

        //delay to show details
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context = getActivity();
                if(!photo.isEmpty()) {
                    //for Tab to not through null because this Fragment starts as the Activity does
                    //base on MVC pathern.
                    imageHolder.setBackgroundResource(itemPosition != 0
                            ? itemPosition : R.drawable.circular_frame_yellow);
                    Picasso.with(context).load(photo)
                            .transform(new CircularTransform())
                            .placeholder(R.mipmap.ic_cast_person)
                            .error(R.mipmap.ic_cast_person)
                            .into(castImage);
                    castName.setText(name);
                    castTitle.setText(title);
                    castGender.setText(gender);

                    //if this fragment was access by the device owner
                    if(user) {
                        castDob.setText(getDevice.equals(AppController.currentDevice)
                                ? getResources().getString(R.string.dob,dob)
                                : (getShowDob.equals(getString(R.string.every_one))
                                || getShowDob.equals(getString(R.string.current_user))
                                ?getResources().getString(R.string.dob,dob) :""));
                    }
                    else {
                        //if it was access by other users
                        castDob.setText(getShowDob.equals(getString(R.string.every_one))
                                ? getResources().getString(R.string.dob,dob) : "");
                    }

                    castMobile.setText(mobile);
                    castEmail.setText(email);
                    castSummary.setText(summary);
                    user = false;
                }
            }
        },100);
    }

    @OnClick
    public void onPhoneCallClicked(){}

    @OnClick(R.id.sms)
    public void onSmsClicked(){}

    @OnClick(R.id.mail_message)
    public void onMailClicked(){}


    private void getSettingsChangedVariables(String emailNoDot) {
        Settings getSettings = AppController.settingMap.get(emailNoDot);
        getShowDob = getSettings.getShowDob();
        getDevice = getSettings.getDevice();
    }
}
