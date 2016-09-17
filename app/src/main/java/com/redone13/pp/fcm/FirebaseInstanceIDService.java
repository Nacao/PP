package com.redone13.pp.fcm;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.redone13.pp.VolleyRequests.TokenRegister;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "fireBaseIdService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        registerToken(refreshedToken);
    }

    private void registerToken(String token) {
        // Hacer el registro con Volley

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

        TokenRegister tokenRegister = new TokenRegister("alfredo.lugo@mymail.com", token, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(tokenRegister);
    }
}
