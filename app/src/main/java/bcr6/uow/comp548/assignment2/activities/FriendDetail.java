package bcr6.uow.comp548.assignment2.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import bcr6.uow.comp548.assignment2.ImageHelper;
import bcr6.uow.comp548.assignment2.R;
import bcr6.uow.comp548.assignment2.database.DatabaseHelper;
import bcr6.uow.comp548.assignment2.database.ORMBaseActivity;
import bcr6.uow.comp548.assignment2.models.Friend;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.io.text.VCardWriter;
import ezvcard.property.Address;
import ezvcard.property.StructuredName;

import static android.support.v4.content.FileProvider.getUriForFile;

//public class FriendDetail extends ORMBaseActivity<DatabaseHelper> implements OnMapReadyCallback {
public class FriendDetail extends ORMBaseActivity<DatabaseHelper> {

    private Friend friend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_detail_activity);

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
        NavUtils.navigateUpFromSameTask(this);
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
        } else {
            photoIV.setScaleType(ImageView.ScaleType.FIT_CENTER);
            photoIV.setImageResource(R.drawable.ic_person_white_24dp);
            friend.setImagePath("");
        }


        TextView mobileTV = (TextView) findViewById(R.id.friend_detail_phone);
        TextView emailTV = (TextView) findViewById(R.id.friend_detail_email);
        TextView addressTV = (TextView) findViewById(R.id.friend_detail_address);


        //If no details for the contact
        if (friend.isEmpty()) {

            mobileTV.setText(R.string.add_phone_number);
            mobileTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit();
                }
            });
            emailTV.setText(R.string.add_email);
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
/*	    View container = findViewById(R.id.friend_map_container);
		if (friend.hasPlace()) {
			container.setVisibility(View.VISIBLE);
			MapFragment mapFragment = (MapFragment) getFragmentManager()
					.findFragmentById(R.id.friend_map);

			mapFragment.getMapAsync(this);
		} else
			container.setVisibility(View.GONE);*/

    }


	public void openMapActivity(View view) {
		Intent intent = new Intent(this, FriendLocationDetails.class);
		startActivity(intent);
	}

/*
	@Override
	public void onMapReady(GoogleMap map) {
		//Friend LatLng
		LatLng addressCoordinates = new LatLng(friend.getLat(), friend.getLng());
		//Curr LatLng
		//TODO what if there is no permission?
		LatLng currCoordinates = new LatLng(-37.7846035,175.3084514);

		//Works out the co-ordinates between the two LatLng
		float[] results = new float[1];
		Location.distanceBetween(addressCoordinates.latitude, addressCoordinates.longitude,
				currCoordinates.latitude, currCoordinates.longitude, results);

		float distance = results[0]/1000;   //How far current position away from user
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(addressCoordinates, 10));
		map.addMarker(new MarkerOptions().title(friend.getName()).snippet(distance + " km's away").position(addressCoordinates));
	}
*/

    /**
     * Exports the contact as a vcf
     */
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
//	    vcard.setGeo(friend.getLat(), friend.getLng());


        try {
            String fileName = friend.getName() + ".vcf";
            File internalDirectory = new File(getFilesDir(), "vcf");
            if (!internalDirectory.mkdirs())
                Log.e("VCard", "vcf directory either already exists, or it was uanble to be created");

            File contactVCF = new File(internalDirectory, fileName);

	        VCardWriter writer = new VCardWriter(contactVCF, VCardVersion.V3_0);
	        writer.write(vcard);
	        writer.close();

            Uri contentUri = getUriForFile(getApplicationContext(), "bcr6.uow.comp548.assignment2.fileprovider", contactVCF);

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setData(contentUri);
            sendIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sendIntent.setType("text/x-vcard");
            startActivity(sendIntent);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("VCARD", "Error writing vcard to internal directory");
            Toast.makeText(this, "Error sharing contact", Toast.LENGTH_LONG).show();
        }
    }
}
