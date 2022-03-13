package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;

import android.widget.EditText;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for UserQRInfoActivity
 */
public class UserQRInfoTest {

    @Rule
    public ActivityScenarioRule<UserQRInfoActivity> rule = new ActivityScenarioRule<>(UserQRInfoActivity.class);

    /**
     * We test if adding a comment will be displayed
     */
    @Test
    public void addCommentTest() {
        // add a comment and see it it is displayed
        onView(withId(R.id.addComment)).perform(typeText("This is an example comment"));
        onView(withId(R.id.sendComment)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.commentList)).atPosition(0).check(matches(isDisplayed()));
    }
}
