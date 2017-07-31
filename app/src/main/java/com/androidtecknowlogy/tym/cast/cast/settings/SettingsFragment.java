package com.androidtecknowlogy.tym.cast.cast.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.androidtecknowlogy.tym.cast.R;
import com.androidtecknowlogy.tym.cast.app.AppController;

/**
 * Created by AGBOMA franklyn on 7/27/17.
 */

public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener{

    private final String LOG_TAG = SettingsFragment.class.getSimpleName();

    private SharedPreferences pref;
    private String castEmail, password;
    private ProgressDialog loading;
    private ListPreference listPreference1;
    private ListPreference listPreference2;
    private String previousValue1 = "";
    private String previousValue2 = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //get cast user email for store settings on database for identification.
        String[] removeDot = pref.getString("email", "").split("\\.");
        password = pref.getString("password", "");
        castEmail = removeDot[0];
        Log.i(LOG_TAG, "Email " + castEmail);
        //add view
        addPreferencesFromResource(R.xml.settings);

        //get value from database to setDefaultValues.


        //bind preference value to summary
        listPreference1 = (ListPreference)
                findPreference(getString(R.string.pref_list_key));
        listPreference2 = (ListPreference)
                findPreference(getString(R.string.pref_cast_list_key));
        //if use is guess remove perference listPreference1
        if(AppController.isGuess)
            listPreference1.setEnabled(false);

        //set up summary from previous values
        if (AppController.settingMap.containsKey(castEmail))
            listPreference1.setSummary(AppController.settingMap.get(castEmail).getShowDob());
        //user just lunch the application for the first time.
        else
            listPreference1.setValue(null);

        //get values from preference
        getListPreferenceValues();

        Preference resetPreference = findPreference(getString(R.string.pref_reset_key));

        listPreference1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                bindPreferenceSummaryToView(findPreference(getString(R.string.pref_list_key)));
                return true;
            }
        });
        listPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                bindPreferenceSummaryToView(findPreference(getString(R.string.pref_cast_list_key)));
                return true;
            }
        });


        resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                bindPreferenceSummaryToView(findPreference(getString(R.string.pref_reset_key)));
                return true;
            }
        });
    }


    private void bindPreferenceSummaryToView (Preference preference) {
        /**
         *get the changes made and trigger immediately.
         */
        preference.setOnPreferenceChangeListener(this);
        //for strings values such as list
        if(preference.getKey().equals(getString(R.string.pref_list_key))
                || preference.getKey().equals(getString(R.string.pref_cast_list_key))
                || preference.getKey().equals(getString(R.string.pref_reset_key)))
            onPreferenceChange(preference, PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));

    }

    @Override
    public boolean onPreferenceChange(final Preference preference, Object newValue) {
        String value = newValue.toString();
        Log.i(LOG_TAG, "Object value: " + value);

        if(preference instanceof ListPreference) {
            //changeListPreference(preference, value, 1);
            //get preference to list
            ListPreference listPreference = (ListPreference) preference;
            //get index checked.
            int prefIndex = listPreference.findIndexOfValue(value);
            //set checked summary.
            if(prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);

                if(listPreference == findPreference(getString(R.string.pref_list_key))) {
                    if(!preference.getSummary().toString().equals(previousValue1))
                        savePreference(listPreference.getTitle().toString(), value);
                }
                /*else if(listPreference == findPreference(getString(R.string.pref_cast_list_key))) {
                    if(!preference.getSummary().toString().equals(previousValue2))
                        savePreference(listPreference.getTitle().toString(), value);
                }*/
            }
        }

        //if instance of preference that is, Reset.
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(preference.getContext());
            final EditText passwordText = new EditText(preference.getContext());
            passwordText.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordText.setHint("password");
            builder.setTitle("Warning!!!")
                    .setMessage("This will clear all previous settings done on CAST")
                    .setView(passwordText)
                    .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //clear settings form online database.

                            if(passwordText.getText().toString().equals(password)) {
                                String getValue1 = getString(R.string.every_one);
                                String getValue2 = getString(R.string.as_they_are_created);
                                listPreference1.setValue(getValue1);
                                listPreference2.setValue(getValue2);

                                //bind two view again.
                                bindPreferenceSummaryToView(findPreference(
                                        getString(R.string.pref_list_key)));
                                bindPreferenceSummaryToView(findPreference(
                                        getString(R.string.pref_cast_list_key)));
                                //update online db.
                                AppController.settingsData.child(listPreference1.getTitle().toString())
                                        .setValue(getValue1);
                                AppController.settingsData.child(listPreference2.getTitle().toString())
                                        .setValue(getValue2);
                                dialog.dismiss();
                                startLoading("");
                                savePreference(listPreference1.getTitle().toString(), getValue1);
                                stopLoading();
                            }
                            else
                                Toast.makeText(getActivity(), "wrong password", Toast.LENGTH_SHORT)
                                        .show();
                        }
                    })
                    .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            stopLoading();
                        }
                    });

            AlertDialog build = builder.create();
            build.setCanceledOnTouchOutside(true);
            build.show();
        }
        return true;
    }

    private void startLoading(String getValue) {
        Log.i(LOG_TAG, "loading...");
        if(loading == null)
            loading = new ProgressDialog(getActivity());
        if(!loading.isShowing()) {
            loading.setMessage("saving changes...");
            loading.setCancelable(false);
            loading.show();
            //for device name, check if value is = Just this device
            //if yes value, no empty.
            AppController.settingsData
                    .child(castEmail)
                    .child("device")
                    .setValue(getValue.equals(getString(R.string.just_this_device))
                            ?AppController.currentDevice
                            :"");
        }
    }
    private void stopLoading() {
        Log.i(LOG_TAG, "stop loading...");
        if(loading != null && loading.isShowing()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loading.dismiss();
                }
            },3000);
        }
    }

    private void savePreference(String key, String value) {
        //change key name so as to relate with Settings pojo.
        if(key.equals(getString(R.string.pref_list_title)))
            key = AppController.SHOW_DOB;
        if(key.equals(getString(R.string.pref_cast_list_title)))
            key = AppController.CAST_UPDATE;

        //update online db.
        startLoading(value);
        AppController.settingsData
                .child(castEmail)
                .child(key)
                .setValue(value);
        stopLoading();
        getListPreferenceValues();
    }
    private void getListPreferenceValues() {
        previousValue1 = listPreference1.getValue();
        previousValue2 = listPreference2.getValue();
    }
}
