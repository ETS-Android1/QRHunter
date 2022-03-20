package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class PlayerProfileTest {
    @Rule
    public ActivityScenarioRule<PlayerProfile> rule = new ActivityScenarioRule<>(PlayerProfile.class);



    @Test
    public void startTest() {
        onView(withId(R.id.generate_button)).perform(click());
        onView(withId(R.id.recyclerViewPlayerProfile)).perform(click());
    }
}

