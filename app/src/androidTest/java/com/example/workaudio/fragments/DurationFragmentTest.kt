package com.example.workaudio.fragments

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.workaudio.Constants
import com.example.workaudio.Constants.WORKOUT_NAME
import com.example.workaudio.R
import com.example.workaudio.fragments.factory.ConcreteFragmentFactory
import com.example.workaudio.fragments.factory.launchFragmentInHiltContainer
import com.example.workaudio.presentation.workoutCreation.DurationFragment
import com.example.workaudio.presentation.workoutCreation.NameFragment
import com.example.workaudio.presentation.workoutMainList.WorkoutListFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class DurationFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var factory: ConcreteFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }


    @Test
    fun whenFragmentLaunchedWithNameAsBundle_thenBundleIsReturned() {
        val expected = "workout_name"
        launchFragmentInHiltContainer<DurationFragment>(
            factory = factory,
            fragmentArgs = bundleOf(WORKOUT_NAME to expected)
        ) {
            val name = this.arguments?.getString(WORKOUT_NAME).orEmpty()
            assertEquals(expected, name)
        }
    }

    @Test
    fun whenFragmentIsLaunched_thenSubElementsAreVisible() {
        launchFragmentInHiltContainer<DurationFragment>(
            factory = factory,
            fragmentArgs = bundleOf(WORKOUT_NAME to "workout_name")
        )
        onView(withId(R.id.timeLabel)).check(matches(isDisplayed()))
    }

}