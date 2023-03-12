package com.example.qrmonsters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.zxing.activity.CaptureActivity;

import java.util.List;
/**
 * This is the main activity for the QR Monsters Android application. It allows users to view their
 * profile information and perform various actions, such as scanning QR codes, searching for nearby
 * QR codes, searching for other users, and viewing a leaderboard.
 */
public class HomeActivity extends AppCompatActivity implements LocationListener {
    private TextView tv_location;
    private static final int PERMISSION_LOCATION = 1000;
    private final static int REQ_CODE = 1028;


    Button curLocBut;
    Button scanQR;
    Button nearbyQR;
    Button viewSelfProfile;
    Location currentlocation;
    String userID;

    /**
     * This method is called when the activity is created. It sets up the UI components and loads
     * the user's profile information from SharedPreferences. It also checks for location and camera
     * permissions and requests them if necessary.
     */
    @SuppressLint({"MissingPermission", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

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
        TextView usernameTextView = findViewById(R.id.usernameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        curLocBut = findViewById(R.id.viewCurrentLocation);
        scanQR = findViewById(R.id.scanQRCodeButton);
        tv_location = findViewById(R.id.tv_location);
        nearbyQR = findViewById(R.id.searchNearbyQRButton);
        viewSelfProfile = findViewById(R.id.viewMyQRCodesButton);

        // Load the user's profile information
        SharedPreferences preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String email = preferences.getString("email", "");
        String phoneNumber = preferences.getString("phoneNumber", "");
        userID = preferences.getString("userID", "");


//        Log.d("HomeActivity", "Username: " + username);
//        Log.d("HomeActivity", "Email: " + email);
//        Log.d("HomeActivity", "Phone: " + phoneNumber);

        // Update the UI with the user's profile information
        usernameTextView.setText("Username: " + username);
        emailTextView.setText("Email: " + email);
        phoneNumberTextView.setText("Phone: " + phoneNumber);

        viewSelfProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, viewPlayerProfile.class);
                intent.putExtra("currentUser", userID);
                intent.putExtra("viewUser", userID);

                startActivity(intent);

            }
        });

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
                Intent intent = new Intent(HomeActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQ_CODE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {

            try{
                String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
                Bitmap bitmap = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);
                Intent intent1 = new Intent(HomeActivity.this, ScannedResult.class);
                intent1.putExtra("TheResult", result);
                intent1.putExtra("TheBitmap", bitmap);
                intent1.putExtra("UserID", userID);
                intent1.putExtra("location", currentlocation);
                startActivity(intent1);                   //Jumped to ScannedResult class
                //Toast.makeText(HomeActivity.this, "" + result, Toast.LENGTH_SHORT).show();
//                if(bitmap != null){
//                    mImageCallback.setImageBitmap(bitmap);
//                }

            }catch (NullPointerException e){
                System.out.println("");
            }

        }

    }
    /**

     Handles the result of a permission request for location access. If the user grants permission,

     a toast is displayed indicating the permission was granted. If the user denies permission, a toast

     is displayed indicating the permission was not granted and the activity is finished.

     @param requestCode The request code for the permission request

     @param permissions The requested permissions

     @param grantResults The results of the permission request
     */
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
    /**

     Shows the user's current location on the screen. If location access is not enabled, a toast

     is displayed indicating that the user needs to enable location access.
     */
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
    /**

     Returns a string representation of the user's current location with latitude and longitude .

     @param location The user's current location

     @return A string representation of the user's current location with latitude and longitude
     */
    private String hereLocation(Location location){

        return "Lat: " + location.getLatitude() + "\nLong: " + location.getLongitude();

    }
    /**

     Launches the QR code scanning functionality when the "Scan QR Code" button is clicked.
     @param view The button view that was clicked
     */
    public void scanQRCode(View view) {
        // TODO: Add code to launch the QR code scanning functionality
    }

    /**
     Displays a list of the user's QR codes when the "View My QR Codes" button is clicked.
     @param view The button view that was clicked
     */
    public void viewMyQRCodes(View view) {
        // TODO: Add code to display a list of the user's QR codes
    }
    /**
     Displays the leaderboard when the "View Leaderboard" button is clicked.
     @param view The button view that was clicked
     */
    public void viewLeaderboard(View view) {
        // TODO: Add code to display the leaderboard
    }

    /**
     Launches the user search functionality when the "Search Users" button is clicked.
     @param view The button view that was clicked
     */
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
