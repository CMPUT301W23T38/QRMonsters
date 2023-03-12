package com.example.qrmonsters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannedResult extends AppCompatActivity {
    private final static int REQ_CODE = 1028;
    private Context mContext;
    private TextView mTvResult;
    private ImageView mImageCallback;
    private Player playerRef;
    ArrayList qrList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_result);

        Intent intent1 = getIntent();

        String theResult = intent1.getStringExtra("TheResult");
        Bitmap theBitmap = (Bitmap) intent1.getParcelableExtra("TheBitmap");
        String currentUser = intent1.getStringExtra("UserID");
        Log.d("UserIDDebug", "onCreate USERID: " + currentUser);
        Location qrLocation = intent1.getParcelableExtra("location");

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

        String hashCode = SHA256andScore.getSha256Str(theResult);
        Integer hashScore = SHA256andScore.getScore(hashCode);

        QRCodeObject qrAdd = new QRCodeObject(theResult, hashCode, hashScore, qrLocation);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference qrRef = db.collection("qrCodes");
        CollectionReference usersRef = db.collection("users");

        DocumentReference userInfo = db.collection("users").document(currentUser);

        userInfo.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            playerRef = documentSnapshot.toObject(Player.class);

                            if(playerRef.getQrCodes().contains(theResult)){

                                Toast.makeText(ScannedResult.this, "Already have this QR Code!",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            userInfo.update("qrCodes", FieldValue.arrayUnion(theResult));

                            qrRef.whereEqualTo("codeName", theResult).get().addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    if(task.getResult().isEmpty()){

                                        Map<String, Object> data = new HashMap<>();

                                        data.put("codeName", qrAdd.getCodeName());
                                        data.put("codeHash", qrAdd.getCodeHash());
                                        data.put("codeScore", qrAdd.getCodeScore());
                                        data.put("codeLocation", qrAdd.getCodeLocation());
                                        data.put("comments", qrAdd.getComments());

                                        qrRef
                                                .document(qrAdd.getCodeName())
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("Working", "Data added successfully");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Working", "Data not added" + e.toString());
                                                    }
                                                });

                                    }
                                }
                            });




                        }
                        else {
                            Toast.makeText(ScannedResult.this, "DOCUMENT DOES NOT EXIST",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ScannedResult.this, "FAILED",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        }
}