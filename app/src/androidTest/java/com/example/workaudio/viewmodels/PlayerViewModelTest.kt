package com.example.workaudio.viewmodels

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.DataHelper.getOrAwaitValue
import com.example.workaudio.presentation.player.PlayerViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakePlayerBoundary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlayerViewModelTest {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var boundary: FakePlayerBoundary

    @Before
    fun setup() {
        boundary = FakePlayerBoundary()
        viewModel = PlayerViewModel(boundary)
        viewModel.setDispatcher(Dispatchers.Main)
        viewModel.emitWorkout(0)
    }


    @Test
    fun test1() = runBlocking(Dispatchers.Main) {

        assertEquals(boundary.workout.name, "test_name")
    }

    @Test
    fun test3() = runBlocking(Dispatchers.Main) {

        assertEquals(boundary.workout.name, "test_name")
    }




}