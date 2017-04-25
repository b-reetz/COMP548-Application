package com.example.bcr6.assignment1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bcr6.assignment1.ImageHelper;
import com.example.bcr6.assignment1.R;
import com.example.bcr6.assignment1.activities.AddNewFriend;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewFriendContactPictureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewFriendContactPictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewFriendContactPictureFragment extends Fragment {
    private OnFragmentInteractionListener listener;
    private String imagePath = "";

    public AddNewFriendContactPictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddNewFriendContactPictureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewFriendContactPictureFragment newInstance() {
        return new AddNewFriendContactPictureFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_new_friend_picture, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFragmentInteraction();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imagePath = ((AddNewFriend)getActivity()).getImagePath();
        if (!imagePath.isEmpty()) {
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.add_new_friend_picture_silhouette);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(ImageHelper.bitmapSmaller(imagePath,
                    200, 200));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
        void onFragmentInteraction();
    }
}
