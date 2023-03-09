package com.example.qrmonsters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

/**
 UserAdapter is a class that extends RecyclerView.Adapter and is used to bind Player data to the user_item view
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Player> userList;
    /**
     * Constructor for UserAdapter
     * @param userList List of Players to be displayed
     */
    public UserAdapter(List<Player> userList) {
        this.userList = userList;
    }
    /**
     * Updates the user list and notifies the adapter that the data set has changed
     * @param userList List of Players to be displayed
     */
    public void setUsers(List<Player> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }
    /**
     * Creates a new UserViewHolder object when there are no existing ones to reuse
     * @param parent ViewGroup containing the RecyclerView
     * @param viewType Type of view being created
     * @return A new UserViewHolder object
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }
    /**
     * Binds the Player data to the user_item view
     * @param holder UserViewHolder containing the user_item view
     * @param position Position of the Player object in the userList
     */
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Player user = userList.get(position);
        holder.usernameTextView.setText(user.getUsername());
        holder.phoneTextView.setText(user.getPhoneNumber());
    }

    /**
     * Returns the number of Players in the userList
     * @return The number of Players in the userList
     */
    @Override
    public int getItemCount() {
        return userList.size();
    }
    /**
     * UserViewHolder is a static inner class that represents each item in the RecyclerView
     */
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView phoneTextView;
        /**
         * Constructor for UserViewHolder
         * @param itemView The view for each item in the RecyclerView
         */

        public UserViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_text_view);
            phoneTextView = itemView.findViewById(R.id.phone_text_view);
        }
    }
}
