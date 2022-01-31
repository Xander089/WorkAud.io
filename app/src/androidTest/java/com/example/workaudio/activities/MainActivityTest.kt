package com.example.workaudio.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.workaudio.R
import com.example.workaudio.presentation.workoutMainList.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
        ActivityScenario.launch(MainActivity::class.java)
    }


    @Test
    fun onActivityLaunched_thenCreateWorkoutButtonIsEnabled() {
        onView(withId(R.id.createWorkoutButton)).check(matches(isEnabled()))
    }

    @Test
    fun onActivityLaunched_thenRecyclerViewIsDisplayed() {
        onView(withId(R.id.workoutList)).check(matches(isDisplayed()))
    }

    @Test
    fun whenCreateButtonClicked_thenNameFragmentIsDisplayed() {
        onView(withId(R.id.createWorkoutButton)).perform(click())
        onView(withId(R.id.chooseNameText)).check(matches(isDisplayed()))
    }

    @Test
    fun whenNameLengthLessThanFiveChar_thenConfirmButtonIsNotEnabled() {
        onView(withId(R.id.createWorkoutButton)).perform(click())
        onView(withId(R.id.confirmNameButton)).check(matches(isNotEnabled()))
    }

    @Test
    fun whenNameLengthGreaterThanFiveChar_thenConfirmButtonIsEnabled() {
        onView(withId(R.id.createWorkoutButton)).perform(click())
        onView(withId(R.id.workoutNameText)).perform(clearText(), typeText("Test Name"))
        onView(withId(R.id.confirmNameButton)).check(matches(isEnabled()))
    }

    @Test
    fun whenConfirmNameClicked_thenDurationFragmentIsDisplayed() {
        onView(withId(R.id.createWorkoutButton)).perform(click())
        onView(withId(R.id.workoutNameText)).perform(clearText(), typeText("Test Name"))
        onView(withId(R.id.confirmNameButton)).perform(click())
        onView(withId(R.id.timeLabel)).check(matches(isDisplayed()))
    }

    @Test
    fun whenConfirmCreationClicked_thenReturnToHomeFragment() {
        onView(withId(R.id.createWorkoutButton)).perform(click())
        onView(withId(R.id.workoutNameText)).perform(clearText(), typeText("Test Name"))
        onView(withId(R.id.confirmNameButton)).perform(click())
        onView(withId(R.id.confirmWorkoutCreationButton)).perform(click())
        onView(withId(R.id.createWorkoutButton)).check(matches(isDisplayed()))
    }

    @Test
    fun whenRecyclerViewItemClicked_thenShowDetailFragment() {

    }

}