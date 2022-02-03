package com.example.workaudio.useCaseInteractors

import com.example.workaudio.TestDataSource
import com.example.workaudio.core.usecases.player.PlayerDataAccessInterface
import com.example.workaudio.core.usecases.player.PlayerInteractor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlayerInteractorTest {

    private lateinit var interactor: PlayerInteractor

    @Mock
    lateinit var access: PlayerDataAccessInterface
    private lateinit var source: TestDataSource


    @Before
    fun setUp() {
        source = TestDataSource()
        interactor = PlayerInteractor(access)
    }

    @Test
    fun whenWorkoutRequestedById_thenItIsReturned() = runBlocking {
        //Given
        val expected = source.workout
        //When
        Mockito.`when`(access.getWorkout(0)).thenReturn(source.workout)
        val result = interactor.getWorkout(0)
        //Then
        assertEquals(expected.id, result?.id)
    }


}