package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

public class QRScanActivityTest {
    @Rule
    public ActivityScenarioRule<QRScanActivity> rule = new ActivityScenarioRule<>(QRScanActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);
    /**
     * We test if adding a comment will be displayed
     */
    @Test
    public void clickOnScannerTest() {
        // add a comment and see it it is displayed

        onView(withId(R.id.scanner_view)).perform(click());
    }
}
