package com.example.qrmonsters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.qrmonsters.Player;
import com.example.qrmonsters.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LeaderboardActivity extends AppCompatActivity implements LeaderboardAdapter.OnPlayerClickListener {

    private RecyclerView recyclerViewLeaderboard;
    private List<Player> players = new ArrayList<>();
    private LeaderboardAdapter leaderboardAdapter;
    private Spinner spinnerSortBy;
    private boolean isInitialSetupComplete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize RecyclerView
        recyclerViewLeaderboard = findViewById(R.id.recyclerView_leaderboard);
        recyclerViewLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Spinner
        spinnerSortBy = findViewById(R.id.spinner_sort_by);
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortAndUpdateLeaderboard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Load leaderboard data
        loadLeaderboardData();

        // Initialize Adapter
        leaderboardAdapter = new LeaderboardAdapter(players, LeaderboardAdapter.SortBy.TOTAL_SCORE, this);
        recyclerViewLeaderboard.setAdapter(leaderboardAdapter);
    }



    private void loadLeaderboardData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            players = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Player player = document.toObject(Player.class);
                                calculateTotalScore(db, player, player.getQrCodes(), new OnTotalScoreCalculatedListener() {
                                    @Override
                                    public void onTotalScoreCalculated(int totalScore, int highestIndividualScore, int numQRCodesScanned) {
                                        player.setTotalScore(totalScore);
                                        player.setHighestIndividualScore(highestIndividualScore);
                                        player.setNumQRCodesScanned(numQRCodesScanned);
                                        players.add(player);
                                        if (players.size() == task.getResult().size()) {
                                            // Load the leaderboard once all data is loaded
                                            sortAndUpdateLeaderboard();
//                                            leaderboardAdapter = new LeaderboardAdapter(players);
//                                            recyclerViewLeaderboard.setAdapter(leaderboardAdapter);
//                                            isInitialSetupComplete = true;
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void sortAndUpdateLeaderboard() {
        int selectedOption = spinnerSortBy.getSelectedItemPosition();
        LeaderboardAdapter.SortBy sortBy;

        switch (selectedOption) {
            case 0:
                sortBy = LeaderboardAdapter.SortBy.TOTAL_SCORE;
                break;
            case 1:
                sortBy = LeaderboardAdapter.SortBy.HIGHEST_INDIVIDUAL_SCORE;
                break;
            case 2:
                sortBy = LeaderboardAdapter.SortBy.NUM_QR_CODES_SCANNED;
                break;
            default:
                sortBy = LeaderboardAdapter.SortBy.TOTAL_SCORE;
        }

        Log.d("sortAndUpdate", "Selected option: " + selectedOption);
        Log.d("sortAndUpdate", "sortBy: " + sortBy);

//        leaderboardAdapter.setSortBy(sortBy);
//        leaderboardAdapter = new LeaderboardAdapter(players, sortBy);
//        recyclerViewLeaderboard.setAdapter(leaderboardAdapter);
//        leaderboardAdapter.notifyDataSetChanged();

        leaderboardAdapter.updateData(players, sortBy);

    }

    private void calculateTotalScore(FirebaseFirestore db, Player player, List<String> qrCodes, OnTotalScoreCalculatedListener listener) {
        if (qrCodes.isEmpty()) {
            listener.onTotalScoreCalculated(0, 0, 0);
            return;
        }

        int[] totalScore = {0};
        int[] highestIndividualScore = {0};
        int[] completedRequests = {0};
        for (String qrCodeId : qrCodes) {
            db.collection("qrCodes").document(qrCodeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot qrCodeDoc = task.getResult();
                        Long scoreLong = qrCodeDoc.getLong("codeScore");
                        if (scoreLong != null) {
                            int score = scoreLong.intValue();
                            totalScore[0] += score;
                            if (score > highestIndividualScore[0]) {
                                highestIndividualScore[0] = score;
                            }
                        }
                    }
                    completedRequests[0]++;
                    if (completedRequests[0] == qrCodes.size()) {
                        int numQRCodesScanned = qrCodes.size();
                        listener.onTotalScoreCalculated(totalScore[0], highestIndividualScore[0], numQRCodesScanned);
                    }
                }
            });
        }
    }

    @Override
    public void onPlayerClick(Player player) {
        Intent intent = new Intent(this, viewPlayerProfile.class);
        intent.putExtra("username", player.getUsername());
        startActivity(intent);
    }

    interface OnTotalScoreCalculatedListener {
        void onTotalScoreCalculated(int totalScore, int highestIndividualScore, int numQRCodesScanned);
    }
}
