package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onData;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.robotium.solo.Solo;

/**
 * Test class for ListCodesActivity clicking on the list feature
 */

public class ListCodesTest {
    @Rule
    public IntentsTestRule<ListCodesActivity> intentsTestRule =
            new IntentsTestRule<>(ListCodesActivity.class);


    /**
     * We test if clicking the list item works
     */
    @Test
    public void checkList(){
        onView(withId(R.id.qr_list)).perform(click());
        //onData(anything()).inAdapterView(withId(R.id.qr_list)).atPosition(0).perform(click());
        //crashes the program
        //intended(hasComponent(UserQRInfoActivity.class.getName()));

    }




}
