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

public class viewQRComment extends AppCompatActivity {

    private viewQRCommentAdapter commentAdapter;
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