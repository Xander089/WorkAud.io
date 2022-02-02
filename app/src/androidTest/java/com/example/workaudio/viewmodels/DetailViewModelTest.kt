package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.workaudio.common.DataHelper.getOrAwaitValue
import com.example.workaudio.TestDataSource
import com.example.workaudio.core.usecases.detail.DetailBoundary
import com.example.workaudio.presentation.workoutDetail.DetailFragmentViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var source : TestDataSource
    private lateinit var viewModel: DetailFragmentViewModel

    @Mock
    lateinit var interactor: DetailBoundary


    @Before
    fun setup() {
        source = TestDataSource()
        mockWorkoutEmission()
        viewModel = DetailFragmentViewModel(interactor)
        viewModel.setDispatcher(Dispatchers.Main)
        viewModel.initializeCurrentWorkout(0)
    }

    private fun mockWorkoutEmission() {
        Mockito.`when`(interactor.getWorkout(0)).thenReturn(flow {
            emit(source.workout)
        })
        Mockito.`when`(interactor.getWorkoutTracks(0)).thenReturn(flow {
            emit(source.tracks)
        })
    }


    @Test
    fun whenWorkoutIsRequested_thenItIsEmittedAsLiveData()  {
        val expected = "test_name"
        val actual = viewModel.selectedWorkout.getOrAwaitValue()?.name.orEmpty()
        assertEquals(expected, actual)
    }

    @Test
    fun whenWorkoutTracksAreRequested_thenEmittedAsLiveData() {
        val expected = "title1"
        val actual = viewModel.tracks.getOrAwaitValue()?.first()?.title.orEmpty()
        assertEquals(expected, actual)
    }


}