package com.example.qrmonsters;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Get references to the UI components
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);

        // Load the user's profile information
        SharedPreferences preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String email = preferences.getString("email", "");
        String phoneNumber = preferences.getString("phoneNumber", "");

//        Log.d("HomeActivity", "Username: " + username);
//        Log.d("HomeActivity", "Email: " + email);
//        Log.d("HomeActivity", "Phone: " + phoneNumber);

        // Update the UI with the user's profile information
        usernameTextView.setText(username);
        emailTextView.setText(email);
        phoneNumberTextView.setText(phoneNumber);
    }

    // Handle clicks on the "Scan QR Code" button
    public void scanQRCode(View view) {
        // TODO: Add code to launch the QR code scanning functionality
    }

    // Handle clicks on the "View My QR Codes" button
    public void viewMyQRCodes(View view) {
        // TODO: Add code to display a list of the user's QR codes
    }

    // Handle clicks on the "View Leaderboard" button
    public void viewLeaderboard(View view) {
        // TODO: Add code to display the leaderboard
    }

    // Handle clicks on the "Search Users" button
    public void searchUsers(View view) {
        // TODO: Add code to launch the user search functionality
    }
}
