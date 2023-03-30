package com.example.qrmonsters;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
public class viewQRCode extends AppCompatActivity {
    TextView qrNameTV, qrScoreTV, qrName1TV, qrNameTV2, qrNameTV3, qrNameTV4;
    TextView qrNumTV1, qrNumTV2, qrNumTV3;
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

        qrName1TV = findViewById(R.id.sylabble1);
        qrNameTV2 = findViewById(R.id.sylabble2);
        qrNameTV3 = findViewById(R.id.sylabble3);
        qrNameTV4 = findViewById(R.id.sylabble4);
        qrNumTV1 = findViewById(R.id.number1);
        qrNumTV2 = findViewById(R.id.number2);
        qrNumTV3 = findViewById(R.id.number3);

        SharedPreferences preferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        qrNameTV.setText("Name: " + qrCodeObject.getCodeName());


        char[] qrNameCharArray = qrCodeObject.getCodeName().toCharArray();

        String syl1 = textToAsciiLetters.getAscii(qrNameCharArray[0], qrNameCharArray[1]);
        String syl2 = textToAsciiLetters.getAscii(qrNameCharArray[2], qrNameCharArray[3]);
        String syl3 = textToAsciiLetters.getAscii(qrNameCharArray[4], qrNameCharArray[5]);
        String syl4 = textToAsciiLetters.getAscii(qrNameCharArray[6], qrNameCharArray[7]);
        String num1 = textToAsciiNumbers.getAscii(qrNameCharArray[8]);
        String num2 = textToAsciiNumbers.getAscii(qrNameCharArray[9]);
        String num3 = textToAsciiNumbers.getAscii(qrNameCharArray[10]);

        qrName1TV.setText(syl1);
        qrNameTV2.setText(syl2);
        qrNameTV3.setText(syl3);
        qrNameTV4.setText(syl4);
        qrNumTV1.setText(num1);
        qrNumTV2.setText(num2);
        qrNumTV3.setText(num3);



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

                if(qrLocation != null){

                    LatLng currLoc = new LatLng(qrLocation.getLatitude(),
                            qrLocation.getLongitude());

                    new currLocationFragment(currLoc).show(getSupportFragmentManager(),
                            "CURR_LOC");

                }
                else {

                    Toast.makeText(viewQRCode.this, "No Location Data Available!",
                            Toast.LENGTH_SHORT).show();
                }

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

                            if(qrCodeObject.getComments().keySet().contains(username)){

                                Toast.makeText(viewQRCode.this,
                                        "You have already commented on this QR code!",
                                        Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(viewQRCode.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                                qrCodeObject.addComment(username ,editText.getText().toString());
                                //myuserID  THIS IS WHAT THE CURRENTUSER'S USERID, SHOULD BE USEFUL FOR UPDATING COMMENTS;
                                // update after 8:30 : addComment needs update

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference qrComments = db.collection("qrCodes")
                                        .document(qrCodeObject.getCodeName());

                                qrComments.update("comments", qrCodeObject.getComments());
                            }


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
