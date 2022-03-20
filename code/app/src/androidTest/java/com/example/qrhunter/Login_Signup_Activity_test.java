package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.Manifest;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class Login_Signup_Activity_test {

    @Rule
    public ActivityScenarioRule<Login_Signup_Activity> rule = new ActivityScenarioRule<>(Login_Signup_Activity.class);

    /**
     * We test clicking on signup opens new signup activity
     */
    @Test
    public void testSignUpButton() {
        onView(withId(R.id.btnSignup)).perform(click());
    }

}
