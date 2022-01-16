package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.workoutMainList.WorkoutListFragmentViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakeNavigationBoundary
import org.junit.Before
import org.junit.Test



class NavigationViewModelTest {

    private lateinit var viewModel: WorkoutListFragmentViewModel

    @Before
    fun setup() {
        viewModel = WorkoutListFragmentViewModel(FakeNavigationBoundary())
    }


    @Test
    fun test() {

    }


}