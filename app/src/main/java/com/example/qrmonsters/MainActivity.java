package com.example.qrmonsters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

//import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

// <<<<<<< QRScanner_Integrate 
FirebaseFirestore db = FirebaseFirestore.getInstance();
Button btm;
// =======
//     //FirebaseFirestore db = FirebaseFirestore.getInstance();
// >>>>>>> main

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