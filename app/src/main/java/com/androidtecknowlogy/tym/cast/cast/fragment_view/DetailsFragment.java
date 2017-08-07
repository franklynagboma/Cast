package com.androidtecknowlogy.tym.cast.cast.fragment_view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private String getLast10DigitWithCountryCode, toEmail, toNumber;
    private String getShowDob, getDevice;
    private final String CAST_SUBJECT = "#CAST";
    private final int PERMISSION_CODE = 100;
    private boolean permissionGranted;
    private String getPhoto = "" , getEmail = "", getGender = "",
            getMonth_year = "", getName = "", getMobile = "", getTitle = "";

    @BindView(R.id.cousant_profile)
    LinearLayout cousantProfile;
    @BindView(R.id.cast_profile)
    LinearLayout castProfile;
    @BindView(R.id.about_cousant)
    TextView aboutCousant;

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

    @BindView(R.id.mobile_number)
    TextView castMobile;
    @BindView(R.id.phone_call)
    ImageView phoneCall;
    @BindView(R.id.sms)
    ImageView sms;

    @BindView(R.id.cast_email)
    TextView castEmail;
    @BindView(R.id.mail_message)
    ImageView mail;

    @BindView(R.id.cast_summary)
    TextView castSummary;

    //info text view
    @BindView(R.id.summary)
    TextView summaryText;
    @BindView(R.id.mobile_details)
    TextView mobileDetails;
    @BindView(R.id.email_detail)
    TextView emailDetails;
    @BindView(R.id.cousant)
    TextView cousantDetails;

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
        //is use is guess show his profile else show Cousant profile
        if(!AppController.isGuest)
            showDetails(0, image, email, gender, "", name, mobile, title, dob, summary);
        else
            showCousantProfile(summary);
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
        String getAll = "";
        /**
         * Note, some emails may contain more then one dot at the ending
         * eg: name.name@gmail.com
         * Therefore check for this condition with the length of getEmailOffDot.
         */
        //check if user has changed the settings on their devices
        if(getEmailOffDot.length > 1){
            //get all without the last one.
            for(int count =0; count <(getEmailOffDot.length -1); count ++)
                getAll = getAll + getEmailOffDot[count];

            if (AppController.settingMap.containsKey(getAll))
                getSettingsChangedVariables(getAll);
        }
        else {
            if (AppController.settingMap.containsKey(getEmailOffDot[0]))
                getSettingsChangedVariables(getEmailOffDot[0]);
        }

        //delay to show details
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                context = getActivity();
                if(!photo.isEmpty()) {

                    //set visibility back if ever it was changed
                    cousantProfile.setVisibility(View.INVISIBLE);
                    castProfile.setVisibility(View.VISIBLE);
                    toEmail = email;
                    toNumber = mobile;

                    mobileDetails.setTypeface(AppController.getDroidFace(context));
                    emailDetails.setTypeface(AppController.getDroidFace(context));

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
                    castName.setTypeface(AppController.getDroidFace(context));
                    castTitle.setText(title);
                    castTitle.setTypeface(AppController.getProximaFace(context));
                    castGender.setText(gender);
                    castGender.setTypeface(AppController.getProximaFace(context));

                    //if this fragment was access by the device owner
                    if(user) {
                        castDob.setText(getDevice != null &&
                                getDevice.equals(AppController.currentDevice)
                                ? getResources().getString(R.string.dob,dob)
                                : (getShowDob != null
                                && (getShowDob.equals(getString(R.string.every_one))
                                || getShowDob.equals(getString(R.string.current_user)))
                                ?getResources().getString(R.string.dob,dob) :""));
                    }
                    else {
                        //if it was access by other users
                        castDob.setText(getShowDob != null
                                && getShowDob.equals(getString(R.string.every_one))
                                ? getResources().getString(R.string.dob,dob) : "");
                    }
                    castDob.setTypeface(AppController.getProximaFace(context));

                    //remove the condition here, make condition before send to detail fragment.
                    castMobile.setText(editUserMobile(mobile));
                    castMobile.setTypeface(AppController.getProximaFace(context));
                    castEmail.setText(email);
                    castEmail.setTypeface(AppController.getProximaFace(context));
                    castSummary.setText(summary);
                    castSummary.setTypeface(AppController.getDroidFace(context));

                    //perform text editing and design
                    performTextEditDesign(gender);
                }
            }
        },100);
    }

    @OnClick(R.id.phone_call)
    public void onPhoneCallClicked(){
        if(!user) {
            requestCallPermission();
            if(permissionGranted){
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + getLast10DigitWithCountryCode));
                if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    sendToast("calling...");
                    startActivity(intent);
                }
            }
        }
        else
            sendToast("call operation denied!");
    }

    @OnClick(R.id.sms)
    public void onSmsClicked(){
        if(!user) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + toNumber));
            intent.putExtra("sms_body", CAST_SUBJECT);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                sendToast("sms...");
                startActivity(intent);
            }
        }
        else
            sendToast("sms operation denied!");
    }

    /**
     * Send mail to other users
     */
    @OnClick(R.id.mail_message)
    public void onMailClicked(){
        //for other users only
        if(!user) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" +toEmail));
            intent.putExtra(Intent.EXTRA_SUBJECT, CAST_SUBJECT);
            if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                sendToast("mail...");
                startActivity(intent);
            }
        }
        else
            sendToast("mail operation denied!");
    }


    private void showCousantProfile(final String summary) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(null == context)
                    context = getActivity();
                //hind cast profile layout and show Cousant profile layout
                castProfile.setVisibility(View.INVISIBLE);
                cousantDetails.setTypeface(AppController.getDroidFace(context));
                aboutCousant.setText(summary != null && !summary.isEmpty() ?summary :"Cousant");
                aboutCousant.setTypeface(AppController.getDroidFace(context));
                cousantProfile.setVisibility(View.VISIBLE);
            }
        }, 100);

    }

    private String editUserMobile (String mobile) {
        /**
         * Nigeria mobile number are in total of 11
         * but to make calls considering here in Nigeria
         * and outside Nigeria, the country code must be
         * added to the last 10 digit of the mobile number
         * Therefore, get the last 10 digit
         */
        String getMobile = mobile.substring(1);// replace string chas with the last 10 digit
        Log.i(LOG_TAG, "Mobile with 10 digit " + getMobile);
        getLast10DigitWithCountryCode = "+234"+getMobile;//get country code from sign up.
        //perform separation operation with '-' after three numbers to display
        String firstThree = mobile.substring(1,4);//first 3
        String secondThree = mobile.substring(4,7);//second 3
        String thirdThree = mobile.substring(7);//last 4
        //get all with Nigeria country code and set EditText.
        getMobile = "+234 - " + firstThree +" - "+ secondThree +" - "+ thirdThree;
        return  getMobile;
    }
    private void getSettingsChangedVariables(String emailNoDot) {
        Settings getSettings = AppController.settingMap.get(emailNoDot);
        getShowDob = getSettings.getShowDob();
        getDevice = getSettings.getDevice();
    }

    private void sendToast (String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    private void performTextEditDesign (String getGender) {
        String summaryIntro = "Why is " + (getGender.equalsIgnoreCase("male") ?"he":"she")
                +" considered to be the best?";
        summaryText.setText(summaryIntro);
        summaryText.setTypeface(AppController.getDroidFace(getActivity()));
    }

    private void requestCallPermission() {
        //check if the permission was not granted
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            //show an explanation why this permission is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CALL_PHONE)){}
            else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
            }
        }
        else {
            //permission was earlier granted.
            permissionGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                //length of grantResults greater than 0 tells that the result for granted
                if(grantResults.length >0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
                    permissionGranted = true;
                else {
                    permissionGranted = false;
                    AppController.getInstance().toastMsg(getActivity(),"Call permission denied");
                }
            }
        }
    }
}
