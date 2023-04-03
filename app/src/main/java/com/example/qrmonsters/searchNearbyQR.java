package com.example.qrmonsters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**

 The searchNearbyQR class is an activity that displays a list of the nearest QR codes
 to the user's current location. It retrieves QR code data from a  database
 and calculates the distance from the user's location to each code location in the database.
 It then displays the three nearest codes in a ListView.
 */
public class searchNearbyQR extends AppCompatActivity implements OnMapReadyCallback {

    FirebaseFirestore db;
    ListView qrList;
    ArrayAdapter<QRCodeObject> qrAdapter;
    ArrayList<QRCodeObject> qrDataList;
    private GoogleMap mMap;
    private Location userLocation;
    private static final int SEARCH_RADIUS_METERS = 50000;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add markers for each QR code
        for (QRCodeObject qrCodeObject : qrDataList) {
            LatLng qrLatLng = new LatLng(qrCodeObject.getCodeLocation().getLatitude(),
                    qrCodeObject.getCodeLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(qrLatLng).title(qrCodeObject.getCodeName()));
        }

        if (userLocation != null) {
            // Move the camera to the user's location
            LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
        }
    }

    private void searchQRByLocation(String locationName) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                Location targetLocation = new Location("");
                targetLocation.setLatitude(latitude);
                targetLocation.setLongitude(longitude);

                float maxDistance = 10000; // Set a maximum distance in meters

                ListView nearbyQRList = findViewById(R.id.nearbyQRList);
                FrameLayout mapContainer = findViewById(R.id.mapContainer);

                Query query = db.collection("qrCodes");
                query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<QRCodeObject> qrCodeObjects = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String codeName = doc.getString("codeName");
                        Integer score = ((Long) doc.get("codeScore")).intValue();

                        // Get the location data from Firestore
                        Map<String, Object> locationData = (Map<String, Object>) doc.get("codeLocation");
                        if (locationData != null) {
                            double qrLatitude = (double) locationData.get("latitude");
                            double qrLongitude = (double) locationData.get("longitude");

                            // Create a Location object from the Firestore data
                            Location qrLocation = new Location("");
                            qrLocation.setLatitude(qrLatitude);
                            qrLocation.setLongitude(qrLongitude);

                            QRCodeObject qrCode = new QRCodeObject(codeName, null, score, qrLocation);
                            float[] distance = new float[1];
                            Location.distanceBetween(qrLatitude, qrLongitude, targetLocation.getLatitude(), targetLocation.getLongitude(), distance);
                            if (distance[0] <= maxDistance) {
                                qrCodeObjects.add(qrCode);
                            }
                        }
                    }

                    QrCustomAdapter qrCustomAdapter = new QrCustomAdapter(searchNearbyQR.this, qrCodeObjects);
                    nearbyQRList.setAdapter(qrCustomAdapter);

                    // Clear the map and add new markers for the searched QR codes
                    mMap.clear();
                    for (QRCodeObject qr : qrCodeObjects) {
                        LatLng qrLatLng = new LatLng(qr.getCodeLocation().getLatitude(), qr.getCodeLocation().getLongitude());
                        mMap.addMarker(new MarkerOptions().position(qrLatLng).title(qr.getCodeName()));
                    }
                });

                nearbyQRList.setVisibility(View.VISIBLE);
                mapContainer.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(searchNearbyQR.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(searchNearbyQR.this, "Error while searching location", Toast.LENGTH_SHORT).show();
        }
    }




    /**
     Called when the activity is first created.
     Sets up the UI, initializes the Firestore database, and retrieves
     the user's location passed in from the previous activity.
     @param savedInstanceState The saved state of the activity
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nearby_qr);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userLocation = getIntent().getParcelableExtra("User Location");
        ArrayList playerList = getIntent().getStringArrayListExtra("playerList");

        //QRCodeObject qrAdd;

        qrList = findViewById(R.id.nearbyQRList);
        qrDataList = new ArrayList<>();
        qrAdapter = new QrCustomAdapter(this, qrDataList);
        qrList.setAdapter(qrAdapter);

        db =FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("qrCodes");
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mapContainer, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        EditText locationSearchEditText = findViewById(R.id.locationSearchEditText);
        Button searchLocationButton = findViewById(R.id.searchLocationButton);

        searchLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationName = locationSearchEditText.getText().toString();
                searchQRByLocation(locationName);
            }
        });

        qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                QRCodeObject clickQR = qrAdapter.getItem(i);

                Location qrLocation = clickQR.getCodeLocation();

                LatLng currLoc = new LatLng(qrLocation.getLatitude(),
                        qrLocation.getLongitude());

                new currLocationFragment(currLoc).show(getSupportFragmentManager(),
                        "CURR_LOC");
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                qrDataList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Log.d("Retrieve", String.valueOf(doc.getData().get("Nearest QR")));

                    if (!playerList.contains((String) doc.getData().get("codeName"))) {

                        String cn = (String) doc.getData().get("codeName");
                        String ch = (String) doc.getData().get("codeHash");
                        Integer cs = Math.toIntExact((Long) doc.getData().get("codeScore"));
                        HashMap locationData = (HashMap) doc.getData().get("codeLocation");

                        Location qrLoc = null;

                        if (locationData != null) {
                            qrLoc = new Location("");
                            qrLoc.setLatitude((Double) locationData.get("latitude"));
                            qrLoc.setLongitude((Double) locationData.get("longitude"));
                        }

                        QRCodeObject toAdd = new QRCodeObject(cn, ch, cs, qrLoc);
                        if (qrLoc != null) {
                            qrDataList.add(toAdd);
                        }
                    }
                }

                qrAdapter.notifyDataSetChanged();
                if (mMap != null) {
                    onMapReady(mMap);
                }
            }
        });

        mapFragment.getMapAsync((OnMapReadyCallback) this);
    }
}

//         qrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//@Override
//public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//@ -79,88 +207,38 @@ public class searchNearbyQR extends AppCompatActivity {
//    @Override
//    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//        qrDataList.clear();
//        for (QueryDocumentSnapshot doc: value) {
//            for (QueryDocumentSnapshot doc : value) {
//                Log.d("Retrieve", String.valueOf(doc.getData().get("Nearest QR")));
//
//                if (!playerList.contains((String) doc.getData().get("codeName"))) {
//
//                    if(!playerList.contains((String) doc.getData().get("codeName"))){
//                        String cn = (String) doc.getData().get("codeName");
//                        String ch = (String) doc.getData().get("codeHash");
//                        Integer cs = Math.toIntExact((Long) doc.getData().get("codeScore"));
//                        HashMap locationData = (HashMap) doc.getData().get("codeLocation");
//
//                        String cn = (String) doc.getData().get("codeName");
//                        String ch = (String) doc.getData().get("codeHash");
//                        Integer cs = Math.toIntExact((Long) doc.getData().get("codeScore"));
//                        HashMap locationData = (HashMap) doc.getData().get("codeLocation");
//
//                        Location qrLoc = null;
//
//                        if(locationData != null){
//                            qrLoc = new Location("");
//                            qrLoc.setLatitude((Double) locationData.get("latitude"));
//                            qrLoc.setLongitude((Double) locationData.get("longitude"));
//                        }
//
//                        QRCodeObject toAdd = new QRCodeObject(cn, ch, cs, qrLoc);
//                        if(qrLoc != null){
//                            qrDataList.add(toAdd);
//                        }
//                    }
//                }
//
//                QRCodeObject top1 = null;
//                QRCodeObject top2 = null;
//                QRCodeObject top3 = null;
//
//                if (qrDataList.size() > 0){
//                    top1 = qrDataList.get(0);
//                    for (int i = 1; i < qrDataList.size(); i++)
//                    {
//                        if (userLocation.distanceTo(qrDataList.get(i).getCodeLocation())
//                                < userLocation.distanceTo(top1.getCodeLocation())){
//                            top1 = qrDataList.get(i);
//                        }
//                    }
//                    qrDataList.remove(top1);
//                }
//                Location qrLoc = null;
//
//                if (qrDataList.size() > 0){
//                    top2 = qrDataList.get(0);
//                    for (int i = 1; i < qrDataList.size(); i++)
//                    {
//                        if (userLocation.distanceTo(qrDataList.get(i).getCodeLocation())
//                                < userLocation.distanceTo(top1.getCodeLocation())){
//                            top2 = qrDataList.get(i);
//                            if (locationData != null) {
//                                qrLoc = new Location("");
//                                qrLoc.setLatitude((Double) locationData.get("latitude"));
//                                qrLoc.setLongitude((Double) locationData.get("longitude"));
//                            }
//                        }
//                        qrDataList.remove(top2);
//                    }
//
//                    if (qrDataList.size() > 0){
//                        top3 = qrDataList.get(0);
//                        for (int i = 1; i < qrDataList.size(); i++)
//                        {
//                            if (userLocation.distanceTo(qrDataList.get(i).getCodeLocation())
//                                    < userLocation.distanceTo(top1.getCodeLocation())){
//                                top3 = qrDataList.get(i);
//                                QRCodeObject toAdd = new QRCodeObject(cn, ch, cs, qrLoc);
//                                if (qrLoc != null) {
//                                    qrDataList.add(toAdd);
//                                }
//                            }
//                            qrDataList.remove(top3);
//                        }
//
//                        qrDataList.clear();
//
//                        if(top1 != null){
//                            qrDataList.add(top1);
//                        }
//
//                        if(top2 != null){
//                            qrDataList.add(top2);
//                        }
//
//                        if(top3 != null){
//                            qrDataList.add(top3);
//                        }
//
//                        qrAdapter.notifyDataSetChanged();
//                        if (mMap != null) {
//                            onMapReady(mMap);
//                        }
//                    }
//                });
//
//                mapFragment.getMapAsync((OnMapReadyCallback) this);
//            }
//        }