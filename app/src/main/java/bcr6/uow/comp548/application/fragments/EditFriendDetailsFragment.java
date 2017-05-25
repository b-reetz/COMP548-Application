package bcr6.uow.comp548.application.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import bcr6.uow.comp548.application.R;
import bcr6.uow.comp548.application.models.Friend;

import static android.app.Activity.RESULT_OK;
import static bcr6.uow.comp548.application.activities.AddNewFriend.PLACE_AUTOCOMPLETE_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link EditFriendDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFriendDetailsFragment extends Fragment {

    private Friend friend;
	private LatLng loc;

    public EditFriendDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddNewFriendDetailsFragment.
     */
    public static EditFriendDetailsFragment newInstance() {
        EditFriendDetailsFragment fragment = new EditFriendDetailsFragment();
        fragment.setArguments(new Bundle());
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_friend_details, container, false);

        EditText firstNameEdit = (EditText) v.findViewById(R.id.edit_friend_details_first_name_edit_text);
        EditText lastNameEdit = (EditText) v.findViewById(R.id.edit_friend_details_last_name_edit_text);
        EditText mobileEdit = (EditText) v.findViewById(R.id.edit_friend_details_mobile_number_edit_text);
        EditText emailEdit = (EditText) v.findViewById(R.id.edit_friend_details_email_edit_text);
        EditText addressEdit = (EditText) v.findViewById(R.id.edit_friend_details_address_edit_text);

	    addressEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
			    if (hasFocus) {
				    try {
					    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build((Activity)v.getContext());
					    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
					    v.clearFocus();
				    } catch (GooglePlayServicesRepairableException e) {
					    e.printStackTrace();
				    } catch (GooglePlayServicesNotAvailableException e) {
					    Toast.makeText(v.getContext(), "Unable to contact Google", Toast.LENGTH_LONG).show();
					    e.printStackTrace();
				    }
			    }
		    }
	    });

        firstNameEdit.setText(friend.getFirstName());
        lastNameEdit.setText(friend.getLastName());
        mobileEdit.setText(friend.getMobileNumber());
        emailEdit.setText(friend.getEmailAddress());
        addressEdit.setText(friend.getAddress());

        return v;
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Place place = PlaceAutocomplete.getPlace(this.getActivity(), data);
				loc = place.getLatLng();

				TextView textView = (TextView) this.getActivity().findViewById(R.id.edit_friend_details_address_edit_text);
				textView.setText(place.getAddress());

				Log.i("Place", "Place: " + place.getName());
			} else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
				Status status = PlaceAutocomplete.getStatus(this.getActivity(), data);
				Log.i("Place", status.getStatusMessage());

			}
		}
	}

    public void setFriend(Friend f) {
        this.friend = f;
	    loc = f.getLatLng();
    }

    public Friend getUpdatedFriend() {

        if (getView() != null) {
            EditText firstNameEdit = (EditText) getView().findViewById(R.id.edit_friend_details_first_name_edit_text);
            EditText lastNameEdit = (EditText) getView().findViewById(R.id.edit_friend_details_last_name_edit_text);
            EditText mobileEdit = (EditText) getView().findViewById(R.id.edit_friend_details_mobile_number_edit_text);
            EditText emailEdit = (EditText) getView().findViewById(R.id.edit_friend_details_email_edit_text);
            EditText addressEdit = (EditText) getView().findViewById(R.id.edit_friend_details_address_edit_text);


            friend.setFirstName(firstNameEdit.getText().toString());
            friend.setLastName(lastNameEdit.getText().toString());
            friend.setMobileNumber(mobileEdit.getText().toString());
            friend.setEmailAddress(emailEdit.getText().toString());
            friend.setAddress(addressEdit.getText().toString());
	        friend.setLatLng(loc);
        }
        return friend;
    }

}
