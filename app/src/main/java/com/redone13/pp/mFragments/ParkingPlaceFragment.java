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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.redone13.pp.MainActivity;
import com.redone13.pp.Parking;
import com.redone13.pp.R;
import com.redone13.pp.VolleyRequests.ParkingRequest;
import com.redone13.pp.VolleyRequests.ReleaseParkingRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParkingPlaceFragment extends Fragment {
    private static final String TAG = ParkingPlaceFragment.class.getSimpleName();
    public static final String PARKING_PLACE_FRAGMENT = "parking_place_fragment";

    @BindView(R.id.parkingPlaceId) TextView mParkingPlaceId;
    @BindView(R.id.releaseParkingPlace) Button mReleaseParkingPlace;
    @BindView(R.id.progressBarParkingPlace) ProgressBar mProgressBarParkingPlace;

    private String mEmployeeEmail;
    private Parking mParking;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parking_place, container, false);
        getActivity().setTitle("Parking Details");
        mEmployeeEmail = getArguments().getString(MainActivity.EMPLOYEE_EMAIL);
        ButterKnife.bind(this, view);
        mParking = new Parking();

        getEmployeeParkingPlaceId();

        return view;
    }

    @OnClick(R.id.releaseParkingPlace)
    public void onReleaseParkingPlaceClicked(View view) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("TAG", jsonObject.toString());
                    if(jsonObject.getBoolean("success")) {
                        Toast.makeText(getActivity().getApplicationContext(), "Fairy Tail", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ReleaseParkingRequest releaseParkingRequest = new ReleaseParkingRequest(mEmployeeEmail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(releaseParkingRequest);

    }

    private void getEmployeeParkingPlaceId() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.i(TAG, "jsonResponse" + jsonResponse);
                    Boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        mParking = getParkingDetails(jsonResponse);
                    }
                    updateDisplay();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        sendParkingRequest(responseListener);
    }

    private void sendParkingRequest(Response.Listener<String> responseListener) {
        ParkingRequest parkingRequest = new ParkingRequest(mEmployeeEmail, responseListener);
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
        mProgressBarParkingPlace.setVisibility(View.INVISIBLE);
        mParkingPlaceId.setVisibility(View.VISIBLE);
        if(mParking != null) {
            mReleaseParkingPlace.setVisibility(View.VISIBLE);
            String stringId = String.format("%02d", mParking.getId());
            mParkingPlaceId.setText(stringId);
        }else {
            mParkingPlaceId.setTextSize(16f);
            mParkingPlaceId.setText(R.string.no_parking_place);
        }
    }

}
