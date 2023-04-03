package com.example.qrmonsters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

//import com.google.firebase.firestore.FirebaseFirestore;

/**
 The MainActivity class represents the first screen of the QR Monsters application and extends AppCompatActivity.
 It checks if the user is registered and redirects them to the RegistrationActivity or HomeActivity accordingly.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is starting.
     * Initializes the SharedPreferences object to check if the user is registered,
     * and redirects them to the RegistrationActivity or HomeActivity accordingly.
     *
     * @param savedInstanceState the saved instance state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        boolean isRegistered = sharedPreferences.getBoolean("isRegistered", false);

        Intent intent;
        //intent = new Intent(this, RegistrationActivity.class);
        if (!isRegistered) {
            intent = new Intent(this, RegistrationActivity.class);
        } else {
            intent = new Intent(this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }


}
