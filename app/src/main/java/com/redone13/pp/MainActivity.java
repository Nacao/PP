package com.redone13.pp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.redone13.pp.VolleyRequests.TokenRegister;
import com.redone13.pp.fcm.FirebaseInstanceIDService;
import com.redone13.pp.mFragments.EmployeeFragment;
import com.redone13.pp.mFragments.MoreFragment;
import com.redone13.pp.mFragments.ParkingPlaceFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    public static final String EMPLOYEE_EMAIL = "employee_email";

    private BottomBar mBottomBar;
    private EmployeeFragment mEmployeeFragment;
    private ParkingPlaceFragment mParkingPlaceFragment;
    private MoreFragment mMoreFragment;
    private Employee mEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("news");

        mEmployee = new Employee();

        Intent intent = getIntent();
        mEmployee.setEmail(intent.getStringExtra(EmployeeFragment.EMPLOYEE_FRAGMENT_EMAIL));
        Toast.makeText(MainActivity.this, mEmployee.getEmail(), Toast.LENGTH_SHORT).show();

        if(getStateUpdateTokenFCM()) {
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, token);
            // Update token for user in db
            registerToken(mEmployee.getEmail(), token);
            updateTokenFCM(false);
        }

        EmployeeFragment savedFragment = (EmployeeFragment) getSupportFragmentManager().
                findFragmentByTag(EmployeeFragment.EMPLOYEE_FRAGMENT);
        if(savedFragment == null) {
            mEmployeeFragment = new EmployeeFragment();
            Bundle bundle = new Bundle();
            bundle.putString(EmployeeFragment.EMPLOYEE_FRAGMENT_EMAIL, mEmployee.getEmail());
            mEmployeeFragment.setArguments(bundle);
        }else {
            mEmployeeFragment = savedFragment;
        }

        ParkingPlaceFragment savedParkingFragment = (ParkingPlaceFragment) getSupportFragmentManager().
                findFragmentByTag(ParkingPlaceFragment.PARKING_PLACE_FRAGMENT);
        if(savedParkingFragment == null) {
            mParkingPlaceFragment = new ParkingPlaceFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.EMPLOYEE_EMAIL, mEmployee.getEmail());
            mParkingPlaceFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.placeHolder, mParkingPlaceFragment, ParkingPlaceFragment.PARKING_PLACE_FRAGMENT);
            fragmentTransaction.commit();
        }else {
            mParkingPlaceFragment = savedParkingFragment;
        }

        MoreFragment savedMoreFragment = (MoreFragment) getSupportFragmentManager().
                findFragmentByTag(MoreFragment.MORE_FRAGMENT);
        if(savedMoreFragment == null) {
            mMoreFragment = new MoreFragment();
        }else {
            mMoreFragment = savedMoreFragment;
        }

        mBottomBar = BottomBar.attach(MainActivity.this, savedInstanceState);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {

            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId == R.id.bottomBarUserData){
                    Log.i(TAG, mEmployee.getEmail());
                    replaceFragmentObjectById(mEmployeeFragment, EmployeeFragment.EMPLOYEE_FRAGMENT);

                } else if(menuItemId == R.id.bottomBarParkingPlace) {
                    replaceFragmentObjectById(mParkingPlaceFragment, ParkingPlaceFragment.PARKING_PLACE_FRAGMENT);

                } else if(menuItemId == R.id.bottomBarMore) {
                    replaceFragmentObjectById(mMoreFragment, MoreFragment.MORE_FRAGMENT);
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                Log.i(TAG, "Menu reselected!!!");
            }
        });

        mBottomBar.setItems(R.menu.bottombar_menu);
    }

    private Boolean getStateUpdateTokenFCM() {
        SharedPreferences settings = getSharedPreferences(RegisterActivity.REDLEE, MODE_PRIVATE);
        return settings.getBoolean(FirebaseInstanceIDService.UPDATE_TOKEN, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBottomBar.onSaveInstanceState(outState);
    }

    private void replaceFragmentObjectById(Fragment fragment, String fragmentTag, Bundle bundle) {
        Log.i(TAG, fragmentTag);
        Log.d(TAG, "Dónde estoyyyyyyyy: " + mBottomBar.getCurrentTabPosition());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeHolder, fragment, fragmentTag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void replaceFragmentObjectById(Fragment fragment, String fragmentTag) {
        Log.i(TAG, fragmentTag);
        Log.d(TAG, "Dónde estoyyyyyyyy: " + mBottomBar.getCurrentTabPosition());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.placeHolder, fragment, fragmentTag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void updateTokenFCM(Boolean update) {
        SharedPreferences settings = getSharedPreferences(RegisterActivity.REDLEE, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(FirebaseInstanceIDService.UPDATE_TOKEN, update);
        editor.apply();
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
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(tokenRegister);
    }
}