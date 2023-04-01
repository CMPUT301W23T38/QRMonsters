package com.example.qrmonsters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Player> userList;
    private OnUserClickListener onUserClickListener;
    /**
     * Constructor for UserAdapter
     * @param userList List of Players to be displayed
     */
    public UserAdapter(List<Player> userList) {
        this.userList = userList;
    }

    public void setUsers(List<Player> userList) {
        this.userList = userList;
        this.onUserClickListener = onUserClickListener;
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
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Player user = userList.get(position);
        //holder.bind(user);
        holder.usernameTextView.setText(user.getUsername());
        holder.phoneTextView.setText(user.getPhoneNumber());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), viewPlayerProfile.class);
            intent.putExtra("currentUser", user.getUserId());
            intent.putExtra("viewUser", user.getUserId());
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    /**
     * UserViewHolder is a static inner class that represents each item in the RecyclerView
     */
    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView phoneTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_text_view);
            phoneTextView = itemView.findViewById(R.id.phone_text_view);
        }
    }

    public interface OnUserClickListener {
        void onUserClick(Player user);
    }
}
