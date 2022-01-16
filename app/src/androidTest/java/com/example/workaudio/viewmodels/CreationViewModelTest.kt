package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.DataHelper.getOrAwaitValue
import com.example.workaudio.presentation.workoutCreation.DurationFragmentViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakeCreationBoundary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CreationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private lateinit var viewModel: DurationFragmentViewModel

    @Before
    fun setup() {
        viewModel = DurationFragmentViewModel(FakeCreationBoundary())
        viewModel.setDispatcher(Dispatchers.Main)
        viewModel.insertWorkout("abc", 30)

    }

    @Test
    fun the_default_workout_is_emitted_as_live_data() = runBlocking(Dispatchers.Main) {
        val name = viewModel.workout.getOrAwaitValue()?.name.orEmpty()
        assertTrue(name == "abc")
    }

    @Test
    fun test_workout_state_created() {
        viewModel.state = DurationFragmentViewModel.Companion.STATE.CREATED
        assertTrue(viewModel.isWorkoutCreated())
    }

    @Test
    fun test_format_minutes_duration() {
        val result = viewModel.formatDuration(1.0F)
        assertTrue(result == "1 min")
    }


}
