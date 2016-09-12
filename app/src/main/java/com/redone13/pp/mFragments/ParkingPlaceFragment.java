package com.redone13.pp.mFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.redone13.pp.Parking;
import com.redone13.pp.R;
import com.redone13.pp.VolleyRequests.ParkingRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParkingPlaceFragment extends Fragment {
    private static final String TAG = ParkingPlaceFragment.class.getSimpleName();
    public static final String PARKING_PLACE_FRAGMENT = "parking_place_fragment";

    @BindView(R.id.parkingPlaceId) TextView mParkingPlaceId;
    @BindView(R.id.releaseParkingPlace) Button mReleaseParkingPlace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_place, container, false);
        getActivity().setTitle("Parking Details");
        ButterKnife.bind(this, view);

        getEmployeeParkingPlaceId();

        return view;
    }

    private void getEmployeeParkingPlaceId() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "jsonObject: " + response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        Parking mParking = getParkingDetails(jsonResponse);
                        updateDisplay(mParking.getId());
                    }else {
                        updateDisplay();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        sendParkingRequest(responseListener);
    }

    private void sendParkingRequest(Response.Listener<String> responseListener) {
        ParkingRequest parkingRequest = new ParkingRequest("1", responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(parkingRequest);
    }

    @NonNull
    private Parking getParkingDetails(JSONObject jsonResponse) throws JSONException {
        JSONObject parkingDetails = jsonResponse.getJSONObject("parking_details");
        Parking mParking = new Parking();
        mParking.setId(parkingDetails.getInt("id"));
        mParking.setName(parkingDetails.getString("name"));
        return mParking;
    }

    private void updateDisplay() {
        mReleaseParkingPlace.setVisibility(View.INVISIBLE);
        mParkingPlaceId.setTextSize(16f);
        mParkingPlaceId.setText("Sorry, du hast heute kein Parkplatz");
    }

    private void updateDisplay(int id) {
        String stringId = String.format("%02d", id);
        mParkingPlaceId.setText(stringId);
    }
}
