package com.androidtecknowlogy.tym.cast.cast.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidtecknowlogy.tym.cast.R;

/**
 * Created by AGBOMA franklyn on 7/29/17.
 */

/**
 * Settings fragment has a support context class
 * to make it support, it is added on this support fragment
 */
public class HostFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View host = inflater.inflate(R.layout.activity_setting_host, container, false);
        getActivity().getFragmentManager().beginTransaction()
                .add(R.id.settings_host, new SettingsFragment())
                .commit();
        return host;
    }
}
