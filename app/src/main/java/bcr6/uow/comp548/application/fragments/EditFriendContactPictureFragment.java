package bcr6.uow.comp548.application.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bcr6.uow.comp548.application.ImageHelper;
import bcr6.uow.comp548.application.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditFriendContactPictureFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditFriendContactPictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFriendContactPictureFragment extends Fragment {
    private OnFragmentInteractionListener listener;
    private String imagePath = "";
    private String tempImagePath = "";

    public EditFriendContactPictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddNewFriendContactPictureFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFriendContactPictureFragment newInstance() {
        EditFriendContactPictureFragment fragment = new EditFriendContactPictureFragment();
        fragment.setArguments(new Bundle());
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.edit_friend_picture, container, false);

        if (!imagePath.isEmpty()) {
            ImageView imageView = (ImageView) view.findViewById(R.id.edit_friend_picture_silhouette);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(ImageHelper.bitmapSmaller(imagePath,
                    200, 200));
        }

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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTempImagePath() {
        return tempImagePath;
    }

    public void setTempImagePath(String tempImagePath) {
        this.tempImagePath = tempImagePath;
    }

    public void removePhoto() {


        if (getView() != null) {
            ImageView iV = (ImageView) getView().findViewById(R.id.edit_friend_picture_silhouette);
            if (iV != null) {
                iV.setScaleType(ImageView.ScaleType.FIT_CENTER);
                iV.setImageResource(R.drawable.ic_person_white_24dp);
            }
        }
        imagePath = "";
    }
}
