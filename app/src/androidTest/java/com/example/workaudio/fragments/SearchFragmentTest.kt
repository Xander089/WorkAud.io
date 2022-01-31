package com.example.workaudio.fragments

import android.widget.Button
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import com.example.workaudio.Constants.ID_TAG
import com.example.workaudio.R
import com.example.workaudio.fragments.factory.ConcreteFragmentFactory
import com.example.workaudio.fragments.factory.launchFragmentInHiltContainer
import com.example.workaudio.presentation.searchTracks.SearchTracksFragment
import com.example.workaudio.presentation.workoutDetail.DetailFragment
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
class SearchFragmentTest {

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
    fun whenViewIsCreated_thenAddCurrentMinuteLabelIsDisplayed() {
        launchFragmentInHiltContainer<SearchTracksFragment>(
            factory = factory,
            fragmentArgs = bundleOf(ID_TAG to 1)
        )
        onView(withId(R.id.currentMinuteLabel)).check(matches(isDisplayed()))
    }


}