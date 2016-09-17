package com.redone13.pp.VolleyRequests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TokenRegister extends StringRequest {
    private static final String TOKEN_REGISTER = "http://10.127.127.1/parkplatz/db/TokenRegister.php";
    //private static final String TOKEN_REGISTER = "http://redone13.net16.net/ParkingRequest.php";
    private Map<String, String> params;

    public TokenRegister(String employee_email, String token, Response.Listener<String> listener){
        super(Request.Method.POST, TOKEN_REGISTER, listener, null);
        params = new HashMap<>();
        params.put("employee_email", employee_email);
        params.put("token", token);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
