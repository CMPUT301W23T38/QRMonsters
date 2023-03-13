package com.example.qrmonsters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.platform.app.InstrumentationRegistry;

import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest1 {



        private Solo solo;
        @Rule
        public ActivityTestRule activityRule = new ActivityTestRule(MainActivity.class, true, true);

        @Before
        public void setUp() throws Exception {
            solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());


        }
        @Test
        public void start() throws Exception{
            Activity activity = activityRule.getActivity();
        }

        @Test
        public void testStartOfRegisterActivity() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isRegistered", false);
            editor.commit();
            Intent intent = new Intent(context, MainActivity.class);
            Activity activity = activityRule.launchActivity(intent);
            solo.waitForActivity(RegistrationActivity.class, 2000);
            solo.assertCurrentActivity("Expected RegistrationActivity", RegistrationActivity.class);

        }

        @Test
        public void testStartOfHomeActivity() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isRegistered", true);
            editor.commit();
            Intent intent = new Intent(context, MainActivity.class);
            Activity activity = activityRule.launchActivity(intent);
            solo.waitForActivity(HomeActivity.class, 2000);
            solo.assertCurrentActivity("Expected RegistrationActivity", HomeActivity.class);

        }
        @Test
        public void checklist() throws Exception{
            solo.assertCurrentActivity("wrong activity",MainActivity.class);

        }

        @After
        public void tearDown() throws Exception {
            solo.finishOpenedActivities();
        }
    }

