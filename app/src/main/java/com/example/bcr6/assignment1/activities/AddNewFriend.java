package com.example.bcr6.assignment1.activities;

import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.example.bcr6.assignment1.R;
import com.example.bcr6.assignment1.fragments.AddNewFriendContactPictureFragment;
import com.example.bcr6.assignment1.fragments.AddNewFriendDetailsFragment;

public class AddNewFriend extends AppCompatActivity {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String MOBILE_NUMBER = "mobileNumber";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String ADDRESS = "address";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.add_new_friend_activity);

        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
        AddNewFriendContactPictureFragment contactPictureFragment = AddNewFriendContactPictureFragment.newInstance();
        AddNewFriendDetailsFragment friendDetails = AddNewFriendDetailsFragment.newInstance();
        fragmentManager.replace(R.id.add_new_friend_contact_photo, contactPictureFragment);
        fragmentManager.replace(R.id.add_new_friend_contact_details, friendDetails);
        fragmentManager.commit();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        EditText firstName = (EditText) findViewById(R.id.add_new_friend_details_first_name_edit_text);
        EditText lastName = (EditText) findViewById(R.id.add_new_friend_details_last_name_edit_text);
        EditText mobileNumber = (EditText) findViewById(R.id.add_new_friend_details_mobile_number_edit_text);
        EditText emailAddress = (EditText) findViewById(R.id.add_new_friend_details_email_edit_text);
        EditText address = (EditText) findViewById(R.id.add_new_friend_details_address_edit_text);

        savedInstanceState.putString(FIRST_NAME, firstName.getText().toString());
        savedInstanceState.putString(LAST_NAME, lastName.getText().toString());
        savedInstanceState.putString(MOBILE_NUMBER, mobileNumber.getText().toString());
        savedInstanceState.putString(EMAIL_ADDRESS, emailAddress.getText().toString());
        savedInstanceState.putString(ADDRESS, address.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }
}
