package com.redone13.pp.mFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.redone13.pp.Employee;
import com.redone13.pp.EmployeeRequest;
import com.redone13.pp.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmployeeFragment extends Fragment {
    private static final String TAG = EmployeeFragment.class.getSimpleName();
    public static final String EMPLOYEE_FRAGMENT = "employee_fragment";
    public static final String EMPLOYEE_FRAGMENT_EMAIL = "employee_fragment_email";
    private Employee mEmployee;
    private String mEmail;

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.employeeFragmentLastName) TextView lastName;
    @BindView(R.id.employeeFragmentFirstName) TextView firstName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mEmail = getArguments().getString(EMPLOYEE_FRAGMENT_EMAIL);
        View view = inflater.inflate(R.layout.fragment_employee, container, false);
        ButterKnife.bind(this, view);

        getEmployeeFullName();

        return view;
    }

    private void getEmployeeFullName() {
        progressBar.setVisibility(View.VISIBLE);
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
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void updateDisplay() {
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
