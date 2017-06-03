package bcr6.uow.comp548.application;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import bcr6.uow.comp548.application.activities.AddNewFriend;
import bcr6.uow.comp548.application.activities.FriendDetail;
import bcr6.uow.comp548.application.activities.Settings;
import bcr6.uow.comp548.application.database.DatabaseHelper;
import bcr6.uow.comp548.application.database.ORMBaseActivity;
import bcr6.uow.comp548.application.fragments.MainFragment;
import bcr6.uow.comp548.application.models.Friend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static bcr6.uow.comp548.application.activities.Settings.SORT_A_Z;
import static bcr6.uow.comp548.application.activities.Settings.SORT_BY;
import static bcr6.uow.comp548.application.activities.Settings.SORT_FIRST;
import static bcr6.uow.comp548.application.activities.Settings.SORT_LAST;
import static bcr6.uow.comp548.application.activities.Settings.SORT_ORDER;
import static bcr6.uow.comp548.application.activities.Settings.SORT_Z_A;

public class MainActivity extends ORMBaseActivity<DatabaseHelper>
        implements MainFragment.OnListFragmentInteractionListener,
					SearchView.OnQueryTextListener {

    public static final int API_LEVEL = android.os.Build.VERSION.SDK_INT;

	private int sort_by, sort_order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Sets the users preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSupport);
        setSupportActionBar(toolbar);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MainFragment mainFragment = MainFragment.newInstance();

        //Retrieves the Friends list sorted from the shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sort_by = Integer.parseInt(sharedPref.getString(SORT_BY, SORT_FIRST+""));
        sort_order = Integer.parseInt(sharedPref.getString(SORT_ORDER, SORT_A_Z+""));
        List<Friend> friends = getFriendsList();

        mainFragment.setContacts(friends);
        transaction.replace(R.id.main_fragment_container, mainFragment, "CONTACTS");
        transaction.commit();


        //Sets up the settings drawer
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Sets the on item selected listener for the menu in the navigation drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_settings:
                        drawer.closeDrawers();
                        startActivity(new Intent(MainActivity.this, Settings.class));
                        return true;
                }
                return false;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(MainActivity.this, AddNewFriend.class);
                startActivity(newIntent);
            }
        });
    }

    /**
     * @return Returns the new list sorted the right way
     */
    private List<Friend> getFriendsList() {

        List<Friend> friends = getHelper().getFriendDataDao().queryForAll();

        //Sorts it by first name a-z
        if (sort_by == SORT_FIRST && sort_order == SORT_A_Z)
            Collections.sort(friends, new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return o1.getFirstName().compareTo(o2.getFirstName());
                }
            });
        //Sorts it by first name z-a
        else if (sort_by == SORT_FIRST && sort_order == SORT_Z_A)
            Collections.sort(friends, new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return -o1.getFirstName().compareTo(o2.getFirstName());
                }
            });
        //Sorts list by last name a-z
        else if (sort_by == SORT_LAST && sort_order == SORT_A_Z)
            Collections.sort(friends, new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return o1.getLastName().compareTo(o2.getLastName());
                }
            });
        //Sorts list by last name z-a
        else if (sort_by == SORT_LAST && sort_order == SORT_Z_A)
            Collections.sort(friends, new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return -o1.getLastName().compareTo(o2.getLastName());
                }
            });

        return friends;
    }

    @Override
    public void onListFragmentInteraction(int friendID) {
        Intent intent = new Intent(this, FriendDetail.class);
        intent.putExtra("friendID", friendID);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		final MenuItem searchItem = menu.findItem(R.id.action_search);
		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

		searchView.setOnQueryTextListener(this);
		//If we cancel the search view, remake the full list
		searchView.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				MainFragment fragment = (MainFragment) getFragmentManager().findFragmentByTag("CONTACTS");
				fragment.updateContacts(getFriendsList());
				return false;
			}
		});

		return true;
	}

	@Override
	public boolean onQueryTextChange(String query) {
		return false;
	}

	/**
	 *
	 * @param query The string we're searching for
	 * @return true if the query has been handled by the listener, false to let the SearchView perform the default action.
	 */
	@Override
	public boolean onQueryTextSubmit(String query) {
		//Filters the list.. not ideal.
		List<Friend> allFriends = getFriendsList();
		List<Friend> toReturn = new ArrayList<>();
		for (Friend f : allFriends)
			if (f.getName().toLowerCase().contains(query.toLowerCase()))
				toReturn.add(f);

		//Hides the keyboard
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		View v = getCurrentFocus();
		if (v != null)
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

		//Updates the list
		MainFragment fragment = (MainFragment) getFragmentManager().findFragmentByTag("CONTACTS");
		fragment.updateContacts(toReturn);
		return true;
	}
}
