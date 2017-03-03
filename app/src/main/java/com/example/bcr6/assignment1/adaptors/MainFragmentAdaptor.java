package com.example.bcr6.assignment1.adaptors;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bcr6.assignment1.R;

/**
 * Created by bcr6 on 3/2/17.
 * Used to display the custom card view going to be on display
 */

public class MainFragmentAdaptor extends RecyclerView.Adapter<MainFragmentAdaptor.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        private final TextView name;
        private final ImageView image;

        private final Context c = itemView.getContext();

        ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.main_card_view);

            cv.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            name = (TextView) itemView.findViewById(R.id.main_card_view_friend_name);
            image = (ImageView) itemView.findViewById(R.id.main_card_view_image);

        }

        public void setName(String s) {
            name.setText(s);
        }

        public void setImage(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }

    public MainFragmentAdaptor() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View cv = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_view, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        holder.setName(i+"");
//        holder.setImage(<insertBitmap>);
    }
}
