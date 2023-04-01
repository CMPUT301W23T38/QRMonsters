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
/**
 This class is responsible for displaying the result of a scanned QR code and processing it
 */
public class ScannedResult extends AppCompatActivity {
    private Player playerRef;
    /**
     * This method is called when the activity is created, initializes the UI with the scan result and image.
     * @param savedInstanceState The bundle containing the saved instance state
     */
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
    /**
     * This method initializes the UI elements.
     * @param theResult The result of the scanned QR code
     * @param theBitmap The image of the scanned QR code
     */
    private void setUI(String theResult, Bitmap theBitmap) {
        TextView mTvResult = findViewById(R.id.tv_result);
        ImageView mImageCallback = findViewById(R.id.image_callback);

        mTvResult.setText("QR Code Scanned: " + theResult);

        if (theBitmap != null) {
            mImageCallback.setImageBitmap(theBitmap);
        }
    }
    /**
     * This method processes the scanned QR code and updates the database accordingly.
     * @param currentUser The ID of the current user
     * @param qrLocation The location where the QR code was scanned
     * @param theResult The result of the scanned QR code
     */
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
    /**

     This method processes the scanned QR code for a player and updates the necessary information.

     @param userInfo the reference to the player's information document in Firestore

     @param qrName the name of the scanned QR code

     @param qrRef the reference to the QR code collection in Firestore

     @param qrAdd the QRCodeObject representing the scanned QR code

     @param usersRef the reference to the collection of all players in Firestore
     */
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
    /**

     This method checks if a QR code exists in the Firestore database and adds it if it doesn't.
     @param qrName the name of the scanned QR code
     @param qrRef the reference to the QR code collection in Firestore
     @param qrAdd the QRCodeObject representing the scanned QR code
     */
    private void processQRCode(String qrName, CollectionReference qrRef, QRCodeObject qrAdd) {
        qrRef.whereEqualTo("codeName", qrName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    addNewQRCode(qrRef, qrAdd);
                }
            }
        });
    }
    /**

     This method adds a new QR code to the Firestore database.
     @param qrRef the reference to the QR code collection in Firestore
     @param qrAdd the QRCodeObject representing the scanned QR code
     */
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
    /**

     This method updates the UI to display information about other players who have scanned the same QR code.

     @param qrName the name of the scanned QR code

     @param usersRef the reference to the collection of all players in Firestore
     */
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
    /**

     Updates the UI to display the number of players who have scanned a QR code and their usernames.

     @param playersScannedSameQR the number of players who have scanned the QR code

     @param playersScannedUsernames a List of the usernames of the players who have scanned the QR code
     */
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

