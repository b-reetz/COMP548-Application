package bcr6.uow.comp548.assignment2.fragments;

import android.preference.PreferenceFragment;
import android.os.Bundle;

import bcr6.uow.comp548.assignment2.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
