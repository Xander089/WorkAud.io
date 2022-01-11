package com.example.workaudio.viewmodels

import com.example.workaudio.presentation.navigation.WorkoutListFragmentViewModel
import com.example.workaudio.viewmodels.fakeservice.FakeNavigationService
import org.junit.Before
import org.junit.Test



class NavigationViewModelTest {

    private lateinit var viewModel: WorkoutListFragmentViewModel

    @Before
    fun setup() {
        viewModel = WorkoutListFragmentViewModel(FakeNavigationService())
    }


    @Test
    fun test() {

    }


}