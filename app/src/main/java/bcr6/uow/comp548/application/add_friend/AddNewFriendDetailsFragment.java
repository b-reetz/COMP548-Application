package bcr6.uow.comp548.application.add_friend;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bcr6.uow.comp548.application.R;

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
    public static AddNewFriendDetailsFragment newInstance() {
        AddNewFriendDetailsFragment fragment = new AddNewFriendDetailsFragment();
        fragment.setArguments(new Bundle());
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	    return inflater.inflate(R.layout.add_friend_details, container, false);
    }

}
