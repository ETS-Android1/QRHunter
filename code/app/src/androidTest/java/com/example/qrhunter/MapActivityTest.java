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

public class MapActivityTest {
    @Rule
    public ActivityScenarioRule<MapActivity> rule = new ActivityScenarioRule<>(MapActivity.class);

    /**
     * We test if adding a comment will be displayed
     */
    @Test
    public void clickOnMapTest() {
        // add a comment and see it it is displayed
        onView(withId(R.id.mapView)).perform(click());
    }
}
