package com.example.qrmonsters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements LocationListener {
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private TextView tv_location;
    private static final int PERMISSION_LOCATION = 1000;

    Button curLocBut;
    Button scanQR;
    Button nearbyQR;
    Location currentlocation;

    LocationManager locationManager;

    @SuppressLint({"MissingPermission", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION);

        }

        else{
            showLocation();
        }

        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_LOCATION);

        }


        // Get references to the UI components
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        curLocBut = findViewById(R.id.viewCurrentLocation);
        scanQR = findViewById(R.id.scanQRCodeButton);
        tv_location = findViewById(R.id.tv_location);
        nearbyQR = findViewById(R.id.searchNearbyQRButton);

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

        nearbyQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(HomeActivity.this, searchNearbyQR.class);
                intent.putExtra("User Location", currentlocation);

                startActivity(intent);



            }
        });

        curLocBut.setOnClickListener(view -> {

            LatLng currLoc = new LatLng(currentlocation.getLatitude(),
                    currentlocation.getLongitude());

            new currLocationFragment(currLoc).show(getSupportFragmentManager(),
                    "CURR_LOC");
        });


        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Scan_QR.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission not Granted!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @SuppressLint("MissingPermission")
    private void showLocation(){

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

           // tv_location.setText("Loading Location...");

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, this);
        }
        else{
            Toast.makeText(this, "Enable GPS!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

    }

    private String hereLocation(Location location){

        return "Lat: " + location.getLatitude() + "\nLong: " + location.getLongitude();

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
        Intent intent = new Intent(this, UserSearchActivity.class);
        startActivity(intent);

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        tv_location.setText(hereLocation(location));
        currentlocation = location;

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
