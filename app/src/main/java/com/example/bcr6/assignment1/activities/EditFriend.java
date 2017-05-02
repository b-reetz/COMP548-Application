package com.example.bcr6.assignment1.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bcr6.assignment1.ImageHelper;
import com.example.bcr6.assignment1.R;
import com.example.bcr6.assignment1.database.DatabaseHelper;
import com.example.bcr6.assignment1.database.ORMBaseActivity;
import com.example.bcr6.assignment1.fragments.EditFriendContactPictureFragment;
import com.example.bcr6.assignment1.fragments.EditFriendDetailsFragment;
import com.example.bcr6.assignment1.models.Friend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by Brendan
 *
 * This activity let's you input details and a photo from the camera or gallery to be saved for a contact
 */
public class EditFriend extends ORMBaseActivity<DatabaseHelper> implements EditFriendContactPictureFragment.OnFragmentInteractionListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;
    private static final int API_LEVEL = android.os.Build.VERSION.SDK_INT;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    EditFriendContactPictureFragment pictureFragment;
    EditFriendDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_friend_activity);

        //Sets up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        setSupportActionBar(toolbar);

        setTitle("Edit contact");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //If first time creating activity
        if (savedInstanceState == null) {


            Log.d("t", "Building picture fragment");

            int friendID = getIntent().getIntExtra("FRIENDID", 0);
            if (friendID == 0)
                throw new IllegalArgumentException("No friend ID passed in to the activity");

            Friend f = getHelper().getFriendDataDao().queryForId(friendID);

            pictureFragment = EditFriendContactPictureFragment.newInstance();
            pictureFragment.setImagePath(f.getImagePath());
            detailsFragment = EditFriendDetailsFragment.newInstance();
            detailsFragment.setFriend(f);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.edit_contact_picture_container, pictureFragment, "PICTURE");
            fragmentTransaction.replace(R.id.edit_contact_details_container, detailsFragment, "DETAILS");
            fragmentTransaction.commit();
        } else if (savedInstanceState.containsKey("SET")) {
//            imagePath = savedInstanceState.getString("IMAGE");
            pictureFragment = (EditFriendContactPictureFragment) getFragmentManager().findFragmentByTag("PICTURE");
            detailsFragment = (EditFriendDetailsFragment) getFragmentManager().findFragmentByTag("DETAILS");
        }
    }


    /**
     * Saves a toggle into the savedInstanceState, just so we know that this
     * @param out The bundle that we are saving in to. This is the bundle that should get passed in to onCreate
     */
    @Override
    public void onSaveInstanceState(Bundle out) {
        out.putBoolean("SET", true);
        super.onSaveInstanceState(out);
    }

    /**
     * Use by my fragment as a click listener to give control back to activity when the fragment is interacted with
     * This particular method deals with selecting photos
     */
    @Override
    public void onFragmentInteraction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final String[] items = getResources().getStringArray(pictureFragment.getImagePath().isEmpty() ? R.array.no_photo : R.array.new_photo);
        builder.setTitle("Change photo")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (items[which]) {
                            case "Take Photo":
                            case "Take new photo":
                                takePhoto();
                                break;
                            case "Select new photo":
                            case "Choose photo":
                                choosePhoto();
                                break;
                            case "Remove photo":
                                removePhoto();
                                break;

                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Mandatory onClick override. Don't need to do anything here
                    }
                }).create().show();


    }

    /**
     * Inflates the custom menu items in to the menu on the toolbar
     * @param menu The menu to inflate my items in to
     * @return True if we were able to inflate it
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TODO icon is showing up in overflow menu, rather than on toolbar
        getMenuInflater().inflate(R.menu.menu_edit_friend, menu);
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
            case R.id.edit_friend_save: {
                if (updateFriend())
//                    onBackPressed();
                    NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This is called when an activity is called with an intent to return with a result.
     * @param requestCode The (hopefully) unique code that got sent with the intent
     * @param resultCode Successful or not code
     * @param data The data returned from the intent
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If we just took a photo
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //RESULT_OK = -1
            try {

                FileChannel fis = new FileInputStream(new File(pictureFragment.getImagePath())).getChannel();
                pictureFragment.setImagePath(ImageHelper.createImageFile(this)); //changes the imagePath variable to new empty file
                int lastIndex = pictureFragment.getImagePath().lastIndexOf('/') + 1;
                FileChannel fos = openFileOutput(pictureFragment.getImagePath().substring(lastIndex, pictureFragment.getImagePath().length()), MODE_PRIVATE).getChannel();
                fos.transferFrom(fis, 0, fis.size());
                fos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //------------------If we just selected a photo from the gallery--------------
        else if(requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK) {
            /* //TODO need to keep a track of what images we take but don't save..
             * List all the media's, then query that media using returned uri
             * Go to the first option (which should be our file), and list
             *  file's absolute path. We then copy file from public directory in to private memory
             * Then make cursor null
             */
            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = this.getContentResolver().query(data.getData(), proj, null, null, null);
                if (cursor == null)
                    throw new NullPointerException("No Cursor was found when looking for an image");

                int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                String url = data.getData().toString();
                if (url.startsWith("content://com.google.android.apps.photos.content")) {
                    InputStream is = getContentResolver().openInputStream(data.getData());
                    pictureFragment.setImagePath(ImageHelper.createImageFile(this));
                    int i = pictureFragment.getImagePath().lastIndexOf('/') + 1;
                    ReadableByteChannel fis = Channels.newChannel(is);
//                    FileOutputStream fos1 = openFileOutput(pictureFragment.getImagePath().substring(i, pictureFragment.getImagePath().length()), Context.MODE_PRIVATE);

                    FileChannel fos = openFileOutput(pictureFragment.getImagePath().substring(i, pictureFragment.getImagePath().length()), Context.MODE_PRIVATE).getChannel();
                    fos.transferFrom(fis, 0, 9999999);
                    fos.close();
                    fis.close();
                } else {

                    pictureFragment.setImagePath(cursor.getString(column_index));


            /* Creates FIS from selected image path, then changes imagePath to be a new empty File
                We then copy the bytes from the selected image into the new internal image file*/

                    FileChannel fis = new FileInputStream(new File(pictureFragment.getImagePath())).getChannel();
                    pictureFragment.setImagePath(ImageHelper.createImageFile(this));
                    int i = pictureFragment.getImagePath().lastIndexOf('/') + 1;
                    FileChannel fos = openFileOutput(pictureFragment.getImagePath().substring(i, pictureFragment.getImagePath().length()), Context.MODE_PRIVATE).getChannel();
                    fos.transferFrom(fis, 0, fis.size());
                    fos.close();
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
//        If we selected an image
        if (!pictureFragment.getImagePath().isEmpty()) {
//        Sets the image as the new select image
            ImageView iV = ((ImageView) findViewById(R.id.edit_friend_picture_silhouette));
            iV.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int height = iV.getHeight();
            int width = iV.getWidth();
            iV.setImageBitmap(ImageHelper.bitmapSmaller(pictureFragment.getImagePath(), width, height));
        }
    }

    /**
     * Adds a friend to the database
     * @return True if successfully added
     */
    private boolean updateFriend() {
        EditText firstNameText = (EditText) findViewById(R.id.edit_friend_details_first_name_edit_text);
        EditText lastNameText = (EditText) findViewById(R.id.edit_friend_details_last_name_edit_text);

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();

        //Needs at least a name
        if (firstName.isEmpty() && lastName.isEmpty()) {
            firstNameText.setError("");
            lastNameText.setError("");
            return false;
        }

        Friend friend = detailsFragment.getUpdatedFriend();
        friend.setImagePath(pictureFragment.getImagePath());
        getHelper().getFriendDataDao().update(friend);

        return true;
    }

    /**
     * Let's you choose a photo from the gallery to use as the contact photo
     */
    private void choosePhoto() {
        if (API_LEVEL >= 23)
            if (checkPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                getPermissions(PERMISSIONS_STORAGE);
                return;
            }

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );

        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    /**
     * Uses the phone's camera to take a photo
     */
    private void takePhoto() {
        //If we need to check permissions
        if (API_LEVEL >= 23) {
            if (checkPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                getPermissions(PERMISSIONS_STORAGE);
                return;
            }
        }
//        Creates an empty temporary file in public directory Pictures/Friends, and then calls camera to take a photo
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                //foal-ed public directory
                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                        + "/Friends");
                //TODO change this temporary file thing to go in the app's cache instead

                try {
                    //creates a temp file that gets deleted when app closes (hopefully)
                    File image = File.createTempFile(
                            "temp",
                            ".jpg",
                            f
                    );
                    image.deleteOnExit();
                    pictureFragment.setImagePath(image.getAbsolutePath()); //creates a temporary file, saving path in imagePath
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Returns whether the user has access to this particular permission
     * @param activity The activity requesting this permission
     * @param permission The particular permission to look in to
     * @return True if the activity DOESN't have the permission, True if not
     */
    private boolean checkPermissions(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED;
    }


    /**
     * Requests permissions from the user.
     * @param permissions The list of permissions to request from the user
     */
    private void getPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, 1);
    }

    /**
     * Removes the user's photo and sets it back to the default one
     */
    private void removePhoto() {
        pictureFragment.removePhoto();
    }

}
