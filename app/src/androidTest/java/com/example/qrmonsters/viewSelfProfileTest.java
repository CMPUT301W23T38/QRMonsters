package com.example.qrmonsters;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.decode.Intents;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void viewSelfProfileTest(){


        solo.clickOnButton("View My QR Codes");
        assertTrue(solo.waitForText("22", 1, 2000));
        assertTrue(solo.waitForText("41", 2, 2000));
        assertTrue(solo.waitForText("6", 2, 2000));
        assertTrue(solo.waitForText("24", 1, 2000));




    }

}
