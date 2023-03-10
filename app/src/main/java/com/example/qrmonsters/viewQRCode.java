package com.example.qrmonsters;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class viewQRCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcode);

        QRCodeObject qrCode = getIntent().getParcelableExtra("QRCODE");

        TextView qrNameTV;
        TextView qrScoreTV;


        //ADD FRAGMENTS TO VIEW COMMENTS AND ADD COMMENTS
        Button viewCommentsButton = findViewById(R.id.viewCommentsButton);
        Button commentButton = findViewById(R.id.commentButton);
        Button qrLocButton = findViewById(R.id.viewLocationButton);




        qrLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Location qrLocation = qrCode.getCodeLocation();

                LatLng currLoc = new LatLng(qrLocation.getLatitude(),
                        qrLocation.getLongitude());

                new currLocationFragment(currLoc).show(getSupportFragmentManager(),
                        "CURR_LOC");


            }
        });




    }
}