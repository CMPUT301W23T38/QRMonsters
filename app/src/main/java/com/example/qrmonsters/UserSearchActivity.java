package com.example.qrmonsters;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserSearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        // Initialize UI components
        searchEditText = findViewById(R.id.search_edit_text);
        searchButton = findViewById(R.id.search_button);
        userRecyclerView = findViewById(R.id.user_recycler_view);

        // Set up RecyclerView with UserAdapter
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString();
                searchForUsers(searchText);
            }
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void searchForUsers(String searchText) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        Query searchQuery = usersRef.whereEqualTo("username", searchText);
        searchQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Player> userList = new ArrayList<>();
                    for (Player user : task.getResult().toObjects(Player.class)) {
                        userList.add(user);
                    }
                    userAdapter = new UserAdapter(userList);
                    userRecyclerView.setAdapter(userAdapter);
                } else {
                    Toast.makeText(UserSearchActivity.this, "User search failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "User search failed: " + task.getException().getMessage());
                }
                
            }

        });
    }

}
