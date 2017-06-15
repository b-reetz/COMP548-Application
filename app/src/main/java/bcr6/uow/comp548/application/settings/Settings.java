package bcr6.uow.comp548.application.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.Toolbar;

import bcr6.uow.comp548.application.R;

public class Settings extends Activity {

    public final static String SORT_BY = "contacts_sort_by";
    public final static String SORT_ORDER = "contacts_sort_order";

    public static final int SORT_FIRST = 1;
    public static final int SORT_LAST = 2;
    public static final int SORT_A_Z = 1;
    public static final int SORT_Z_A = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setActionBar(toolbar);

        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
        NavUtils.navigateUpFromSameTask(this);
    }

}
