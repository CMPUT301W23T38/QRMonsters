package com.example.qrmonsters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannedResult extends AppCompatActivity {
    private Player playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_result);

        Intent intent1 = getIntent();
        String theResult = intent1.getStringExtra("TheResult");
        Bitmap theBitmap = intent1.getParcelableExtra("TheBitmap");
        String currentUser = intent1.getStringExtra("UserID");
        Location qrLocation = intent1.getParcelableExtra("location");

        setUI(theResult, theBitmap);
        processScannedResult(currentUser, qrLocation, theResult);
    }

    private void setUI(String theResult, Bitmap theBitmap) {
        TextView mTvResult = findViewById(R.id.tv_result);
        ImageView mImageCallback = findViewById(R.id.image_callback);

        mTvResult.setText("QR Code Scanned: " + theResult);

        if (theBitmap != null) {
            mImageCallback.setImageBitmap(theBitmap);
        }
    }

    private void processScannedResult(String currentUser, Location qrLocation, String theResult) {
        String hashCode = SHA256andScore.getSha256Str(theResult);
        Integer hashScore = SHA256andScore.getScore(hashCode);
        String qrName = SHA256andScore.generateName(hashCode);

        QRCodeObject qrAdd = new QRCodeObject(qrName, hashCode, hashScore, qrLocation);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference qrRef = db.collection("qrCodes");
        CollectionReference usersRef = db.collection("users");

        DocumentReference userInfo = db.collection("users").document(currentUser);

        userInfo.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        playerRef = documentSnapshot.toObject(Player.class);
                        processPlayer(userInfo, qrName, qrRef, qrAdd, usersRef);
                    } else {
                        Toast.makeText(ScannedResult.this, "DOCUMENT DOES NOT EXIST",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(ScannedResult.this, "FAILED",
                        Toast.LENGTH_SHORT).show());
    }

    private void processPlayer(DocumentReference userInfo, String qrName, CollectionReference qrRef,
                               QRCodeObject qrAdd, CollectionReference usersRef) {
        if (playerRef.getQrCodes().contains(qrName)) {
            Toast.makeText(ScannedResult.this, "Already have this QR Code!",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        userInfo.update("qrCodes", FieldValue.arrayUnion(qrName));
        playerRef.addQRCode(qrName, qrAdd.getCodeScore());
        userInfo.update("qrScores", playerRef.getQrScores());

        processQRCode(qrName, qrRef, qrAdd);
        updatePlayersScannedInfo(qrName, usersRef);
    }

    private void processQRCode(String qrName, CollectionReference qrRef, QRCodeObject qrAdd) {
        qrRef.whereEqualTo("codeName", qrName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    addNewQRCode(qrRef, qrAdd);
                }
            }
        });
    }

    private void addNewQRCode(CollectionReference qrRef, QRCodeObject qrAdd) {
        Map<String, Object> data = new HashMap<>();
        data.put("codeName", qrAdd.getCodeName());
        data.put("codeHash", qrAdd.getCodeHash());
        data.put("codeScore", qrAdd.getCodeScore());
        data.put("codeLocation", qrAdd.getCodeLocation());
        data.put("comments", qrAdd.getComments());
        qrRef.document(qrAdd.getCodeName())
                .set(data)
                .addOnSuccessListener(unused -> Log.d("Working", "Data added successfully"))
                .addOnFailureListener(e -> Log.d("Working", "Data not added" + e));
    }

    private void updatePlayersScannedInfo(String qrName, CollectionReference usersRef) {
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int playersScannedSameQR = 0;
                List<String> playersScannedUsernames = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    Player otherPlayer = document.toObject(Player.class);
                    if (otherPlayer.getQrCodes().contains(qrName) && !otherPlayer.getUsername().equals(playerRef.getUsername())) {
                        playersScannedSameQR++;
                        playersScannedUsernames.add(otherPlayer.getUsername());
                    }
                }

                updatePlayersScannedUI(playersScannedSameQR, playersScannedUsernames);
            } else {
                Log.d("ScannedResult", "Error getting users: ", task.getException());
            }
        });
    }

    private void updatePlayersScannedUI(int playersScannedSameQR, List<String> playersScannedUsernames) {
        TextView tvPlayersScanned = findViewById(R.id.tv_players_scanned);
        TextView tvPlayersScannedName = findViewById(R.id.tv_players_scanned_name);

        if (playersScannedSameQR == 0) {
            tvPlayersScanned.setText("No one has scanned this QR code yet!");
        } else {
            tvPlayersScanned.setText("# of players scanned this QR code: " + playersScannedSameQR);
        }

        String playersScannedName = String.join(", ", playersScannedUsernames);
        tvPlayersScannedName.setText(playersScannedName);
    }
}

