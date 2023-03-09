package com.example.qrmonsters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.zxing.qrcode.encoder.QRCode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class searchNearbyQR extends AppCompatActivity {

    FirebaseFirestore db;
    ListView qrList;
    ArrayAdapter<QRCodeObject> qrAdapter;
    ArrayList<QRCodeObject> qrDataList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nearby_qr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Location userLocation = getIntent().getParcelableExtra("User Location");

        QRCodeObject qrAdd;

        qrList = findViewById(R.id.nearbyQRList);

        qrDataList = new ArrayList<>();

        qrAdapter = new nearbyQrCustomAdapter(this, qrDataList);

        qrList.setAdapter(qrAdapter);


        db =FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("qrCodes");



        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                qrDataList.clear();
                for (QueryDocumentSnapshot doc: value) {
                    Log.d("Retrieve", String.valueOf(doc.getData().get("Nearest QR")));


                    String cn = (String) doc.getData().get("codeName");
                    String ch = (String) doc.getData().get("codeHash");
                    Integer cs = Math.toIntExact((Long) doc.getData().get("codeScore"));
                    HashMap locationData = (HashMap) doc.getData().get("codeLocation");

                    Location qrLoc = new Location("");
                    qrLoc.setLatitude((Double) locationData.get("latitude"));
                    qrLoc.setLongitude((Double) locationData.get("longitude"));

                    qrDataList.add(new QRCodeObject(cn, ch, cs, qrLoc));

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