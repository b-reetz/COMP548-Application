package com.example.bcr6.assignment1.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bcr6.assignment1.R;
import com.example.bcr6.assignment1.fragments.AddNewFriendContactPictureFragment;
import com.example.bcr6.assignment1.fragments.AddNewFriendDetailsFragment;
import com.example.bcr6.assignment1.models.Friend;

public class AddNewFriend extends AppCompatActivity implements AddNewFriendContactPictureFragment.OnFragmentInteractionListener {

    String imagePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_friend_activity);

        //Sets up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        setTitle(R.string.add_new_friend_toolbar_label);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator();
        }

        //Creates the fragments (set to retain themselves on activity recreate), and replaces relevant FrameLayouts in the activity
/*        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
        AddNewFriendContactPictureFragment contactPictureFragment = AddNewFriendContactPictureFragment.newInstance();
        AddNewFriendDetailsFragment friendDetails = AddNewFriendDetailsFragment.newInstance();
        contactPictureFragment.setRetainInstance(true);
        friendDetails.setRetainInstance(true);
        fragmentManager.replace(R.id.add_new_friend_contact_photo, contactPictureFragment);
        fragmentManager.replace(R.id.add_new_friend_contact_details, friendDetails);
        fragmentManager.commit();*/
    }

    @Override
    public void onFragmentInteraction() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = getResources().getStringArray(imagePath.isEmpty() ? R.array.no_photo : R.array.new_photo);
        builder.setTitle("Change photo")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Mandatory onClick override. Don't need to do anything here
                    }
                }).create().show();

/*        EditText firstName = (EditText) findViewById(R.id.add_new_friend_details_first_name_edit_text);
        Toast.makeText(this, firstName.getText().toString(), Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_friend_action_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_contact: {
                insertFriend();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void insertFriend() {
        EditText firstName = (EditText) findViewById(R.id.add_new_friend_details_first_name_edit_text);
        EditText lastName = (EditText) findViewById(R.id.add_new_friend_details_last_name_edit_text);
        EditText mobileNumber = (EditText) findViewById(R.id.add_new_friend_details_mobile_number_edit_text);
        EditText emailAddress = (EditText) findViewById(R.id.add_new_friend_details_email_edit_text);
        EditText address = (EditText) findViewById(R.id.add_new_friend_details_address_edit_text);

        Friend friend = new Friend(
                firstName.getText().toString(), lastName.getText().toString(),
                mobileNumber.getText().toString(), emailAddress.getText().toString(),
                address.getText().toString(), imagePath);

    }


}
