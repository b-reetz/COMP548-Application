package com.example.bcr6.assignment1.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.bcr6.assignment1.R;
import com.example.bcr6.assignment1.activities.AddNewFriend;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewFriendDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewFriendDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewFriendDetailsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public AddNewFriendDetailsFragment() {
        // Required empty public constructor
    }

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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_friend_details_fragment, container, false);
        EditText firstName = (EditText) container.findViewById(R.id.add_new_friend_details_first_name_edit_text);
        EditText lastName = (EditText) container.findViewById(R.id.add_new_friend_details_last_name_edit_text);
        EditText mobileNumber = (EditText) container.findViewById(R.id.add_new_friend_details_mobile_number_edit_text);
        EditText emailAddress = (EditText) container.findViewById(R.id.add_new_friend_details_email_edit_text);
        EditText address = (EditText) container.findViewById(R.id.add_new_friend_details_address_edit_text);

        firstName.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        if (savedInstanceState != null && savedInstanceState.containsKey(AddNewFriend.FIRST_NAME)) {
            firstName.setText(savedInstanceState.getString(AddNewFriend.FIRST_NAME));
            lastName.setText(savedInstanceState.getString(AddNewFriend.LAST_NAME));
            mobileNumber.setText(savedInstanceState.getString(AddNewFriend.MOBILE_NUMBER));
            emailAddress.setText(savedInstanceState.getString(AddNewFriend.EMAIL_ADDRESS));
            address.setText(savedInstanceState.getString(AddNewFriend.ADDRESS));
        }
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
