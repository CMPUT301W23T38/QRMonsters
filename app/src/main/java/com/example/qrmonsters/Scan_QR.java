package com.example.qrmonsters;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.common.BitmapUtils;


/**
 * Scan_QR class is now on-hold, nothing related to this class for now.
 * 2023-03-10 07:27am
 */





/**

 Scan_QR is an activity that allows the user to scan and create QR codes. It utilizes the ZXing
 library to capture QR codes and generate new ones.
 */
public class Scan_QR extends AppCompatActivity implements View.OnClickListener {

    private Button mBtn1;
    private EditText mEt;
    private Button mBtn2;
    private ImageView mImage;
    private final static int REQ_CODE = 1028;
    private Context mContext;
    private TextView mTvResult;
    private ImageView mImageCallback;
    /**
     * Initializes the activity's UI components.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        initView();
        mContext = this;
    }
    /**
     * Initializes the activity's UI components and sets their click listeners.
     */
    private void initView() {
        mBtn1 = (Button) findViewById(R.id.btn1);

        mBtn1.setOnClickListener(this);
        mEt = (EditText) findViewById(R.id.et);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn2.setOnClickListener(this);
        mImage = (ImageView) findViewById(R.id.image);
        mImage.setOnClickListener(this);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mTvResult.setOnClickListener(this);
        mImageCallback = (ImageView) findViewById(R.id.image_callback);
        mImageCallback.setOnClickListener(this);
    }
    /**
     * Handles clicks on the activity's buttons.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
//                startActivity(new Intent(MainActivity.this, CaptureActivity.class));
                Intent intent = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(intent, REQ_CODE);
                break;
            case R.id.btn2:
                mImage.setVisibility(View.VISIBLE);
                mImageCallback.setVisibility(View.GONE);
                mTvResult.setVisibility(View.GONE);

                String content = mEt.getText().toString().trim();
                content += '\n' + String.valueOf(SHA256andScore.getScore(SHA256andScore.getSha256Str(content))) +'\n' + SHA256andScore.getSha256Str(content);
                Bitmap bitmap = null;

                try {
                    bitmap = BitmapUtils.create2DCode(content);
                    mTvResult.setVisibility(View.GONE);
                    mImage.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
/**
 * Handles the result of the ZXing library's capture activity.
 *
 * @param requestCode The request code used to launch the activity.
 * @param resultCode The result code returned by the activity.
 * @param data The data returned by the activity.
 */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            mImage.setVisibility(View.GONE);
            mTvResult.setVisibility(View.VISIBLE);
            mImageCallback.setVisibility(View.VISIBLE);
            try{
                String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
                Bitmap bitmap = data.getParcelableExtra(CaptureActivity.SCAN_QRCODE_BITMAP);
                mTvResult.setText("scan result："+result);
                showToast("scan result：" + result);
                if(bitmap != null){
                    mImageCallback.setImageBitmap(bitmap);
                }

            }catch (NullPointerException e){
                System.out.println("");
            }



        }


    }
    /**
     * show a pop up message of string message
     * @param msg string of the message
     */
    private void showToast(String msg) {
        Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
    }
}