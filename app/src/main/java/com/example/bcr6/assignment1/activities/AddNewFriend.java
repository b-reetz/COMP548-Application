package com.example.bcr6.assignment1.activities;

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;

import com.example.bcr6.assignment1.R;
import com.example.bcr6.assignment1.fragments.AddNewFriendContactPictureFragment;
import com.example.bcr6.assignment1.fragments.AddNewFriendDetailsFragment;

public class AddNewFriend extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend_activity);

        //Sets up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle(R.string.add_new_friend_toolbar_label);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Creates the fragments (set to retain themselves on activity recreate), and replaces relevant FrameLayouts in the activity
        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
        AddNewFriendContactPictureFragment contactPictureFragment = AddNewFriendContactPictureFragment.newInstance();
        AddNewFriendDetailsFragment friendDetails = AddNewFriendDetailsFragment.newInstance();
        contactPictureFragment.setRetainInstance(true);
        friendDetails.setRetainInstance(true);
        fragmentManager.replace(R.id.add_new_friend_contact_photo, contactPictureFragment);
        fragmentManager.replace(R.id.add_new_friend_contact_details, friendDetails);
        fragmentManager.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_friend_action_items, menu);
        return true;
    }
}
