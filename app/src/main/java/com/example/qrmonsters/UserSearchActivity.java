package com.example.qrmonsters;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserSearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        // Initialize UI components
        searchEditText = findViewById(R.id.search_edit_text);
        Button searchButton = findViewById(R.id.search_button);
        userRecyclerView = findViewById(R.id.user_recycler_view);

        // Set up RecyclerView with UserAdapter
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(v -> {
            String searchText = searchEditText.getText().toString();
            searchForUsers(searchText);
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

    }

    public void searchForUsers(String searchText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        Query searchQuery = usersRef.whereEqualTo("username", searchText);
        searchQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Player> userList = new ArrayList<>(task.getResult().toObjects(Player.class));
                userAdapter = new UserAdapter(userList);
                userRecyclerView.setAdapter(userAdapter);
            } else {
                Toast.makeText(UserSearchActivity.this, "User search failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "User search failed: " + task.getException().getMessage());
            }

        });
    }

}
