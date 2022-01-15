package com.example.workaudio.fragments

import com.example.workaudio.presentation.workoutCreation.NameFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class FragmentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var factory : MyFragmentFactory


    @Before
    fun setup() {
        hiltRule.inject()

    }

    @After
    fun tearDown() {

    }

    @Test
    fun test_name_fragment2() {

    }

    @Test
    fun test_name_fragment() {
        val scenario = launchFragmentInHiltContainer<NameFragment>(factory = factory){}
    }

}