package com.example.qrmonsters;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.zxing.activity.CaptureActivity;

public class ScannedResult extends AppCompatActivity {
    private final static int REQ_CODE = 1028;
    private Context mContext;
    private TextView mTvResult;
    private ImageView mImageCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_result);

        Intent intent1 = getIntent();

        String theResult = intent1.getStringExtra("TheResult");
        Bitmap theBitmap = (Bitmap) intent1.getParcelableExtra("TheBitmap");

        mTvResult = (TextView) findViewById(R.id.tv_result);
        // This is a TEXTVIEW, not a string, need to be convert before using
        mImageCallback = (ImageView) findViewById(R.id.image_callback);
        // This is the QRCODE  VIEW, need to be convert before using

        mTvResult.setText(theResult);
        try{
            mTvResult.setText(theResult);

            if(theBitmap != null){
                mImageCallback.setImageBitmap(theBitmap);
            }
        }catch (NullPointerException e){
            System.out.println("");
        }

        /**
         * Here is the part after scan, the scanned person's information should have 1 scan history store in an arrayList (The person who scanned)
         *
         * the be scanned person's information should have 1 be scanned history in an arrayList (The person who be scanned)
         */
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        Query searchQuery = usersRef.whereEqualTo("userID", theResult);

        // This line is for searching who has been scanned
        Log.d("777", String.valueOf(searchQuery.get()));

        }
}