package com.redone13.pp;

import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();
    public static final String REDLEE = "rollal";
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText et_last_name = (EditText) findViewById(R.id.et_lastname);
        final EditText et_first_name = (EditText) findViewById(R.id.et_firstname);
        final EditText et_email = (EditText) findViewById(R.id.et_email);
        final EditText et_password = (EditText) findViewById(R.id.et_password);
        final Button b_register = (Button) findViewById(R.id.b_register);
        final CheckBox cb_fixed_parking = (CheckBox) findViewById(R.id.cb_fixed_parking);
        final EditText et_parking_name = (EditText) findViewById(R.id.et_parking_name);
        final EditText et_first_candidate = (EditText) findViewById(R.id.et_first_candidate);
        final EditText et_second_canditate = (EditText) findViewById(R.id.et_second_candidate);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(REDLEE, 0);
        et_email.setText(settings.getString("user_email", null));

        // Set fixed parking check box as false
        cb_fixed_parking.setChecked(false);

        // Make all parking information invisible
        et_parking_name.setVisibility(View.INVISIBLE);
        et_first_candidate.setVisibility(View.INVISIBLE);
        et_second_canditate.setVisibility(View.INVISIBLE);

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

        cb_fixed_parking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb_fixed_parking.isChecked()) {
                    // Make all parking information visible
                    et_parking_name.setVisibility(View.VISIBLE);
                    et_first_candidate.setVisibility(View.VISIBLE);
                    et_second_canditate.setVisibility(View.VISIBLE);
                }else {
                    // Make all parking information invisible
                    et_parking_name.setVisibility(View.INVISIBLE);
                    et_first_candidate.setVisibility(View.INVISIBLE);
                    et_second_canditate.setVisibility(View.INVISIBLE);
                }
            }
        });

        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String last_name = et_last_name.getText().toString();
                final String first_name = et_first_name.getText().toString();
                final String email = et_email.getText().toString();
                final String password = et_password.getText().toString();
                final String parking_name = et_parking_name.getText().toString();

                if(!last_name.matches("") && !first_name.matches("") && !email.matches("") && !password.matches("")) {
                    Log.d(TAG, "después del if, antes del metodo");

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "dentro del metodo, antes del try");
                            Log.d(TAG, response);
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                Integer error_code = jsonResponse.getInt("code");
                                String err_msg = jsonResponse.getString("err_msg");
                                Log.d(TAG, String.valueOf(success));
                                Log.d(TAG, String.valueOf(error_code));
                                Log.d(TAG, err_msg);

                                if(success && error_code == 0) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    RegisterActivity.this.startActivity(intent);
                                }
                                else if (!success && error_code == -1) {
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

                RegisterRequest registerRequest = new RegisterRequest(last_name, first_name, email, password, parking_name, responseListener);
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
}
