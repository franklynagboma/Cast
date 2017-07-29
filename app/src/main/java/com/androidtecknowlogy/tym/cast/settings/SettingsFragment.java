package com.androidtecknowlogy.tym.cast.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.androidtecknowlogy.tym.cast.R;

/**
 * Created by AGBOMA franklyn on 7/27/17.
 */

public class SettingsFragment extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener{

    private final String LOG_TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //add view
        addPreferencesFromResource(R.xml.settings);

        setListBindingOnly();

        Preference resetPreference = findPreference(getString(R.string.pref_reset_key));
        resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                bindPreferenceSummaryToView(findPreference(getString(R.string.pref_reset_key)));
                return true;
            }
        });
    }

    private void setListBindingOnly () {
        //bind preference value to summary
        bindPreferenceSummaryToView(findPreference(getString(R.string.pref_list_key)));
        bindPreferenceSummaryToView(findPreference(getString(R.string.pref_cast_list_key)));
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
        final String value = newValue.toString();
        Log.i(LOG_TAG, "Object value: " + value);

        if(preference instanceof ListPreference) {
            //changeListPreference(preference, value, 1);
            //get preference to list
            ListPreference listPreference = (ListPreference) preference;
            //get index checked.
            int prefIndex = listPreference.findIndexOfValue(value);
            //set checked summary.
            if(prefIndex >= 0)
                preference.setSummary(listPreference.getEntries()[prefIndex]);
        }

        //if instance of preference that is, Reset.
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(preference.getContext());
            builder.setTitle("Warning!!!")
                    .setMessage("This will clear all previous settings done on CAST")
                    .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //clear settings form online database.

                            ListPreference listPreference1 = (ListPreference)
                                    findPreference(getString(R.string.pref_list_key));
                            ListPreference listPreference2 = (ListPreference)
                                    findPreference(getString(R.string.pref_cast_list_key));

                            listPreference1.setValue(getString(R.string.every_one));
                            listPreference2.setValue(getString(R.string.as_they_are_created));

                            setListBindingOnly();

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Abort", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog build = builder.create();
            build.setCanceledOnTouchOutside(true);
            build.show();
        }
        return true;
    }
}
