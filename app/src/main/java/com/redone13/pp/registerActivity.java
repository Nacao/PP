package com.redone13.pp;

import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();
    public static final String REDLEE = "rollal";
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @BindView(R.id.et_lastname) EditText mLastName;
    @BindView(R.id.et_firstname) EditText mFirstName;
    @BindView(R.id.et_email) EditText mEmail;
    @BindView(R.id.et_password) EditText mPassword;
    @BindView(R.id.b_register) Button mRegisterButton;
    @BindView(R.id.cb_fixed_parking) CheckBox cb_fixed_parking;
    @BindView(R.id.et_parking_name) EditText et_parking_id;
    @BindView(R.id.et_first_candidate) EditText mFirstCandidateEmail;
    @BindView(R.id.et_second_candidate) EditText mSecondCandidateEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(REDLEE, 0);
        mEmail.setText(settings.getString("user_email", null));

        // Set fixed parking check box as false
        cb_fixed_parking.setChecked(false);

        // Make all parking information invisible
        setExtendedRegistrationVisibility(View.INVISIBLE);

        spinner = (Spinner)findViewById(R.id.sp_employment_place);
        adapter = ArrayAdapter.createFromResource(this,R.array.employment_places,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+" selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enableExtendedRegistration();

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String last_name = mLastName.getText().toString();
                final String first_name = mFirstName.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String ownsParkingPlace = cb_fixed_parking.isChecked() + "";
                final String parking_id = et_parking_id.getText().toString();
                final String firstCandidateEmail = mFirstCandidateEmail.getText().toString();
                final String secondCandidateEmail = mSecondCandidateEmail.getText().toString();

                if(!last_name.matches("") && !first_name.matches("") && !email.matches("") && !password.matches("")) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "jsonResponse: " + response);
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                Integer err_code = jsonResponse.getInt("err_code");
                                String err_msg = jsonResponse.getString("err_msg");
                                Log.d(TAG, String.valueOf(success));
                                Log.d(TAG, String.valueOf(err_code));
                                Log.d(TAG, err_msg);

                                if(success && err_code == 0) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    RegisterActivity.this.startActivity(intent);
                                }
                                else if (!success && err_code == -1) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("The email account: " + email + " is already " +
                                            "registered. Please try again with a different email account.")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage("Register Failed. " + err_msg)
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                };

                RegisterRequest registerRequest = new RegisterRequest(last_name, first_name, email,
                        password, ownsParkingPlace, parking_id, firstCandidateEmail, secondCandidateEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }else {
                    AlertDialog.Builder emptyValuesAlert = new AlertDialog.Builder(RegisterActivity.this);
                    emptyValuesAlert.setMessage("One of the entry values is empty. Please verify them again!")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }
        }});
    }

    private void enableExtendedRegistration() {
        cb_fixed_parking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb_fixed_parking.isChecked()) {
                    setExtendedRegistrationVisibility(View.VISIBLE);
                }else {
                    setExtendedRegistrationVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setExtendedRegistrationVisibility(int visibility) {
        et_parking_id.setVisibility(visibility);
        mFirstCandidateEmail.setVisibility(visibility);
        mSecondCandidateEmail.setVisibility(visibility);
    }

}
