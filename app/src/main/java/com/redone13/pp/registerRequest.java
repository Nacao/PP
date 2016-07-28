package com.redone13.pp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://10.127.127.1/parkplatz/dbRegister.php";
    //public static final String REGISTER_REQUEST_URL = "http://www.redone13.net16.net/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String last_name, String first_name, String email, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("last_name", last_name);
        params.put("first_name", first_name);
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}