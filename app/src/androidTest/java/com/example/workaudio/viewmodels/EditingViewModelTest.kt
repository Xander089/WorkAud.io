package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workaudio.presentation.workoutCreation.DurationFragmentViewModel
import com.example.workaudio.presentation.workoutDetail.DetailFragmentViewModel
import com.example.workaudio.viewmodels.fakeBoundary.FakeCreationBoundary
import com.example.workaudio.viewmodels.fakeBoundary.FakeEditingBoundary
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditingViewModelTest {

    private lateinit var viewModel: DetailFragmentViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        viewModel = DetailFragmentViewModel(FakeEditingBoundary())
        viewModel.setDispatcher(Dispatchers.Main)
    }


    @Test
    fun test() {
        Assert.assertTrue("abc" == "abc")
    }


}