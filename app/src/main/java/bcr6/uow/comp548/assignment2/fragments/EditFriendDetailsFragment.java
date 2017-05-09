package bcr6.uow.comp548.assignment2.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import bcr6.uow.comp548.assignment2.R;
import bcr6.uow.comp548.assignment2.models.Friend;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link EditFriendDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFriendDetailsFragment extends Fragment {

    private Friend friend;

    public EditFriendDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddNewFriendDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        firstNameEdit.setText(friend.getFirstName());
        lastNameEdit.setText(friend.getLastName());
        mobileEdit.setText(friend.getMobileNumber());
        emailEdit.setText(friend.getEmailAddress());
        addressEdit.setText(friend.getAddress());

        return v;
    }

    public void setFriend(Friend f) {
        this.friend = f;
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
        }
        return friend;
    }

}
