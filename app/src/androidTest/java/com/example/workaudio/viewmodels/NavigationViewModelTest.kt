package com.example.workaudio.viewmodels

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.DataHelper.getOrAwaitValue
import com.example.workaudio.presentation.workoutMainList.WorkoutListFragmentViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakeNavigationBoundary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class NavigationViewModelTest {

    private lateinit var viewModel: WorkoutListFragmentViewModel
    private lateinit var access: FakeNavigationBoundary

    @Before
    fun setup() {
        access = FakeNavigationBoundary()
        viewModel = WorkoutListFragmentViewModel(access)
        viewModel.setDispatcher(Dispatchers.Main)
    }


    @Test
    fun test() = runBlocking(Dispatchers.Main){
        val workoutName = viewModel.workouts.getOrAwaitValue()[0].name
        assert(workoutName == "test_name")
    }

    @Test
    fun test2() = runBlocking(Dispatchers.Main){
        viewModel.deleteWorkout(0)
        assert(access.getWorkouts().first().first().name == "test_name")
    }


}