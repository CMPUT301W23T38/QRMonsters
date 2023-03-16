package com.example.qrmonsters;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class viewQRCode extends AppCompatActivity {
    TextView qrNameTV, qrScoreTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcode);


        QRCodeObject qrCodeObject = getIntent().getParcelableExtra("qrCodeObject");

        //Set the TextViews to the QRCodeObject's properties
        qrNameTV = findViewById(R.id.qrNameTextView);
        qrScoreTV = findViewById(R.id.qrScoreTextView);

        qrNameTV.setText("Name: " + qrCodeObject.getCodeName());
        qrScoreTV.setText("Score: " + qrCodeObject.getCodeScore().toString());
        

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


    }
}