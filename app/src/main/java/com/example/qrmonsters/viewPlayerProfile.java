package com.example.qrmonsters;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class viewPlayerProfile extends AppCompatActivity {
    FirebaseFirestore db;
    ListView playerQRList;
    ArrayAdapter<QRCodeObject> qrAdapter;
    ArrayList<QRCodeObject> qrDataList;
    Integer playerScore, playerQRCount;
    List<String> playerQRs;
    ListView qrList;
    TextView playerNameTV, playerScoreTV, playerQRCountTV, playerLowestTV, playerHighestTV, playerEstimatedRankingTV;

    private void fetchAllPlayers(FetchPlayersCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference playersRef = db.collection("users");
        CollectionReference qrCodesRef = db.collection("qrCodes");

        playersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Player> allPlayers = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Player player = document.toObject(Player.class);
                    ArrayList<String> qrCodes = player.getQrCodes();
                    ArrayList<Integer> qrScores = new ArrayList<>();

                    for (String qrCode : qrCodes) {
                        qrCodesRef.document(qrCode).get().addOnCompleteListener(qrCodeTask -> {
                            if (qrCodeTask.isSuccessful()) {
                                int score = qrCodeTask.getResult().getLong("codeScore").intValue();
                                qrScores.add(score);
                                player.setQrScores(qrScores);

                                if (qrScores.size() == qrCodes.size()) {
                                    allPlayers.add(player);
                                    if (allPlayers.size() == task.getResult().size()) {
                                        callback.onComplete(allPlayers);
                                    }
                                }
                            }
                        });
                    }
                }
            } else {
                Log.w(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    private QRCodeObject createQRCodeObject(DocumentSnapshot document) {
        String cn = document.getString("codeName");
        String ch = document.getString("codeHash");
        Integer cs = document.getLong("codeScore").intValue();
        HashMap<String, Object> locationData = (HashMap<String, Object>) document.get("codeLocation");
        HashMap<String, String> comments = (HashMap<String, String>) document.get("comments");

        Location qrLoc = null;
        if (locationData != null) {
            qrLoc = new Location("");
            qrLoc.setLatitude((Double) locationData.get("latitude"));
            qrLoc.setLongitude((Double) locationData.get("longitude"));
        }
        return new QRCodeObject(cn, ch, cs, qrLoc, comments);
    }

    private void updateUI(Player playerRef) {
        playerNameTV.setText("Name: " + playerRef.getUsername());
        updateScore();
        updateTotalQR();
        playerScoreTV.setText("Score: " + playerScore);
        playerQRCountTV.setText("# of QR codes: " + playerQRCount);

        QRCodeObject lowest = Collections.min(qrDataList, Comparator.comparing(QRCodeObject::getCodeScore));
        playerLowestTV.setText("Lowest QR code Score: \n" + lowest.getCodeName() + "    Score: " + lowest.getCodeScore().toString());

        QRCodeObject highest = Collections.max(qrDataList, Comparator.comparing(QRCodeObject::getCodeScore));
        playerHighestTV.setText("Highest QR code Score: \n" + highest.getCodeName() + "    Score: " + highest.getCodeScore().toString());

        fetchAllPlayers(allPlayers -> {
            int estimatedRanking = RankingManager.getEstimatedRanking(highest.getCodeScore(), allPlayers);
            playerEstimatedRankingTV.setText("Estimated Ranking: " + estimatedRanking);
        });
    }

    private void fetchQRData(Player playerRef, AtomicInteger qrCodesFetched) {
        for (String qrCode : playerRef.getQrCodes()) {
            DocumentReference qrInfo = db.collection("qrCodes").document(qrCode);
            qrInfo.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());
                        QRCodeObject toAdd = createQRCodeObject(document);
                        qrDataList.add(toAdd);
                        qrAdapter.notifyDataSetChanged();
                        qrCodesFetched.incrementAndGet();

                        if (qrCodesFetched.get() == playerRef.getQrCodes().size()) {
                            updateUI(playerRef);
                        }
                    } else {
                        Log.d("!EXISTS", "No such document");
                    }
                } else {
                    Log.d("TASK FAILED", "get failed with ", task.getException());
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_player_profile);

        //for when viewing other profiles, still has access to curent user name
        String currentUser = getIntent().getStringExtra("currentUser");

        //reference to current player profile being viewed
        String playerView = getIntent().getStringExtra("viewUser");
        //print to console to check if correct player is being viewed

        // Check if the playerView is null
        if (playerView == null) {
            Log.e(TAG, "playerView is null");
            return;
        }

        db = FirebaseFirestore.getInstance();

        qrList = findViewById(R.id.playerQRList);
        qrDataList = new ArrayList<>();
        qrAdapter = new QrCustomAdapter(this, qrDataList);
        qrList.setAdapter(qrAdapter);

        playerScoreTV = findViewById(R.id.playerScoreTextView);
        playerNameTV = findViewById(R.id.playerNameTextView);
        playerQRCountTV = findViewById(R.id.playerQRcountTextView);
        playerLowestTV = findViewById(R.id.lowestQRTextView);
        playerHighestTV = findViewById(R.id.highestQRTextView);
        playerEstimatedRankingTV = findViewById(R.id.playerEstimatedRankingTV);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        DocumentReference playerInfo = db.collection("users").document(playerView);
        playerInfo.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());
                    Player playerRef = document.toObject(Player.class);
                    AtomicInteger qrCodesFetched = new AtomicInteger(0);
                    fetchQRData(playerRef, qrCodesFetched);
                } else {
                    Log.d("!EXISTS", "No such document");
                }
            } else {
                Log.d("TASK FAILED", "get failed with ", task.getException());
            }
        });

        qrList.setOnItemClickListener((adapterView, view, i, l) -> {
            QRCodeObject selectedQR = qrDataList.get(i);
            Intent intent = new Intent(viewPlayerProfile.this, viewQRCode.class);
            intent.putExtra("qrCodeObject", selectedQR);
            intent.putExtra("usersID", currentUser);
            startActivity(intent);
        });

        qrList.setOnItemLongClickListener((adapterView, view, i, l) -> {
            if (currentUser.equals(playerView)) {
                QRCodeObject clickQR = qrAdapter.getItem(i);
                playerInfo.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("EXISTS", "DocumentSnapshot data: " + document.getData());
                            Player playerRef = document.toObject(Player.class);
                            playerRef.getQrCodes().remove(clickQR.getCodeName());
                            playerInfo.update("qrCodes", playerRef.getQrCodes());
                            qrDataList.clear();

                            if (playerRef.getQrCodes().size() == 0) {
                                Toast.makeText(viewPlayerProfile.this, "Deleted all owned QR codes!",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            AtomicInteger qrCodesFetched = new AtomicInteger(0);
                            fetchQRData(playerRef, qrCodesFetched);
                        } else {
                            Log.d("!EXISTS", "No such document part 2");
                        }
                    } else {
                        Log.d("TASK FAILED", "get failed with ", task.getException());
                    }
                });
            }
            return true;
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

    public interface FetchPlayersCallback {
        void onComplete(List<Player> players);
    }

}
