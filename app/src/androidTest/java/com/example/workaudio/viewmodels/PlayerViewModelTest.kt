package com.example.workaudio.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.workaudio.TestDataSource
import com.example.workaudio.core.usecases.player.PlayerBoundary
import com.example.workaudio.presentation.player.PlayerViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
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
class PlayerViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    private lateinit var source : TestDataSource
    private lateinit var viewModel: PlayerViewModel

    @Mock
    lateinit var playerInteractor: PlayerBoundary

    @Before
    fun setup() {
        source = TestDataSource()
        hiltRule.inject()
        viewModel = PlayerViewModel(playerInteractor)
        viewModel.setDispatcher(Dispatchers.Main)
    }


    @Test
    fun test() = runBlocking(Dispatchers.Main) {
        `when`(playerInteractor.getWorkout(0)).thenReturn(source.workout)
        val id = viewModel.getWorkout()?.id ?: -1
        assertEquals(0, id)
    }

    @Test
    fun test2() {
        val minutes = "01:00"
        val progress = viewModel.getProgress(minutes)
        assertTrue(progress > 100)
    }
}