package com.example.qrmonsters;
/**

 The LeaderboardActivity class displays a list of players sorted by total score or other criteria selected by the user.
 It loads data from a Firestore database and calculates the total score for each player based on the QR codes they have scanned.
 It also provides a listener for when a player is clicked, which opens a view of their profile.
 */
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LeaderboardActivity extends AppCompatActivity implements LeaderboardAdapter.OnPlayerClickListener {
    private List<Player> players = new ArrayList<>();
    private LeaderboardAdapter leaderboardAdapter;
    private Spinner spinnerSortBy;
    /**
     * This method is called when the activity is created. It initializes the RecyclerView, the Spinner,
     * loads the leaderboard data, and sets the adapter to the RecyclerView.
     *
     * @param savedInstanceState a Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize RecyclerView
        RecyclerView recyclerViewLeaderboard = findViewById(R.id.recyclerView_leaderboard);
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
    /**
     * This method loads the leaderboard data from a Firestore database and calculates the total score for each player
     * based on the QR codes they have scanned.
     */
    private void loadLeaderboardData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        players = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Player player = document.toObject(Player.class);
                            calculateTotalScore(db, Objects.requireNonNull(player).getQrCodes(), (totalScore, highestIndividualScore, numQRCodesScanned) -> {
                                player.setTotalScore(totalScore);
                                //update the player's total score in the database
                                DocumentReference playerRef = db.collection("users").document(player.getUserId());
                                playerRef.update("totalScore", totalScore);
                                player.setHighestIndividualScore(highestIndividualScore);
                                playerRef.update("highestIndividualScore", highestIndividualScore);
                                player.setNumQRCodesScanned(numQRCodesScanned);
                                playerRef.update("numQRCodesScanned", numQRCodesScanned);
                                players.add(player);
                                if (players.size() == task.getResult().size()) {
                                    sortAndUpdateLeaderboard();
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
    /**
     * Sorts the leaderboard based on the user's selected option from the spinner
     * and updates the leaderboard data accordingly.
     */
    private void sortAndUpdateLeaderboard() {
        int selectedOption = spinnerSortBy.getSelectedItemPosition();
        LeaderboardAdapter.SortBy sortBy;

        switch (selectedOption) {
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

        leaderboardAdapter.updateData(players, sortBy);
    }
    /**
     * Calculates the total score for the given QR codes by querying the Firestore database
     * and invokes the onTotalScoreCalculated() method of the provided listener when done.
     * If the list of QR codes is empty, then the total score, highest individual score, and number
     * of QR codes scanned are all zero.
     *
     * @param db The Firestore database instance to use for the query.
     * @param qrCodes The list of QR code IDs to query for total score.
     * @param listener The listener to notify when the total score calculation is complete.
     */
    private void calculateTotalScore(FirebaseFirestore db, List<String> qrCodes, OnTotalScoreCalculatedListener listener) {
        if (qrCodes.isEmpty()) {
            listener.onTotalScoreCalculated(0, 0, 0);
            return;
        }

        int[] totalScore = {0};
        int[] highestIndividualScore = {0};
        int[] completedRequests = {0};
        for (String qrCodeId : qrCodes) {
            db.collection("qrCodes").document(qrCodeId).get().addOnCompleteListener(task -> {
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
            });
        }
    }
    /**
     * Handles the click event for a player item in the leaderboard RecyclerView by
     * launching the view player profile activity with the selected player's username.
     *
     * @param player The player item that was clicked.
     */
    @Override
    public void onPlayerClick(Player player) {
        Intent intent = new Intent(this, viewPlayerProfile.class);
        intent.putExtra("username", player.getUsername());
        startActivity(intent);
    }

    /**
     * Listener interface for notifying when the total score calculation is complete.
     */

    interface OnTotalScoreCalculatedListener {
        void onTotalScoreCalculated(int totalScore, int highestIndividualScore, int numQRCodesScanned);
    }
}
