package com.example.qrmonsters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**

 This class represents the activity for viewing QR code comments.
 It extends AppCompatActivity and displays a ListView of comments for a given QR code.
 */
public class viewQRComment extends AppCompatActivity {

    private viewQRCommentAdapter commentAdapter;
    /**

     Called when the activity is created. Sets the layout and initializes the ListView and adapter.
     @param savedInstanceState the saved instance state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcomment);

        Intent intent1 = getIntent();
        String codeName = intent1.getStringExtra("The_Code_Name");
        HashMap<String, String> comments = (HashMap<String, String>) getIntent().getSerializableExtra("comments");
        //String commenttts = intent1.getStringExtra("comments");

        ListView comment_view = findViewById(R.id.comments);

        ArrayList<Map.Entry<String, String>> data = new ArrayList<>(comments.entrySet());
        commentAdapter = new viewQRCommentAdapter(this, data);
        comment_view.setAdapter(commentAdapter);
    }
}