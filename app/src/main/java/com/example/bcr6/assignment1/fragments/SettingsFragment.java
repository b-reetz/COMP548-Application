package com.example.bcr6.assignment1.fragments;

import android.preference.PreferenceFragment;
import android.os.Bundle;

import com.example.bcr6.assignment1.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);

        addPreferencesFromResource(R.xml.preferences);
    }

    public SettingsFragment() {
    }



/*    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }*/
}
