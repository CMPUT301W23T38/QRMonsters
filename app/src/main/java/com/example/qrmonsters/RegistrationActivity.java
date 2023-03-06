package com.example.qrmonsters;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

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
            String userId = usersRef.push().getKey();
            QRMonstersUser user = new QRMonstersUser(userId, username, email, phoneNumber);
            usersRef.child(userId).setValue(user);

            // Store user information in shared preferences
            editor.putString("username", username);
            editor.putString("email", email);
            editor.putString("phoneNumber", phoneNumber);
            editor.remove("phone");
            editor.putBoolean("isRegistered", true);
            editor.apply();

            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();

        });
    }

}
