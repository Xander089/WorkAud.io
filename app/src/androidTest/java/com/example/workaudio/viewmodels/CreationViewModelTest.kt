package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.workaudio.DataHelper.getOrAwaitValue
import com.example.workaudio.core.usecases.creation.CreationBoundary
import com.example.workaudio.presentation.workoutCreation.DurationFragmentViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
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
class CreationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var source : TestDataSource
    private lateinit var viewModel: DurationFragmentViewModel

    @Mock
    lateinit var interactor: CreationBoundary


    @Before
    fun setup() {
        source = TestDataSource()
        hiltRule.inject()
        mockWorkoutEmission()
        viewModel = DurationFragmentViewModel(interactor)
        viewModel.setDispatcher(Dispatchers.Main)

    }

    private fun mockWorkoutEmission(){
        `when`(interactor.getLatestWorkout()).thenReturn(flow {
            emit(source.workout)
        })
    }

    @Test
    fun whenWorkoutRequested_thenItIsEmittedAsLiveData() = runBlocking(Dispatchers.Main) {
        val expected = "test_name"
        val actual = viewModel.workout.getOrAwaitValue()?.name.orEmpty()
        assertEquals(expected,actual)
    }

    @Test
    fun whenInputDurationIsOneMinute_then_1_min_isReturnedAsString() {
        val result = viewModel.formatDuration(1.0F)
        assertTrue(result == "1 min")
    }


}
