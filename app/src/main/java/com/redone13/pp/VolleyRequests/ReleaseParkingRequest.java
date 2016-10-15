package com.redone13.pp.VolleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReleaseParkingRequest extends StringRequest {
    //public static final String RELEASE_PARKING_REQUEST_URL = "http://10.127.127.1/parkplatz/db/ReleaseParkingRequest.php";
    public static final String RELEASE_PARKING_REQUEST_URL = "http://redone13.net16.net/ParkingRequest.php";
    private Map<String, String> params;

    public ReleaseParkingRequest(String email, Response.Listener<String> listener) {
        super(Method.POST, RELEASE_PARKING_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("employee_email", email);
    }

    public Map<String, String> getParams() {
        return params;
    }
}