package com.example.workaudio.viewmodels

import androidx.test.filters.SmallTest
import com.example.workaudio.common.DataHelper.getOrAwaitValue
import com.example.workaudio.TestDataSource
import com.example.workaudio.core.usecases.workoutList.ListBoundary
import com.example.workaudio.presentation.workoutMainList.WorkoutListFragmentViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class MainListViewModelTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var source: TestDataSource
    private lateinit var viewModel: WorkoutListFragmentViewModel

    @Mock
    lateinit var interactor: ListBoundary


    @Before
    fun setup() {
        `when`(interactor.getWorkouts()).thenReturn(flow { emit(source.list) })
        source = TestDataSource()
        hiltRule.inject()
        viewModel = WorkoutListFragmentViewModel(interactor)
        viewModel.setDispatcher(Dispatchers.Main)
    }

    private fun removeWorkout(id: Int) {
        source.list.removeAt(id)
    }


    @Test
    fun whenWorkoutsRequested_thenTheyAreEmittedAsLiveData() = runBlocking(Dispatchers.Main) {
        val workoutName = viewModel.workouts.getOrAwaitValue()?.get(0)?.name
        assertTrue(workoutName == "test_name")
    }

    @Test
    fun whnWorkoutIsDeleted_thenListDoesNotContainIt() = runBlocking(Dispatchers.Main) {
        `when`(interactor.deleteWorkout(0)).thenReturn(removeWorkout(0))
        viewModel.deleteWorkout(0)
        assertTrue(source.list.isEmpty())
    }


}