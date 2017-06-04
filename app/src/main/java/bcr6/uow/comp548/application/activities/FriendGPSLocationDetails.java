package bcr6.uow.comp548.application.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

import bcr6.uow.comp548.application.R;
import bcr6.uow.comp548.application.database.DatabaseHelper;
import bcr6.uow.comp548.application.database.ORMBaseActivity;
import bcr6.uow.comp548.application.models.Friend;


public class FriendGPSLocationDetails extends ORMBaseActivity<DatabaseHelper>
		implements OnMapReadyCallback,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		android.location.LocationListener {

	private Friend friend;
	private GoogleApiClient mGoogleApiClient;
	private GoogleMap map;
	private LocationManager locationManager;
	private double currentLatitude;
	private double currentLongitude;
	private Polyline line;
	private Marker currMarker;

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

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.friend_map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		currentLatitude = location.getLatitude();
		currentLongitude = location.getLongitude();

		updatePolyLine();
		updateDistance(friend.getLat(), friend.getLng(), currentLatitude, currentLongitude);

	}

	private void updateDistance(double friendLat, double friendLng, double currLat, double currLng) {
		if (currMarker == null)
			currMarker = map.addMarker(new MarkerOptions().position(new LatLng(currLat, currLng)).title("Current location"));
		currMarker.setPosition(new LatLng(currLat, currLng));

		float[] result = new float[2];
		Location.distanceBetween(friendLat, friendLng, currLat, currLng, result);

		TextView textView = (TextView) findViewById(R.id.distance);
		String distance = ((int)result[0]) + " meters";

		textView.setText(distance);
	}


	@Override
	public void onResume() {
		super.onResume();

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
			Toast.makeText(this, "This activity requires the permission to access the device's location", Toast.LENGTH_LONG).show();
			finish();
		} else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(this, "This activity requires location services to be turned on", Toast.LENGTH_LONG).show();
			finish();
		}
		//Now lets connect to the API
		mGoogleApiClient.connect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(this.getClass().getSimpleName(), "onPause()");

		//Disconnect from API onPause()
		if (mGoogleApiClient.isConnected()) {
			locationManager.removeUpdates(this);
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
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Toast.makeText(this, "The.. status has been changed?", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Provider has been ENABLED", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Provider has been disabled", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onConnected(Bundle bundle) {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			return;

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

		Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (loc != null) {
			currentLatitude = loc.getLatitude();
			currentLongitude = loc.getLongitude();

			updateDistance(friend.getLat(), friend.getLng(), currentLatitude, currentLongitude);
			updatePolyLine();
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
		map = googleMap;

		LatLng friendLatLng = friend.getLatLng();

		//If friend has an address in the field but didn't add contact (importing .vcf?)
		if (!friend.getAddress().isEmpty() && friend.getLatLng() == null) {
			Geocoder geocoder = new Geocoder(this);
			try {
				List<Address> addressList = geocoder.getFromLocationName(friend.getAddress(), 1);
				//If no address found from imported contact's address field
				if (addressList.isEmpty()) {
					Toast.makeText(this, "Unable to resolve address from contact. Please edit the address in the contact", Toast.LENGTH_LONG).show();
					finish();
				}

				Address address = addressList.get(0);
				friend.setLat(address.getLatitude());
				friend.setLng(address.getLongitude());

				String stringAddress = "";

				for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
					stringAddress += address.getAddressLine(i);

				friendLatLng = friend.getLatLng();
				map.addMarker(new MarkerOptions().position(friendLatLng).title(stringAddress));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			map.addMarker(new MarkerOptions().position(friendLatLng).title(friend.getAddress()));


		map.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLatLng, 13));

		updatePolyLine();

	}

	private void updatePolyLine() {
		if (currentLongitude != 0 && currentLatitude != 0)
			map.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBounds.builder().include(friend.getLatLng()).include(new LatLng(currentLatitude, currentLongitude)).build(), 150));
		else if (currentLongitude == 0 && currentLatitude == 0)
			return;


		if (line != null)
			line.remove();

		line = map.addPolyline(new PolylineOptions().add(friend.getLatLng(), new LatLng(currentLatitude, currentLongitude)));
	}

	private void populateData() {
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
