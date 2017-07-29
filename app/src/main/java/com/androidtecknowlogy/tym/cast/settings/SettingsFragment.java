package com.androidtecknowlogy.tym.cast.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.androidtecknowlogy.tym.cast.R;

/**
 * Created by AGBOMA franklyn on 7/27/17.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //add view
        addPreferencesFromResource(R.xml.settings);
    }
}
