package com.example.qrmonsters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.zxing.qrcode.encoder.QRCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
/**

 The searchNearbyQR class is an activity that displays a list of the nearest QR codes
 to the user's current location. It retrieves QR code data from a  database
 and calculates the distance from the user's location to each code location in the database.
 It then displays the three nearest codes in a ListView.
 */
public class searchNearbyQR extends AppCompatActivity {

    FirebaseFirestore db;
    ListView qrList;
    ArrayAdapter<QRCodeObject> qrAdapter;
    ArrayList<QRCodeObject> qrDataList;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Location userLocation = getIntent().getParcelableExtra("User Location");
        ArrayList playerList = getIntent().getStringArrayListExtra("playerList");


        //QRCodeObject qrAdd;
        qrList = findViewById(R.id.nearbyQRList);

        qrDataList = new ArrayList<>();


        qrAdapter = new QrCustomAdapter(this, qrDataList);
        qrList.setAdapter(qrAdapter);


        db =FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("qrCodes");

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
                for (QueryDocumentSnapshot doc: value) {
                    Log.d("Retrieve", String.valueOf(doc.getData().get("Nearest QR")));


                    if(!playerList.contains((String) doc.getData().get("codeName"))){

                        String cn = (String) doc.getData().get("codeName");
                        String ch = (String) doc.getData().get("codeHash");
                        Integer cs = Math.toIntExact((Long) doc.getData().get("codeScore"));
                        HashMap locationData = (HashMap) doc.getData().get("codeLocation");

                        Location qrLoc = new Location("");
                        qrLoc.setLatitude((Double) locationData.get("latitude"));
                        qrLoc.setLongitude((Double) locationData.get("longitude"));


                        QRCodeObject toAdd = new QRCodeObject(cn, ch, cs, qrLoc);
                        qrDataList.add(toAdd);


                    }



                }

                QRCodeObject top1;
                QRCodeObject top2;
                QRCodeObject top3;

                Integer top1int;
                Integer top2int;
                Integer top3int;


                top1 = qrDataList.get(0);
                top1int = 0;
                for (int i = 1; i < qrDataList.size(); i++)
                {
                    if (userLocation.distanceTo(qrDataList.get(i).getCodeLocation())
                            < userLocation.distanceTo(top1.getCodeLocation())){
                        top1 = qrDataList.get(i);
                        top1int = i;
                    }
                }

                qrDataList.remove(top1);

                top2 = qrDataList.get(0);
                top2int = 0;
                for (int i = 1; i < qrDataList.size(); i++)
                {
                    if (userLocation.distanceTo(qrDataList.get(i).getCodeLocation())
                            < userLocation.distanceTo(top2.getCodeLocation())){
                        top2 = qrDataList.get(i);
                        top2int = i;
                    }
                }

                qrDataList.remove(top2);

                top3 = qrDataList.get(0);
                top3int = 0;
                for (int i = 1; i < qrDataList.size(); i++)
                {
                    if (userLocation.distanceTo(qrDataList.get(i).getCodeLocation())
                            < userLocation.distanceTo(top3.getCodeLocation())){
                        top3 = qrDataList.get(i);
                        top3int = i;
                    }
                }

                qrDataList.remove(top3);

                qrDataList.clear();

                qrDataList.add(top1);
                qrDataList.add(top2);
                qrDataList.add(top3);

                qrAdapter.notifyDataSetChanged();

            }
        });

    }
}