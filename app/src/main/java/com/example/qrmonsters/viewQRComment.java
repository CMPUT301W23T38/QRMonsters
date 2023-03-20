package com.example.qrmonsters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class viewQRComment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcomment);

        Intent intent1 = getIntent();
        String codeName = intent1.getStringExtra("The_Code_Name");
        HashMap<String, String> map = (HashMap<String, String>) getIntent().getSerializableExtra("comments");
        // Not working for now but not influencing anything related (blank page)
        for (String key : map.keySet()) {
            Log.v("wjw","The key you got = " + key);
            Log.v("wjw","The value you got = " + map.get(key));
        }
    }
}