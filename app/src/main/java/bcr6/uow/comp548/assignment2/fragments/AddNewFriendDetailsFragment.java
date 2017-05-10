package bcr6.uow.comp548.assignment2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import bcr6.uow.comp548.assignment2.R;
import bcr6.uow.comp548.assignment2.activities.AddNewFriend;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link AddNewFriendDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewFriendDetailsFragment extends Fragment {

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
        final View view = inflater.inflate(R.layout.add_new_friend_details, container, false);

        EditText address = (EditText) view.findViewById(R.id.add_new_friend_details_address_edit_text);
/*        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, AddNewFriend.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (Exception e) {
                    // TODO: Handle the error.
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddNewFriend)getContext()).launchAutoComplete();
            }
        });

        return view;
    }

}
