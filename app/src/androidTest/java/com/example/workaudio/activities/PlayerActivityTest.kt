package com.example.workaudio.activities

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.workaudio.Constants
import com.example.workaudio.R
import com.example.workaudio.presentation.player.PlayerActivity
import com.example.workaudio.presentation.workoutMainList.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
class PlayerActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @Before
    fun setup() {
        hiltRule.inject()
        val intent = Intent(ApplicationProvider.getApplicationContext(), PlayerActivity::class.java)
        intent.putExtra(Constants.WORKOUT_ID, 0)
        ActivityScenario.launch<PlayerActivity>(intent)
    }

    @Test
    fun activityIsLaunched() {
        onView(withId(R.id.playButton)).check(matches(isEnabled()))
    }
}