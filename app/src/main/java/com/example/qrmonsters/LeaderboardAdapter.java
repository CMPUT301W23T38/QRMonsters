package com.example.qrmonsters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {

    private List<Player> users;
    private SortBy currentSortBy;
    private OnPlayerClickListener onPlayerClickListener;


    public enum SortBy {
        TOTAL_SCORE,
        HIGHEST_INDIVIDUAL_SCORE,
        NUM_QR_CODES_SCANNED
    }

    public LeaderboardAdapter(List<Player> playerList, SortBy sortBy, OnPlayerClickListener onPlayerClickListener) {
        this.users = new ArrayList<>(playerList);
        this.currentSortBy = sortBy;
        this.onPlayerClickListener = onPlayerClickListener;
        sortUsers();
    }


    private void sortUsers() {
        Collections.sort(users, (p1, p2) -> {
            switch (currentSortBy) {
                case TOTAL_SCORE:
                    return Integer.compare(p2.getTotalScore(), p1.getTotalScore());
                case HIGHEST_INDIVIDUAL_SCORE:
                    return Integer.compare(p2.getHighestIndividualScore(), p1.getHighestIndividualScore());
                case NUM_QR_CODES_SCANNED:
                    return Integer.compare(p2.getNumQRCodesScanned(), p1.getNumQRCodesScanned());
                default:
                    return 0;
            }
        });
    }

    public void updateData(List<Player> playerList, SortBy sortBy) {
        this.users.clear();
        this.users.addAll(playerList);
        this.currentSortBy = sortBy;
        sortUsers();
        notifyDataSetChanged();
    }

    public void setSortBy(SortBy sortBy) {
        Collections.sort(users, (p1, p2) -> {
            switch (sortBy) {
                case TOTAL_SCORE:
                    return Integer.compare(p2.getTotalScore(), p1.getTotalScore());
                case HIGHEST_INDIVIDUAL_SCORE:
                    return Integer.compare(p2.getHighestIndividualScore(), p1.getHighestIndividualScore());
                case NUM_QR_CODES_SCANNED:
                    return Integer.compare(p2.getNumQRCodesScanned(), p1.getNumQRCodesScanned());
                default:
                    return 0;
            }
        });
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        Player user = users.get(position);
        holder.rankTextView.setText(String.valueOf(position + 1) + ".");
        holder.usernameTextView.setText(user.getUsername());
//        holder.usernameTextView.setOnClickListener(v -> {
//            if (onPlayerClickListener != null) {
//                onPlayerClickListener.onPlayerClick(user);
//            }
//        });
        holder.usernameTextView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), viewPlayerProfile.class);
            intent.putExtra("currentUser", user.getUserId());
            intent.putExtra("viewUser", user.getUserId());
            view.getContext().startActivity(intent);
        });

        int score;
        switch (currentSortBy) {
            case TOTAL_SCORE:
                score = user.getTotalScore();
                break;
            case HIGHEST_INDIVIDUAL_SCORE:
                score = user.getHighestIndividualScore();
                break;
            case NUM_QR_CODES_SCANNED:
                score = user.getNumQRCodesScanned();
                break;
            default:
                score = user.getTotalScore();
        }
        holder.scoreTextView.setText(String.valueOf(score));
    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        TextView rankTextView;
        TextView usernameTextView;
        TextView scoreTextView;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.textView_rank);
            usernameTextView = itemView.findViewById(R.id.textView_username);
            scoreTextView = itemView.findViewById(R.id.textView_score);
        }
    }

    public interface OnPlayerClickListener {
        void onPlayerClick(Player player);
    }

}
