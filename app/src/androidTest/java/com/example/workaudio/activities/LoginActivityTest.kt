package com.example.workaudio.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.workaudio.R
import com.example.workaudio.presentation.login.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun onActivity_whenLoginButtonClicked_thenSpotifyAutomaticLoginIsLaunched() {
       val scenario =  ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.loginButton)).perform(click())
    }


}