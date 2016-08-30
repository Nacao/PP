package com.redone13.pp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //private static final String REGISTER_REQUEST_URL = "http://10.127.127.1/parkplatz/dbRegister.php";
    private static final String REGISTER_REQUEST_URL = "http://redone13.net16.net/dbRegister.php";
    private Map<String, String> params;

    public RegisterRequest(String last_name, String first_name, String email, String password, String parking_name,  Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("last_name", last_name);
        params.put("first_name", first_name);
        params.put("email", email);
        params.put("password", password);
        params.put("parking_name", parking_name);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
