package com.example.qrmonsters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class QRMonstersTest {
    /**
     * Ensure that the user is able to register successfully
     */
    @Test
    public void testUserRegistration() {
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.username_edit_text)).perform(typeText("testuser123"));
        onView(withId(R.id.email_edit_text)).perform(typeText("testuser123@gmail.com"));
//        onView(withId(R.id.home_screen)).check(matches(isDisplayed()));
    }

    /**
     * Ensure that the user is able to add a QR code successfully
     */
//    @Test
//    public void testQRCodeAddition() {
//        onView(withId(R.id.add_qr_button)).perform(click());
//        onView(withId(R.id.qr_code_edittext)).perform(typeText("123456"));
//        onView(withId(R.id.qr_code_add_button)).perform(click());
//        onView(withId(R.id.qr_code_list)).check(matches(hasDescendant(withText("123456"))));
//    }

    /**
     * Ensure that the user is able to search for other users successfully
     */
    @Test
    public void testUserSearch() {
        onView(withId(R.id.search_button)).perform(click());
        onView(withId(R.id.search_edit_text)).perform(typeText("testuser123"));
        onView(withId(R.id.user_recycler_view)).check(matches(hasDescendant(withText("testuser123"))));
    }
}


