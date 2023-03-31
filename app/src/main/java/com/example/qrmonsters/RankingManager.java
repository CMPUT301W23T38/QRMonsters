package com.example.qrmonsters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankingManager {

    public static int getEstimatedRanking(int playerHighestScore, List<Player> allPlayers) {
        List<Integer> highestIndividualScores = new ArrayList<>();
        for (Player player : allPlayers) {
            highestIndividualScores.add(player.getHighestIndividualScore());
        }
        Collections.sort(highestIndividualScores, Collections.reverseOrder());

        int ranking = 1;
        for (int score : highestIndividualScores) {
            if (playerHighestScore < score) {
                ranking++;
            } else {
                break;
            }
        }
        return ranking;
    }
    
}
