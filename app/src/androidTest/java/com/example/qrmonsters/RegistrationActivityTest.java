package com.example.qrmonsters;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RegistrationActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(RegistrationActivity.class, true,
            true);

    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void register(){

        solo.assertCurrentActivity("Wrong Activity", RegistrationActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username_edit_text), "Hawah");
        solo.enterText((EditText) solo.getView(R.id.email_edit_text), "H@e.c");
        solo.enterText((EditText) solo.getView(R.id.phone_number_edit_text), "777");
        solo.clickOnButton("Register");

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

    }

    @After
    public void teardown() throws Exception{


        Activity activity = rule.getActivity();

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        SharedPreferences sharedPrefs = context.getSharedPreferences("UserDetails",
                MODE_PRIVATE);

        String userID = sharedPrefs.getString("userID", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userID).delete();


    }


}
