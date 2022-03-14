// This is intent testing the ProfileQRInfoActivity

package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for ProfileQRInfoActivity
 */
public class ProfileQRInfoTest {

    @Rule
    public ActivityScenarioRule<ProfileQRInfoActivity> rule = new ActivityScenarioRule<>(ProfileQRInfoActivity.class);


    /**
     * We test if activity is started
     */
    @Test
    public void startTest() {
        // we see if there is a back button that is unique the the ProfileQRInfoActivity
        // to verify that we are actually in ProfileQRInfoActivity
        onView(withId(R.id.backProfile)).check(matches(isDisplayed()));
    }
}
