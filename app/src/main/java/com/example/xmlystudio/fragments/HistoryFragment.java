package com.example.xmlystudio.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xmlystudio.R;
import com.example.xmlystudio.base.BaseFragment;

public class HistoryFragment extends BaseFragment {

    private View view;

    @Override
    protected View onSubViewLoaded(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_history, container,false);

        return view;
    }
}
