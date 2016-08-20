package com.redone13.pp;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.redone13.pp.mFragments.EmployeeFragment;
import com.redone13.pp.mFragments.MoreFragment;
import com.redone13.pp.mFragments.ParkingPlaceFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    public static final String EMPLOYEE_FRAGMENT = "employee_fragment";
    public static final String PARKING_PLACE_FRAGMENT = "parking_place_fragment";
    public static final String MORE_FRAGMENT = "more_fragment";
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmployeeFragment savedFragment = (EmployeeFragment) getSupportFragmentManager().
                findFragmentByTag(EMPLOYEE_FRAGMENT);
        if(savedFragment == null) {
            EmployeeFragment employeeFragment = new EmployeeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.placeHolder, employeeFragment, EMPLOYEE_FRAGMENT);
            fragmentTransaction.commit();
        }

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.bottombar_menu);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            ParkingPlaceFragment parkingPlaceFragment = new ParkingPlaceFragment();
            MoreFragment moreFragment = new MoreFragment();

            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if(menuItemId == R.id.bottomBarUserData){
                    Log.i(TAG, "User Data");
                } else if(menuItemId == R.id.bottomBarParkingPlace) {
                    Log.i(TAG, "Parking Place");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.placeHolder, parkingPlaceFragment, PARKING_PLACE_FRAGMENT);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else if(menuItemId == R.id.bottomBarMore) {
                    Log.i(TAG, "More");
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.placeHolder, moreFragment, MORE_FRAGMENT);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                Log.i(TAG, "Menu reselected!!!");
            }
        });
    }
}