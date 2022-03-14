package com.example.qrhunter;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class SignUpTest {
    @Rule
    public ActivityScenarioRule<signup> rule = new ActivityScenarioRule<>(signup.class);

    /**
     * We test clicking on signup opens new signup activity
     */
    @Test
    public void testSignUpFields() {

        onView(withId(R.id.edtTextInputUsername)).perform(typeText("User1"));
        onView(withId(R.id.edtTextPassword)).perform(typeText("Pass"));
        onView(withId(R.id.edtTextPasswordConfirm)).perform(typeText("Pass"));
        onView(withId(R.id.edtTextEmail)).perform(typeText("Pass@gmail.com"));
        onView(withId(R.id.edtTextPhone)).perform(typeText("1111111111"));
        onView(withId(R.id.btnConfirm)).perform(click());
    }

}
