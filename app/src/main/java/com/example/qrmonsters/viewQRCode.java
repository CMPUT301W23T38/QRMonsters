package com.example.qrmonsters;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import java.util.*;
public class viewQRCode extends AppCompatActivity {
    TextView qrNameTV, qrScoreTV;
    String myuserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcode);


        QRCodeObject qrCodeObject = getIntent().getParcelableExtra("qrCodeObject");
        String myuserID = getIntent().getStringExtra("usersID");
        //Set the TextViews to the QRCodeObject's properties
        qrNameTV = findViewById(R.id.qrNameTextView);
        qrScoreTV = findViewById(R.id.qrScoreTextView);

        qrNameTV.setText("Name: " + qrCodeObject.toString());

        // Was trying to make qrCodeObject.toString() to see if I can mannually add a code inside there.
        // But not working, the code used for testing named 5vhp 08q
        // Might be the syntax error

        qrScoreTV.setText("Score: " + qrCodeObject.getCodeScore().toString());
        String qrCommTV = qrCodeObject.getComments().toString();
        //Log.v("Not", qrCommTV);

        //ADD FRAGMENTS TO VIEW COMMENTS AND ADD COMMENTS
        Button viewCommentsButton = findViewById(R.id.viewCommentsButton);
        Button commentButton = findViewById(R.id.commentButton);
        Button qrLocButton = findViewById(R.id.viewLocationButton);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());
        qrLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Location qrLocation = qrCodeObject.getCodeLocation();

                LatLng currLoc = new LatLng(qrLocation.getLatitude(),
                        qrLocation.getLongitude());

                new currLocationFragment(currLoc).show(getSupportFragmentManager(),
                        "CURR_LOC");
            }
        });

        viewCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(viewQRCode.this, viewQRComment.class);
                intent1.putExtra("The_Code_Name", qrCodeObject.getCodeName());

                intent1.putExtra("comments",(Serializable)qrCodeObject.getComments());


                startActivity(intent1);
            }
        });
        final EditText editText = new EditText(viewQRCode.this);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder inputDialog = new AlertDialog.Builder(viewQRCode.this);
                inputDialog.setTitle("Please enter your comment:").setView(editText);
                inputDialog.setPositiveButton("Confirm",
                        (dialog, which) -> {
                                Toast.makeText(viewQRCode.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                                qrCodeObject.addComment(editText.getText().toString());
                                //myuserID  THIS IS WHAT THE CURRENTUSER'S USERID, SHOULD BE USEFUL FOR UPDATING COMMENTS;
                                // update after 8:30 : addComment needs update
                                });
                inputDialog.setNegativeButton("Cancel",
                        (dialog, which) -> {
                            //...To-do
                            Log.v("Not","Nothing has been added");
                        }).show();
            }
        });
        }
}
