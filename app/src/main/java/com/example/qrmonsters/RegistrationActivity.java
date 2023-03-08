package com.example.qrmonsters;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity {
    private EditText mUsernameEditText;
    private EditText mEmailEditText;
    private EditText mPhoneNumberEditText;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize UI components
        mUsernameEditText = findViewById(R.id.username_edit_text);
        mEmailEditText = findViewById(R.id.email_edit_text);
        mPhoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        mRegisterButton = findViewById(R.id.register_button);

        // Initialize shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        // Set click listener for the register button
        mRegisterButton.setOnClickListener(v -> {
            // Retrieve user input
            String username = mUsernameEditText.getText().toString().trim();
            String email = mEmailEditText.getText().toString().trim();
            String phoneNumber = mPhoneNumberEditText.getText().toString().trim();

            // Validate user input
            if (TextUtils.isEmpty(username)) {
                mUsernameEditText.setError("Please enter a username");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                mEmailEditText.setError("Please enter an email address");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmailEditText.setError("Please enter a valid email address");
                return;
            }
            if (TextUtils.isEmpty(phoneNumber)) {
                mPhoneNumberEditText.setError("Please enter a phone number");
                return;
            }
            if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
                mPhoneNumberEditText.setError("Please enter a valid phone number");
                return;
            }

            // Store user information in Firebase
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
//            String userId = usersRef.push().getKey();
//            QRMonstersUser user = new QRMonstersUser(userId, username, email, phoneNumber);
//            usersRef.child(userId).setValue(user);
//
//            // Store user information in shared preferences
//            editor.putString("username", username);
//            editor.putString("email", email);
//            editor.putString("phoneNumber", phoneNumber);
//            editor.remove("phone");
//            editor.putBoolean("isRegistered", true);
//            editor.apply();
//
//            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
            // check if the username already exists in the database
            usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // the username already exists, show an error message to the user
                        Toast.makeText(RegistrationActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        // the username is unique, create a new user object and add it to the database
                        String userId = usersRef.push().getKey();
                        Player user = new Player(userId, username, email, phoneNumber);
                        usersRef.push().setValue(user);
                        // save the registration status to SharedPreferences

                        editor.putBoolean("isRegistered", true);
                        editor.apply();
                        // start the HomeActivity
                        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Display an error message to the user
                    Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    // Log the error for debugging purposes
                    Log.e(TAG, "Error reading data from Firebase Database", error.toException());
                }


            });
        });
    }
}

