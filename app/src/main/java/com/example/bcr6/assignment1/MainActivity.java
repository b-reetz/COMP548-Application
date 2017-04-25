package com.example.bcr6.assignment1;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.bcr6.assignment1.activities.AddNewFriend;
import com.example.bcr6.assignment1.activities.FriendDetail;
import com.example.bcr6.assignment1.activities.Settings;
import com.example.bcr6.assignment1.database.DatabaseHelper;
import com.example.bcr6.assignment1.database.ORMBaseActivity;
import com.example.bcr6.assignment1.fragments.MainFragment;
import com.example.bcr6.assignment1.models.Friend;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.bcr6.assignment1.activities.Settings.SORT_A_Z;
import static com.example.bcr6.assignment1.activities.Settings.SORT_BY;
import static com.example.bcr6.assignment1.activities.Settings.SORT_FIRST;
import static com.example.bcr6.assignment1.activities.Settings.SORT_LAST;
import static com.example.bcr6.assignment1.activities.Settings.SORT_ORDER;
import static com.example.bcr6.assignment1.activities.Settings.SORT_Z_A;

public class MainActivity extends ORMBaseActivity<DatabaseHelper>
        implements MainFragment.OnListFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //If we couldn't set up the directory
        try {
            ImageHelper.setUpDirectory();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("IMAGE", e.getMessage());
        }
        //Sets the users preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSupport);
        setSupportActionBar(toolbar);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MainFragment mainFragment = MainFragment.newInstance();

        //Retrieves the Friends list sorted from the shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int sort_by = Integer.parseInt(sharedPref.getString(SORT_BY, SORT_FIRST+""));
        int sort_order = Integer.parseInt(sharedPref.getString(SORT_ORDER, SORT_A_Z+""));
        List<Friend> friends = getFriendsList(sort_by, sort_order);

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
     *
     * @param sort_by Sort by first or last name (1 or 2)
     * @param sort_order Sort by A-Z or Z-A (1 or 2)
     * @return Returns the new list sorted the right way
     */
    private List<Friend> getFriendsList(int sort_by, int sort_order) {

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
        intent.putExtra("FRIENDID", friendID);
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
}
