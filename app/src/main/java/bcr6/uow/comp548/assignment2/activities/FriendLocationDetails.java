package bcr6.uow.comp548.assignment2.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import bcr6.uow.comp548.assignment2.R;
import bcr6.uow.comp548.assignment2.database.DatabaseHelper;
import bcr6.uow.comp548.assignment2.database.ORMBaseActivity;
import bcr6.uow.comp548.assignment2.models.Friend;

public class FriendLocationDetails extends ORMBaseActivity<DatabaseHelper>
		implements OnMapReadyCallback,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		LocationListener {

	private Friend friend;
	private GoogleApiClient mGoogleApiClient;
	private LatLng friendLocation;
	private LocationRequest mLocationRequest;
	Location mLastLocation;
	private double currentLatitude;
	private double currentLongitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_location_activity);

		populateData();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();

		mLocationRequest = LocationRequest.create()
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
				.setInterval(10 * 1000)        // 10 seconds, in milliseconds
				.setFastestInterval(1000); // 1 second, in milliseconds

		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.friend_map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLatitude = location.getLatitude();
		currentLongitude = location.getLongitude();

		updateDistance(friendLocation.latitude, friendLocation.longitude, currentLatitude, currentLongitude);

	}

	private void updateDistance(double friendLat, double friendLng, double currLat, double currLng) {
		float[] result = new float[2];
		Location.distanceBetween(friendLat, friendLng, currLat, currLng, result);

		TextView textView = (TextView) findViewById(R.id.latlng);
		textView.setText((result[0]) + "");
	}


	@Override
	public void onResume() {
		super.onResume();
		//Now lets connect to the API
		mGoogleApiClient.connect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(this.getClass().getSimpleName(), "onPause()");

		//Disconnect from API onPause()
		if (mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
			mGoogleApiClient.disconnect();
		}


	}

	@Override
	protected void onStart() {
		mGoogleApiClient.connect();
		super.onStart();
	}

	@Override
	protected void onStop() {
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult result) {

	}

	@Override
	public void onConnectionSuspended(int i) {
	}

	@Override
	public void onConnected(Bundle bundle) {

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		if (location == null) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

		} else {
			//If everything went fine lets get latitude and longitude
			currentLatitude = location.getLatitude();
			currentLongitude = location.getLongitude();

			updateDistance(friendLocation.latitude, friendLocation.longitude, currentLatitude, currentLongitude);
		}
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
		String address = friend.getAddress();
		Geocoder geocoder = new Geocoder(this);

		List<Address> addressList;

		try {

			addressList = geocoder.getFromLocationName(address, 5);
			Address location = addressList.get(0);
			friendLocation = new LatLng(location.getLatitude(), location.getLongitude());

			googleMap.addMarker(new MarkerOptions().position(friendLocation).title(address));
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLocation, 13));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void populateData() {
        int friendID;
        String errorMessage = "No extra was passed through to this activity.. How did we get here?";
        if (getIntent().hasExtra("friendID"))
            friendID = getIntent().getIntExtra("friendID", 0);
        else
            throw new IllegalArgumentException(errorMessage);

        friend = getHelper().getFriendDataDao().queryForId(friendID);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(friend.getOneName() + "'s location details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView addressTV = (TextView) findViewById(R.id.friend_location_address);
        addressTV.setText(friend.getAddress());
	}
}
