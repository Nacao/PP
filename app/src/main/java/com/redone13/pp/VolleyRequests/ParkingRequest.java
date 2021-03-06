package com.redone13.pp.VolleyRequests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ParkingRequest extends StringRequest {
    //private static final String PARKING_ID_REQUEST = "http://10.127.127.1/parkplatz/db/ParkingRequest.php";
    private static final String PARKING_ID_REQUEST = "http://redone13.net16.net/ParkingRequest.php";
    private Map<String, String> params;

    public ParkingRequest(String employee_email, Response.Listener<String> listener){
        super(Request.Method.POST, PARKING_ID_REQUEST, listener, null);
        params = new HashMap<>();
        params.put("employee_email", employee_email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
