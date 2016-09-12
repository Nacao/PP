package com.redone13.pp;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.redone13.pp.mFragments.EmployeeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    public static final String REDLEE = "rollal";
    public static boolean stay_logged = false;

    @BindView(R.id.b_login) Button mLoggingButton;
    @BindView(R.id.b_register) Button mRegisterButton;
    @BindView(R.id.cb_stay_logged) CheckBox mStayLoggedCheckBox;
    @BindView(R.id.et_email) EditText mEmail;
    @BindView(R.id.et_password) EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        restorePreferences();

        mStayLoggedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                stay_logged = mStayLoggedCheckBox.isChecked();
            }
        });

        // When login button pressed then:
        mLoggingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logEmployee();
            }
        });

        // When register button pressed then:
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText et_email = (EditText) findViewById(R.id.et_email);

                // Save user data to shared preferences
                SharedPreferences settings = getSharedPreferences(REDLEE, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("user_email", et_email.getText().toString());
                editor.commit();

                startRegistration();
            }
        });
    }

    private void startRegistration() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.putExtra("email", mEmail.getText().toString());
        if(Build.VERSION.SDK_INT >= 21) {
            Slide slide = new Slide();
            slide.setDuration(3000);
            slide.setMode(Visibility.MODE_OUT);
            getWindow().setExitTransition(slide);
            startActivity(registerIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }else {
            startActivity(registerIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void restorePreferences() {
        SharedPreferences settings = getSharedPreferences(REDLEE, MODE_PRIVATE);
        mStayLoggedCheckBox.setChecked(settings.getBoolean("stayLogged", true));
        mEmail.setText(settings.getString("user_email", null));
    }

    private void logEmployee() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Integer error_code = jsonResponse.getInt("error_code");

                    if(error_code == 0) {

                        if(mStayLoggedCheckBox.isChecked()) {
                            // Save user data to shared preferences
                            SharedPreferences settings = getSharedPreferences(REDLEE, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("user_email", mEmail.getText().toString());
                            editor.commit();
                        }

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(EmployeeFragment.EMPLOYEE_FRAGMENT_EMAIL, mEmail.getText().toString());
                        LoginActivity.this.startActivity(intent);
                    }else {
                        AlertDialog.Builder dialogNotConnected = new AlertDialog.Builder(LoginActivity.this);
                        dialogNotConnected.setMessage("Password or email invalid. Please verify them")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(mEmail.getText().toString(), mPassword.getText().toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(REDLEE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("stayLogged", stay_logged);
        editor.commit();
    }
}
