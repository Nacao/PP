package com.redone13.pp.mFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redone13.pp.R;

public class ParkingPlaceFragment extends Fragment {
    public static final String PARKING_PLACE_FRAGMENT = "parking_place_fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_place, container, false);
        return view;
    }
}
