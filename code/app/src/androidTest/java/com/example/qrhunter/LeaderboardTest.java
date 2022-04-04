package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
// log in on device prior to test
public class LeaderboardTest {
    @Rule
    public ActivityScenarioRule<LeaderBoard> rule = new ActivityScenarioRule<>(LeaderBoard.class);



    @Test
    public void startTest() {
        onView(withId(R.id.sort_by_score)).perform(click());
        onView(withId(R.id.sort_by_num_scans)).perform(click());
        onView(withId(R.id.sort_by_top_code)).perform(click());

    }
}

