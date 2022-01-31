package com.example.workaudio.fragments

import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import com.example.workaudio.R
import com.example.workaudio.fragments.factory.ConcreteFragmentFactory
import com.example.workaudio.fragments.factory.launchFragmentInHiltContainer
import com.example.workaudio.presentation.workoutMainList.WorkoutListFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class WorkoutListFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var factory: ConcreteFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {

    }


    @Test
    fun whenViewIsCreated_thenCreateButtonIsEnabled() {
        launchFragmentInHiltContainer<WorkoutListFragment>(
            factory = factory,
            fragmentArgs = null
        )
        onView(withId(R.id.createWorkoutButton)).check(matches(isDisplayed()))
    }


}