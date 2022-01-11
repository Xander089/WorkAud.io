package com.example.workaudio

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.presentation.DummyActivity
import com.example.workaudio.presentation.creation.NameFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
class AnotherTest {

private lateinit var scenario : FragmentScenario<NameFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_MaterialComponents)
        scenario.moveToState(Lifecycle.State.STARTED)

    }

    @After
    fun tearDown() {

    }



    @Test
    fun test_name_fragment_button_enabled_when_edit_text_is_not_empty() {
        onView(withId(R.id.workoutNameText)).perform(typeText("myname"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirmNameButton)).check(matches(isEnabled()))
    }

    @Test
    fun test_name_fragment_button_disabled_when_edit_text_is_empty() {
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.confirmNameButton)).check(matches(isNotEnabled()))
    }

}