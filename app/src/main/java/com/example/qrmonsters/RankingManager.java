package com.example.qrmonsters;

import java.util.Collections;
import java.util.List;

public class RankingManager {
    public static int getEstimatedRanking(int playerHighestScore, List<Player> allPlayers) {
        int rank = 1;
        int playersWithSameScore = 0;

        for (Player player : allPlayers) {
            int currentPlayerHighestScore = Collections.max(player.getQrScores());
            if (playerHighestScore < currentPlayerHighestScore) {
                rank++;
            } else if (playerHighestScore == currentPlayerHighestScore) {
                playersWithSameScore++;
            }
        }

        if (playersWithSameScore > 1) {
            return rank + playersWithSameScore - 1;
        } else {
            return rank;
        }
    }
}

