package com.example.qrmonsters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    Integer playerQRCount;
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
        String currentUser = getIntent().getStringExtra("currentUser");

        //reference to current player profile being viewed
        String playerView = getIntent().getStringExtra("viewUser");

        db = FirebaseFirestore.getInstance();

        qrList = findViewById(R.id.playerQRList);

        qrDataList = new ArrayList<>();

        qrAdapter = new QrCustomAdapter(this, qrDataList);

        qrList.setAdapter(qrAdapter);

        playerScoreTV = findViewById(R.id.playerScoreTextView);
        playerNameTV = findViewById(R.id.playerNameTextView);
        playerQRCountTV = findViewById(R.id.playerQRcountTextView);

        //final CollectionReference qrReference = db.collection("qrCodes");

        DocumentReference playerInfo = db.collection("users")
                .document(playerView);

        playerInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());

                        Player playerRef = document.toObject(Player.class);

                        for (String qrCode: playerRef.getQrCodes()) {

                            DocumentReference qrInfo = db.collection("qrCodes").document(qrCode);

                            qrInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());

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

                                            playerNameTV.setText("Player Name: " + playerRef.getUsername());

                                            updateScore();
                                            updateTotalQR();

                                            playerScoreTV.setText("Player Score: " + String.valueOf(playerScore));
                                            playerQRCountTV.setText("Player QR count: " + String.valueOf(playerQRCount));

                                        } else {
                                            Log.d("!EXISTS", "No such document");
                                        }
                                    } else {
                                        Log.d("TASK FAILED", "get failed with ", task.getException());
                                    }
                                }
                            });

                        }

                    } else {
                        Log.d("!EXISTS", "No such document");
                    }
                } else {
                    Log.d("TASK FAILED", "get failed with ", task.getException());
                }
            }
        });

        qrList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                QRCodeObject clickQR = qrAdapter.getItem(i);

                playerInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());

                                Player playerRef = document.toObject(Player.class);

                                for (String qrCode: playerRef.getQrCodes()) {

                                    DocumentReference qrInfo = db.collection("qrCodes").document(qrCode);

                                    qrInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());

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

                                                    playerNameTV.setText("Player Name: " + playerRef.getUsername());

                                                    updateScore();
                                                    updateTotalQR();

                                                    playerScoreTV.setText("Player Score: " + String.valueOf(playerScore));
                                                    playerQRCountTV.setText("Player QR count: " + String.valueOf(playerQRCount));

                                                } else {
                                                    Log.d("!EXISTS", "No such document");
                                                }
                                            } else {
                                                Log.d("TASK FAILED", "get failed with ", task.getException());
                                            }
                                        }
                                    });

                                }

                            } else {
                                Log.d("!EXISTS", "No such document");
                            }
                        } else {
                            Log.d("TASK FAILED", "get failed with ", task.getException());
                        }
                    }
                });

                return true;
            }
        });


    }

    public void updateScore(){

        Integer tentScore = 0;

        for(QRCodeObject qr: qrDataList){

            tentScore += qr.getCodeScore();

        }

        playerScore = tentScore;

    }

    public void updateTotalQR(){

        playerQRCount = qrDataList.size();

    }
}