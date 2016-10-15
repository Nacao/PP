package com.redone13.pp.mFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.redone13.pp.Employee;
import com.redone13.pp.RegisterActivity;
import com.redone13.pp.VolleyRequests.EmployeeRequest;
import com.redone13.pp.R;
import com.redone13.pp.VolleyRequests.TokenRegister;
import com.redone13.pp.fcm.FirebaseInstanceIDService;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeeFragment extends Fragment {
    private static final String TAG = EmployeeFragment.class.getSimpleName();
    public static final String EMPLOYEE_FRAGMENT = "employee_fragment";
    public static final String EMPLOYEE_FRAGMENT_EMAIL = "employee_fragment_email";
    private Employee mEmployee;
    private String mEmail;

    @BindView(R.id.progressBarLastName) ProgressBar progressBarLastName;
    @BindView(R.id.progressBarFirstName) ProgressBar progressBarFirstName;
    @BindView(R.id.employeeFragmentLastName) TextView lastName;
    @BindView(R.id.employeeFragmentFirstName) TextView firstName;
    @BindView(R.id.fcm_register) Button fcmRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee, container, false);
        mEmail = getArguments().getString(EMPLOYEE_FRAGMENT_EMAIL);
        getActivity().setTitle("User Details");
        ButterKnife.bind(this, view);

        getEmployeeFullName();

        return view;
    }

    @OnClick(R.id.fcm_register)
    public void fcmRegister(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, token);
        Log.d(TAG, mEmployee.getEmail());
        // Update token for user in db
        registerToken(mEmployee.getEmail(), token);
    }

    private void registerToken(String email, String token) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG, "response" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "response: " + jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Token Fallo!");
                }
            }
        };

        TokenRegister tokenRegister = new TokenRegister(email, token, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(tokenRegister);
    }

    private void getEmployeeFullName() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "La respuesta del servidor: " + response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    getCurrentEmployee(jsonResponse);
                    updateDisplay();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        EmployeeRequest employeeRequest = new EmployeeRequest(mEmail, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(employeeRequest);
    }

    private void updateDisplay() {
        progressBarLastName.setVisibility(View.INVISIBLE);
        progressBarFirstName.setVisibility(View.INVISIBLE);
        lastName.setVisibility(View.VISIBLE);
        firstName.setVisibility(View.VISIBLE);
        lastName.setText(mEmployee.getLastName());
        firstName.setText(mEmployee.getFirstName());
    }

    private void getCurrentEmployee(JSONObject jsonResponse) throws JSONException {
        mEmployee = new Employee();
        mEmployee.setEmail(mEmail);
        mEmployee.setLastName(jsonResponse.getString("last_name"));
        mEmployee.setFirstName(jsonResponse.getString("first_name"));
    }
}
