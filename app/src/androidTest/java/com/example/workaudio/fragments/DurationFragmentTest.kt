package com.example.workaudio.fragments

import androidx.core.os.bundleOf

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import com.example.workaudio.common.Constants.WORKOUT_NAME
import com.example.workaudio.R
import com.example.workaudio.fragments.factory.ConcreteFragmentFactory
import com.example.workaudio.fragments.factory.launchFragmentInHiltContainer
import com.example.workaudio.presentation.workoutCreation.DurationFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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