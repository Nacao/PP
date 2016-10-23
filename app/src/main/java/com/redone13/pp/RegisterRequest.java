package com.redone13.pp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    //private static final String REGISTER_REQUEST_URL = "http://10.127.127.1/parkplatz/db/dbRegister.php";
    private static final String REGISTER_REQUEST_URL = "http://redone13.net16.net/dbRegister.php";
    private Map<String, String> params;

    public RegisterRequest(String last_name, String first_name, String email, String password,
                           String owns_parking_place , String parking_id,
                           String firstCandidateEmail, String secondCandidateEmail,
                           Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("last_name", last_name);
        params.put("first_name", first_name);
        params.put("email", email);
        params.put("password", password);
        params.put("owns_parking_place", owns_parking_place);
        params.put("parking_id", parking_id);
        params.put("first_candidate_email", firstCandidateEmail);
        params.put("second_candidate_email", secondCandidateEmail);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
