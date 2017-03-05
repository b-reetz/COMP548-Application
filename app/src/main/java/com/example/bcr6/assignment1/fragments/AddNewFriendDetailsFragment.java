package com.example.bcr6.assignment1.fragments;

import android.app.Activity;
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
 * Use the {@link AddNewFriendDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewFriendDetailsFragment extends Fragment {

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
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_new_friend_details_fragment, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
