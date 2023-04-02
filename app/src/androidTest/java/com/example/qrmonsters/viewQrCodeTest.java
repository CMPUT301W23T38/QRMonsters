package com.example.qrmonsters;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.ListView;

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

public class viewQrCodeTest {

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

        Location londonderry = new Location("");

        londonderry.setLatitude(53.60291953603656);
        londonderry.setLongitude(-113.44608664603429);

        Map<String, String> comments = new HashMap<>();
        comments.put("homer", "pretty");

        QRCodeObject qr1 = new QRCodeObject("BaBaBaBa454", "BaBaBaBa454546422",
                45, londonderry, (HashMap<String, String>) comments);


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


        usersRef.document("S39gHoJQtgOuaH5nYE1U").update("qrCodes",
                FieldValue.arrayUnion(qr1.getCodeName()));

    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void viewQRTest(){


        solo.clickOnButton("View My QR Codes");
        assertTrue(solo.waitForText("BaBaBaBa454", 1, 2000));

        ListView list = solo.getCurrentViews(ListView.class).get(0);


        solo.clickInList(0,0);

        solo.assertCurrentActivity("view QR activity", viewQRCode.class);
        assertTrue(solo.waitForText("BaBaBaBa454"));
        assertTrue(solo.waitForText("45"));

        solo.clickOnButton("View QRcode Location");
        solo.goBack();

        solo.clickOnButton("View QRcode Comments");
        solo.assertCurrentActivity("comments sections", viewQRComment.class);

        assertTrue(solo.waitForText("homer"));
        assertTrue(solo.waitForText("pretty"));

        solo.goBack();

        solo.clickOnButton("Comment on QRCode");
        solo.typeText(0, "ok");
        solo.clickOnButton("Confirm");

        solo.clickOnButton("View QRcode Comments");
        solo.assertCurrentActivity("comments sections", viewQRComment.class);

        assertTrue(solo.waitForText("yehdhs"));
        assertTrue(solo.waitForText("ok"));

    }

    @After
    public void teardown() throws Exception{

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document("S39gHoJQtgOuaH5nYE1U").delete();

        QRCodeObject qr1 = new QRCodeObject("BaBaBaBa454", "BaBaBaBa454546422",
                45, null);

        db.collection("qrCodes").document(qr1.getCodeName()).delete();

    }


}
