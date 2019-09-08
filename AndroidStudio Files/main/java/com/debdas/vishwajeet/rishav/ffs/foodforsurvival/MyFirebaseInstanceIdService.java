package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";
    DatabaseReference databaseReference;
    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,recent_token);

        sendRegistrationToServer(recent_token);



    }

    private void sendRegistrationToServer(String recent_token) {

        databaseReference= FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", 0);
        String email = sharedPreferences.getString("email", "");

        User user=new User(recent_token,email);
        databaseReference.setValue(user);
    }
}
