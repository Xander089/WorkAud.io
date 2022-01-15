package com.example.workaudio.activities

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.workaudio.R
import com.example.workaudio.presentation.login.LoginActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup(){
        activityRule.scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun activityIsLaunched() {
        onView(ViewMatchers.withId(R.id.loginButton)).perform(click())
        onView(ViewMatchers.withId(R.id.textView)).check(
            matches(withText("a"))
        )

    }
}