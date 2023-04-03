package com.example.qrmonsters;

import static android.content.Context.MODE_PRIVATE;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class viewRankingsTest {

    private Solo solo;

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(HomeActivity.class, true,
            true);

    @Before
    public void setUp() throws Exception {

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

        Player user = new Player("S39gHoJQtgOuaH5nYE1U", "yehdhs", "r@v.com"
                , "196864");

        usersRef.document("S39gHoJQtgOuaH5nYE1U").set(user);
        usersRef.document("test_user").set(user);


    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void viewRankingTest(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");

        solo.clickOnButton("View Leaderboard");

        solo.assertCurrentActivity("leaderboard Activity", LeaderboardActivity.class);
        assertTrue(solo.waitForText("Kkst12"));
        assertTrue(solo.waitForText("1487"));

        solo.pressSpinnerItem(0,1);
        assertTrue(solo.waitForText("Kkst12"));
        assertTrue(solo.waitForText("248"));

        solo.pressSpinnerItem(0,2);
        assertTrue(solo.waitForText("Kkst12"));
        assertTrue(solo.waitForText("39"));


    }

    @After
    public void teardown() throws Exception{

    }
}
