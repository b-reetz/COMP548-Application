package bcr6.uow.comp548.application.fragments;

import android.preference.PreferenceFragment;
import android.os.Bundle;

import bcr6.uow.comp548.application.R;

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
