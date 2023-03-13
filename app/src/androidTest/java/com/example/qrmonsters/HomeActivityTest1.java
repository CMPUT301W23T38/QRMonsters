package com.example.qrmonsters;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.zxing.activity.CaptureActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HomeActivityTest1 {
    private Solo solo;
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule(HomeActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityRule.getActivity());
    }
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
    @Test
    public void TestHomeActivity(){
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

    }
    @Test
    public void TestStartOfScanQr(){
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.scanQRCodeButton));
        solo.waitForActivity(Scan_QR.class, 5000);
        solo.assertCurrentActivity("Expected CaptureActivity",  CaptureActivity.class);

    }
    /*   @Test
       public void TestStartOfSearchNearbyQrCode(){
           solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);
           solo.clickOnView(solo.getView(R.id.searchNearbyQRButton));
           solo.waitForActivity(searchNearbyQR.class, 5000);
           solo.assertCurrentActivity("Expected CaptureActivity",  searchNearbyQR.class);

       }*/
    @Test
    public void TestStartOfViewMyQrCodes(){
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.viewMyQRCodesButton));
        solo.waitForActivity(viewPlayerProfile.class, 5000);
        solo.assertCurrentActivity("Expected viewPlayerProfileActivity",  viewPlayerProfile.class);

    }
    @Test
    public void TestStartOfSearchUsers(){
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.searchUsersButton));
        solo.waitForActivity(UserSearchActivity.class, 5000);
        solo.assertCurrentActivity("Expected UserSearchActivity",  UserSearchActivity.class);

    }
  /*  @Test
    //"fragment_curr_location"
    public void TestStartOfViewCurrentLocation(){
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.viewCurrentLocation));
        assertTrue(solo.waitForFragmentById(R.id.map,5000));

        // Check if the map is displayed in the fragment


    }*/
}
