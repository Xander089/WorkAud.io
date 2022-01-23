package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.DataHelper.getOrAwaitValue
import com.example.workaudio.presentation.workoutDetail.DetailFragmentViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakeDetailBoundary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailViewModelTest {

    private lateinit var viewModel: DetailFragmentViewModel
    private lateinit var boundary: FakeDetailBoundary

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        boundary = FakeDetailBoundary()
        viewModel = DetailFragmentViewModel(boundary)
        viewModel.setDispatcher(Dispatchers.Main)
        viewModel.initializeCurrentWorkout(0)
    }


    @Test
    fun when_workout_is_requested_then_it_is_emitted_as_live_data() {
        val workout = viewModel.selectedWorkout.getOrAwaitValue()
        assertEquals("test_workout", workout.name)
    }

    @Test
    fun when_tracks_are_requested_then_they_are_emitted_as_live_data() {
        val track = viewModel.tracks.getOrAwaitValue().first()
        assertEquals("test_title", track.title)
    }

    @Test
    fun when_name_is_updated_then_workout_with_new_name_is_returned() =
        runBlocking(Dispatchers.Main) {
            viewModel.updateWorkoutName(0, "new_name")
            delay(1000)
            assertEquals("new_name", boundary.workout.name)
        }

    @Test
    fun when_duration_is_updated_then_workout_with_new_duration_is_returned() =
        runBlocking(Dispatchers.Main) {
            viewModel.updateWorkoutDuration(0, 1)
            delay(1000)
            assertEquals(60000, boundary.workout.duration)
        }

    @Test
    fun when_track_is_deleted_then_tracks_do_not_contain_it_anymore() = runBlocking(Dispatchers.Main) {
        viewModel.deleteTrack("test_uri")
        delay(1000)
        assertTrue(boundary.trackList.isEmpty())
    }


}