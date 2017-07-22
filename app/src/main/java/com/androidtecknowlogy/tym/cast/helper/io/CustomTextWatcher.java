package com.androidtecknowlogy.tym.cast.helper.io;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.androidtecknowlogy.tym.cast.cast.fragment_view.EventFragment;
import com.androidtecknowlogy.tym.cast.complete_signup.activity_view.CompleteSignUpFragment;

/**
 * Created by AGBOMA franklyn on 6/27/17.
 */

public class CustomTextWatcher implements TextWatcher {

    private final String LOG_TAG = CustomTextWatcher.class.getSimpleName();

    private EditText editText;
    private TextInputLayout inputLayout;
    private String idType;
    private CompleteSignUpFragment completeSignUpFragment = new CompleteSignUpFragment();
    private EventFragment eventFragment = new EventFragment();

    public CustomTextWatcher() {
    }

    public CustomTextWatcher(EditText editText, String idType) {
        this.editText = editText;
        this.idType = idType;
    }

    public CustomTextWatcher(TextInputLayout inputLayout, String idType) {
        this.inputLayout = inputLayout;
        this.idType = idType;
    }

    public CustomTextWatcher(EditText editText, TextInputLayout inputLayout, String idType) {
        this.editText = editText;
        this.inputLayout = inputLayout;
        this.idType = idType;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.i(LOG_TAG, "onTextChanged");

        //Text watcher for completeSignUpFragment.
        String chars = "";
        if(idType.equals(completeSignUpFragment.SUN_NAME_ID)){
            chars = charSequence.toString();
            if(null != inputLayout) {
                if(chars.isEmpty())
                    inputLayout.setError("input last name");
                else
                    inputLayout.setError("");
            }
        }

        if(idType.equals(completeSignUpFragment.OTHER_NAME_ID)){
            chars = charSequence.toString();
            if(null != inputLayout) {
                if(chars.isEmpty())
                    inputLayout.setError("input first name");
                else
                    inputLayout.setError("");
            }
        }

        Log.i(LOG_TAG, "idType " + idType);
        if(idType.equals(completeSignUpFragment.PASSWORD_ID)){
            chars = charSequence.toString();
            if(null != inputLayout) {
                if(chars.isEmpty())
                    inputLayout.setError("input password");
                else
                    inputLayout.setError("");
            }
        }
        if(idType.equals(completeSignUpFragment.CONFIRM_ID)){
            chars = charSequence.toString();
            if(null != inputLayout) {
                if(chars.isEmpty())
                    inputLayout.setError("input password");
                else
                    inputLayout.setError("");
            }
        }

        if(idType.equals(completeSignUpFragment.TITLE_ID)){
            chars = charSequence.toString();
            if(null != inputLayout) {
                if(chars.isEmpty())
                    inputLayout.setError("Input job title");
                else
                    inputLayout.setError("");
            }
        }

        if(idType.equals(completeSignUpFragment.MOBILE_ID)){
            chars = charSequence.toString();
            if(null != inputLayout) {
                if(chars.isEmpty())
                    inputLayout.setError("Input mobile number");
                else if(chars.length() >0 && chars.length() !=11){
                    inputLayout.setError("Number should be 11");
                }
                else
                    inputLayout.setError("");
            }
        }

        //Text watcher for EventFragment.
        if(idType.equals(eventFragment.TITLE)) {
            chars = charSequence.toString();
            if(null != editText) {
                if(chars.isEmpty())
                    editText.setError("Input event title");
                else
                    editText.setError(null);
            }
        }
        if(idType.equals(eventFragment.TEXT)) {
            chars = charSequence.toString();
            int charLength = chars.length();
            if(null != inputLayout) {
                inputLayout.setError(charLength + " of 200");
                if(null != editText) {
                    if(charLength > 200)
                        editText.setError("Above 200 words");
                    else
                        editText.setError(null);
                }
            }
        }
        /*if(idType.equals(eventFragment.DATE)) {
            chars = charSequence.toString();
            if(null != editText) {
                if(chars.isEmpty())
                    editText.setError("Input event date");
                else
                    editText.setError(null);
            }
        }
        if(idType.equals(eventFragment.TIME)) {
            chars = charSequence.toString();
            if(null != editText) {
                if(chars.isEmpty())
                    editText.setError("Input event time");
                else
                    editText.setError(null);
            }
        }*/


    }

    @Override
    public void afterTextChanged(Editable editable) {
        /*Log.i(LOG_TAG, "afterTextChanged");
        String chars = "", firstThree = "", secondThree = "", thirdThree = "";
        if(idType.equals(completeSignUpFragment.MOBILE_ID)){
            //get editable string
            chars = editable.toString();
            if(chars.length() == 11){
                *//**
                 * Nigeria mobile number are in total of 11
                 * but to make calls considering here in Nigeria
                 * and outside Nigeria, the country code must be
                 * added to the last 10 digit of the mobile number
                 * Therefore, get the last 10 digit
                 *//*
                String getChars = chars.substring(1);// replace string chas with the last 10 digit
                Log.i(LOG_TAG, "Chars with 10 digit " + getChars);
                AppController.getLast10Digit = getChars;//send digit for online storage
                //perform separation operation with '-' after three numbers to display
                firstThree = chars.substring(1,4);//first 3
                secondThree = chars.substring(4,7);//second 3
                thirdThree = chars.substring(7);//last 4
                //get all and set EditText
                getChars = firstThree +" - "+ secondThree +" - "+ thirdThree;
                AppController.getSeparatedDigit = getChars;
                Log.i(LOG_TAG, "getChars mobile " +getChars);
                editText.setText(getChars);
                chars = "";
            }
        }*/
    }
}
