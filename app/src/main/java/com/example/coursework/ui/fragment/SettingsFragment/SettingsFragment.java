package com.example.coursework.ui.fragment.SettingsFragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.coursework.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}