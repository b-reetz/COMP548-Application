package bcr6.uow.comp548.assignment2.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import bcr6.uow.comp548.assignment2.R;
import bcr6.uow.comp548.assignment2.database.DatabaseHelper;
import bcr6.uow.comp548.assignment2.database.ORMBaseActivity;
import bcr6.uow.comp548.assignment2.models.Friend;

public class FriendLocationDetails extends ORMBaseActivity<DatabaseHelper> implements OnMapReadyCallback {

	private Friend friend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_location_activity);

		populateData();

/*		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);*/
	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {

		// Add a marker in Sydney and move the camera
		LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
	}

	public void populateData() {
        int friendID;
        String errorMessage = "No extra was passed through to this activity.. How did we get here?";
        if (getIntent().hasExtra("friendID"))
            friendID = getIntent().getIntExtra("friendID", 0);
        else
            throw new IllegalArgumentException(errorMessage);

        friend = getHelper().getFriendDataDao().queryForId(friendID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(friend.getOneName() + "'s location details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView addressTV = (TextView) findViewById(R.id.friend_location_address);
        addressTV.setText(friend.getAddress());
	}
}
