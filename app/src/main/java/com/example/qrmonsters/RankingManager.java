package com.example.qrmonsters;

import java.util.Collections;
import java.util.List;
/**
 A class for managing the ranking of a player based on their highest score compared to all other players.
 */
public class RankingManager {
    /**
     * Calculates an estimated ranking for a player based on their highest score compared to all other players.
     *
     * @param playerHighestScore The highest score of the player.
     * @param allPlayers A list of all players to compare the score with.
     * @return An estimated ranking for the player based on their highest score.
     */

    public static int getEstimatedRanking(int playerHighestScore, List<Player> allPlayers) {
        int rank = 1;
        int playersWithSameScore = 0;

        for (Player player : allPlayers) {
            if (player.getQrScores().isEmpty()) {
                continue;
            }

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