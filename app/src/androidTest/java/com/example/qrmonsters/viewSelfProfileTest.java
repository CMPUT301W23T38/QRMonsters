package com.example.qrmonsters;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class viewSelfProfileTest {
    private Solo solo;

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(HomeActivity.class, true,
            true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPrefs = context.getSharedPreferences("UserDetails",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();


        editor.putString("userID", "S39gHoJQtgOuaH5nYE1U");
        editor.putString("username", "yehdhs");
        editor.putString("email", "r@v.com");
        editor.putString("phoneNumber", "196864");
        editor.putBoolean("isRegistered", true);
        editor.apply();
        editor.commit();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        CollectionReference qrRef = db.collection("qrCodes");

        Player user = new Player("S39gHoJQtgOuaH5nYE1U", "yehdhs", "r@v.com"
                , "196864");

        usersRef.document("S39gHoJQtgOuaH5nYE1U").set(user);

        QRCodeObject qr1 = new QRCodeObject("londonderry", "fbhefbhebfqd",
                45, null);

        QRCodeObject qr2 = new QRCodeObject("happyMart", "bgbgdgfdgd",
                32, null);

        QRCodeObject qr3 = new QRCodeObject("clareview", "ngbgdhdg",
                32, null);

        Map<String, Object> data = new HashMap<>();
        data.put("codeName", qr1.getCodeName());
        data.put("codeHash", qr1.getCodeHash());
        data.put("codeScore", qr1.getCodeScore());
        data.put("codeLocation", qr1.getCodeLocation());
        data.put("comments", qr1.getComments());
        qrRef.document(qr1.getCodeName())
                .set(data)
                .addOnSuccessListener(unused -> Log.d("Working", "Data added successfully"))
                .addOnFailureListener(e -> Log.d("Working", "Data not added" + e));

        data = new HashMap<>();
        data.put("codeName", qr2.getCodeName());
        data.put("codeHash", qr2.getCodeHash());
        data.put("codeScore", qr2.getCodeScore());
        data.put("codeLocation", qr2.getCodeLocation());
        data.put("comments", qr2.getComments());
        qrRef.document(qr2.getCodeName())
                .set(data)
                .addOnSuccessListener(unused -> Log.d("Working", "Data added successfully"))
                .addOnFailureListener(e -> Log.d("Working", "Data not added" + e));

        data = new HashMap<>();
        data.put("codeName", qr3.getCodeName());
        data.put("codeHash", qr3.getCodeHash());
        data.put("codeScore", qr3.getCodeScore());
        data.put("codeLocation", qr3.getCodeLocation());
        data.put("comments", qr3.getComments());
        qrRef.document(qr3.getCodeName())
                .set(data)
                .addOnSuccessListener(unused -> Log.d("Working", "Data added successfully"))
                .addOnFailureListener(e -> Log.d("Working", "Data not added" + e));


        usersRef.document("S39gHoJQtgOuaH5nYE1U").update("qrCodes",
                FieldValue.arrayUnion(qr1.getCodeName(), qr2.getCodeName()
                ,qr3.getCodeName()));

    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void viewSelfProfileTest(){


        solo.clickOnButton("View My QR Codes");
        assertTrue(solo.waitForText("londonderry", 1, 2000));
        assertTrue(solo.waitForText("happyMart", 1, 2000));
        assertTrue(solo.waitForText("clareview", 1, 2000));


    }

    @After
    public void teardown() throws Exception{

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document("S39gHoJQtgOuaH5nYE1U").delete();

        QRCodeObject qr1 = new QRCodeObject("londonderry", "fbhefbhebfqd",
                45, null);

        QRCodeObject qr2 = new QRCodeObject("happyMart", "bgbgdgfdgd",
                32, null);

        QRCodeObject qr3 = new QRCodeObject("clareview", "ngbgdhdg",
                32, null);

        db.collection("qrCodes").document(qr1.getCodeName()).delete();
        db.collection("qrCodes").document(qr2.getCodeName()).delete();
        db.collection("qrCodes").document(qr2.getCodeName()).delete();


    }

}
