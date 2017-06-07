package bcr6.uow.comp548.application.adaptors;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import bcr6.uow.comp548.application.R;
import bcr6.uow.comp548.application.fragments.MainFragment.OnListFragmentInteractionListener;
import bcr6.uow.comp548.application.models.Friend;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static bcr6.uow.comp548.application.activities.Settings.SORT_A_Z;
import static bcr6.uow.comp548.application.activities.Settings.SORT_BY;
import static bcr6.uow.comp548.application.activities.Settings.SORT_FIRST;
import static bcr6.uow.comp548.application.activities.Settings.SORT_LAST;
import static bcr6.uow.comp548.application.activities.Settings.SORT_ORDER;
import static bcr6.uow.comp548.application.activities.Settings.SORT_Z_A;


public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.FriendViewHolder> implements SharedPreferences.OnSharedPreferenceChangeListener {

    private List<Friend> friends;
    private final OnListFragmentInteractionListener mListener;

    public MainRecyclerViewAdapter(List<Friend> friends, OnListFragmentInteractionListener listener) {
        this.friends = friends;
        mListener = listener;
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        final CircleImageView contactPhoto;
        final TextView contactName;
        public final View item;
        int contactID;

        FriendViewHolder(View view) {
            super(view);
            contactPhoto = (CircleImageView) view.findViewById(R.id.main_contact_image);
            contactName = (TextView) view.findViewById(R.id.main_contact_name);
            item = view.findViewById(R.id.main_item_container);

        }
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_item, viewGroup, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FriendViewHolder holder, int position) {
        Friend f = friends.get(position);
        holder.contactID = f.getId();
        holder.contactName.setText(f.getFirstName() +" "+ f.getLastName());
        if (!f.getImagePath().isEmpty())
            Picasso.with(holder.contactPhoto.getContext()).load("file://+"+f.getImagePath()).resize(400, 400).noFade().into(holder.contactPhoto);
	    else
	    	holder.contactPhoto.setImageResource(R.drawable.ic_person_black_24dp);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.contactID);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        int sort_by = Integer.parseInt(sharedPreferences.getString(SORT_BY, SORT_FIRST+""));
        int sort_order = Integer.parseInt(sharedPreferences.getString(SORT_ORDER, SORT_A_Z+""));

        updateFriends(sort_by, sort_order);
        notifyDataSetChanged();
    }

    /**
     *
     * @param sort_by Sort by first or last name (1 or 2)
     * @param sort_order Sort by A-Z or Z-A (1 or 2)
     */
    private void updateFriends(int sort_by, int sort_order) {

        //Sorts it by first name a-z
        if (sort_by == SORT_FIRST && sort_order == SORT_A_Z)
            Collections.sort(friends, new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return o1.getFirstName().compareTo(o2.getFirstName());
                }
            });
            //Sorts it by first name z-a
        else if (sort_by == SORT_FIRST && sort_order == SORT_Z_A)
            Collections.sort(friends, new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return -o1.getFirstName().compareTo(o2.getFirstName());
                }
            });
            //Sorts list by last name a-z
        else if (sort_by == SORT_LAST && sort_order == SORT_A_Z)
            Collections.sort(friends, new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return o1.getLastName().compareTo(o2.getLastName());
                }
            });
            //Sorts list by last name z-a
        else if (sort_by == SORT_LAST && sort_order == SORT_Z_A)
            Collections.sort(friends, new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return -o1.getLastName().compareTo(o2.getLastName());
                }
            });
    }

	public void updateList(List<Friend> list){
		friends = list;
		notifyDataSetChanged();
	}
}
