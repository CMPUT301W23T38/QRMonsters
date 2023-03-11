package com.example.qrmonsters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class viewPlayerProfile extends AppCompatActivity {


    FirebaseFirestore db;
    ListView playerQRList;
    ArrayAdapter<QRCodeObject> qrAdapter;
    ArrayList<QRCodeObject> qrDataList;
    Integer playerScore;
    String playerQRCount;
    List<String> playerQRs;

    ListView qrList;

    TextView playerNameTV;
    TextView playerScoreTV;
    TextView playerQRCountTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_player_profile);

        //for when viewing other profiles, still has access to curent user name
        Player currentUser = getIntent().getParcelableExtra("Current User");

        //reference to current player profile being viewed
        Player playerName = getIntent().getParcelableExtra("Player Name");

        db = FirebaseFirestore.getInstance();

        qrList = findViewById(R.id.playerQRList);

        qrDataList = new ArrayList<>();

        qrAdapter = new QrCustomAdapter(this, qrDataList);

        qrList.setAdapter(qrAdapter);


        //final CollectionReference qrReference = db.collection("qrCodes");

        DocumentReference playerInfo = db.collection("users")
                .document(playerName.getUsername());

        playerInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());

                        playerQRs = (List<String>) document.getData().get("qrCodes");

                    } else {
                        Log.d("!EXISTS", "No such document");
                    }
                } else {
                    Log.d("TASK FAILED", "get failed with ", task.getException());
                }
            }
        });

        playerQRCount =  String.valueOf(playerQRs.size());
        
        playerScore = 0;

        for (String qrCode: playerQRs) {

            DocumentReference qrInfo =db.collection("qrCodes").document(qrCode);

            qrInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());

                            playerScore += (Integer) document.getData().get("codeScore");

                            String cn = (String) document.getData().get("codeName");
                            String ch = (String) document.getData().get("codeHash");
                            Integer cs = Math.toIntExact((Long) document.getData().get("codeScore"));
                            HashMap locationData = (HashMap) document.getData().get("codeLocation");

                            Location qrLoc = new Location("");
                            qrLoc.setLatitude((Double) locationData.get("latitude"));
                            qrLoc.setLongitude((Double) locationData.get("longitude"));


                            QRCodeObject toAdd = new QRCodeObject(cn, ch, cs, qrLoc);
                            qrDataList.add(toAdd);

                            qrAdapter.notifyDataSetChanged();

                        } else {
                            Log.d("!EXISTS", "No such document");
                        }
                    } else {
                        Log.d("TASK FAILED", "get failed with ", task.getException());
                    }
                }
            });
            
        }

        playerNameTV = findViewById(R.id.playerNameTextView);
        playerScoreTV = findViewById(R.id.playerScoreTextView);
        playerQRCountTV = findViewById(R.id.playerQRcountTextView);

        playerNameTV.setText(playerName.getUsername());
        playerScoreTV.setText(String.valueOf(playerScore));
        playerQRCountTV.setText(playerQRCount);


    }
}