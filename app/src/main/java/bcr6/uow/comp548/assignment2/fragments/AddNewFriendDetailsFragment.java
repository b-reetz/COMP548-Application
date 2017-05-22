package bcr6.uow.comp548.assignment2.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;

import bcr6.uow.comp548.assignment2.R;
import bcr6.uow.comp548.assignment2.activities.AddNewFriend;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link AddNewFriendDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewFriendDetailsFragment extends Fragment {

	Place place;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddNewFriendDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewFriendDetailsFragment newInstance() {
        AddNewFriendDetailsFragment fragment = new AddNewFriendDetailsFragment();
        fragment.setArguments(new Bundle());
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.add_new_friend_details, container, false);

	    /*PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
			    getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

	    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
		    @Override
		    public void onPlaceSelected(Place place) {
			    transferPlace(place);
			    Log.i("OMG", "Place: " + place.getName());
		    }

		    @Override
		    public void onError(Status status) {
			    // TODO: Handle the error.
			    Log.i("OMG", "An error occurred: " + status);
		    }
	    });*/

/*	    TextView tV = (TextView) view.findViewById(R.id.add_new_friend_details_address_edit_text);

	    tV.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {

		    }
	    });*/

	    return view;
    }

    public void transferPlace(Place place) {
	    ((AddNewFriend)getActivity()).setPlace(place);
    }


}
