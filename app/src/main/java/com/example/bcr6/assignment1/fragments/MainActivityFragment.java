package com.example.bcr6.assignment1.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bcr6.assignment1.MainActivity;
import com.example.bcr6.assignment1.R;
import com.example.bcr6.assignment1.adaptors.MainFragmentAdaptor;

/**
 * Created by bcr6 on 3/2/17.
 *
 * Fragment to look at the friends saved within the apps internal storage
 */

public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {}

    RecyclerView recyclerView;
    MainFragmentAdaptor mainFragmentAdaptor;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        layoutManager = new LinearLayoutManager(getActivity());

        mainFragmentAdaptor = new MainFragmentAdaptor();
        recyclerView.setAdapter(mainFragmentAdaptor);
        return view;
    }

}
