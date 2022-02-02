package com.example.workaudio.fragments

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.SmallTest
import com.example.workaudio.common.Constants.ID_TAG
import com.example.workaudio.R
import com.example.workaudio.fragments.factory.ConcreteFragmentFactory
import com.example.workaudio.fragments.factory.launchFragmentInHiltContainer
import com.example.workaudio.presentation.searchTracks.SearchTracksFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
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