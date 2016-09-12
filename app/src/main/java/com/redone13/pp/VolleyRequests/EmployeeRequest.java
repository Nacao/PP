package com.redone13.pp.VolleyRequests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EmployeeRequest extends StringRequest {
    //private static final String EMPLOYEE_REQUEST_URL = "http://10.127.127.1/parkplatz/db/dbEmployee.php";
    private static final String EMPLOYEE_REQUEST_URL = "http://redone13.net16.net/EmployeeRequest.php";
    private Map<String, String> params;

    public EmployeeRequest(String email, Response.Listener<String> listener) {
        super(Request.Method.POST, EMPLOYEE_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
