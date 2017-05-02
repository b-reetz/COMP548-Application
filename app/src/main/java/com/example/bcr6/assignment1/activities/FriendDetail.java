package com.example.bcr6.assignment1.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bcr6.assignment1.ImageHelper;
import com.example.bcr6.assignment1.R;
import com.example.bcr6.assignment1.database.DatabaseHelper;
import com.example.bcr6.assignment1.database.ORMBaseActivity;
import com.example.bcr6.assignment1.models.Friend;
import com.hannesdorfmann.swipeback.Position;
import com.hannesdorfmann.swipeback.SwipeBack;

import java.io.File;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.Address;
import ezvcard.property.StructuredName;

public class FriendDetail extends ORMBaseActivity<DatabaseHelper> {

    private Friend friend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.friend_detail_activity);

        SwipeBack.attach(this, SwipeBack.Type.BEHIND, Position.LEFT)
                .setContentView(R.layout.friend_detail_activity)
                .setSwipeBackView(R.layout.swipeback_default);


        copyToClipboardConfirmation(findViewById(R.id.friend_detail_phone));
        copyToClipboardConfirmation(findViewById(R.id.friend_detail_address));
        copyToClipboardConfirmation(findViewById(R.id.friend_detail_email));

        populateData();
    }

    private void copyToClipboardConfirmation(View v) {

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String toCopy = ((TextView) v).getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                String[] items = {"Copy to clipboard"};
                builder.setTitle(toCopy)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("contact phone", toCopy);
                                    clipboard.setPrimaryClip(clip);
                                }

                            }
                        }).create().show();

                return true;
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        populateData();
    }

    /**
     * Inflates the custom menu items in to the menu on the toolbar
     * @param menu The menu to inflate my items in to
     * @return True if we were able to inflate it
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO icon is showing up in overflow menu, rather than on toolbar
        getMenuInflater().inflate(R.menu.friend_detail_action_items, menu);
        return true;
    }

    /**
     *
     * @param item the item selected within the menu
     * @return ..
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_contact: {
                deleteContact();
                return true;
            }
            case R.id.action_edit_contact: {
                edit();
                return true;
            }
            case R.id.action_share_contact: {
                share();
                return true;
            }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.swipeback_stack_to_front,
                R.anim.swipeback_stack_right_out);
//        NavUtils.navigateUpFromSameTask(this);
    }

    private void deleteContact() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this contact?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                        getHelper().getFriendDataDao().delete(friend);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Mandatory onClick override. Don't need to do anything here
                    }
                }).create().show();
    }

    private void edit() {
        Intent intent = new Intent(this, EditFriend.class);
        intent.putExtra("FRIENDID", friend.getId());
        startActivity(intent);
    }

    private void populateData() {
        //sets up friend id, or else breaks
        int friendID = 0;
        if (getIntent().hasExtra("FRIENDID"))
            friendID = getIntent().getIntExtra("FRIENDID", 0);
        if (friendID == 0)
            throw new IllegalArgumentException("No extra was passed through to this activity.. How did we get here?");

        friend = getHelper().getFriendDataDao().queryForId(friendID);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(friend.getFirstName() + " " + friend.getLastName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        ImageView photoIV = (ImageView) findViewById(R.id.friend_detail_photo);
        if (!friend.getImagePath().isEmpty()) {
            photoIV.setImageBitmap(ImageHelper.bitmapSmaller(friend.getImagePath(), 300, 300));
            photoIV.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }


        TextView mobileTV = (TextView) findViewById(R.id.friend_detail_phone);
        TextView emailTV = (TextView) findViewById(R.id.friend_detail_email);
        TextView addressTV = (TextView) findViewById(R.id.friend_detail_address);

        //If no details for the contact
        if (friend.isEmpty()) {

            mobileTV.setText("Add phone number");
            mobileTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit();
                }
            });
            emailTV.setText("Add email");
            emailTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit();
                }
            });
            addressTV.setVisibility(View.GONE);

        } else {

            if (!friend.getMobileNumber().isEmpty()) {
                mobileTV.setText(friend.getMobileNumber());
                mobileTV.setVisibility(View.VISIBLE);
            } else
                mobileTV.setVisibility(View.GONE);

            if (!friend.getEmailAddress().isEmpty()) {
                emailTV.setText(friend.getEmailAddress());
                emailTV.setVisibility(View.VISIBLE);
            } else
                emailTV.setVisibility(View.GONE);


            if (!friend.getAddress().isEmpty()) {
                addressTV.setText(friend.getAddress());
                addressTV.setVisibility(View.VISIBLE);
            } else
                addressTV.setVisibility(View.GONE);
        }
    }

    private void share() {
        VCard vcard = new VCard();
        StructuredName name = new StructuredName();

        name.setFamily(friend.getLastName());
        name.setGiven(friend.getFirstName());
        vcard.setStructuredName(name);

        Address address = new Address();
        String fullAddress = friend.getAddress();
        String streetAddress = fullAddress.split(",")[0];
        address.setStreetAddress(streetAddress);
        vcard.addAddress(address);

        vcard.addEmail(friend.getEmailAddress());

        vcard.addTelephoneNumber(friend.getMobileNumber());

        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "test");
        if (!f.mkdirs())
            Log.e("SHARE", "Directory not created");



        File file = new File(f.getAbsolutePath() + "/vcard.vcf");
/*        if (!file.exists())
            file.mkdir();*/
        try {
            Ezvcard.write(vcard).go(file);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/x-vcard");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(sendIntent);

    }
}
