package com.example.ifind.pushMessage;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class Push extends FBPush {
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("IDService", "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

    }
}