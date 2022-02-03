package com.example.workaudio.useCaseInteractors

import com.example.workaudio.TestDataSource
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationDataAccessInterface
import com.example.workaudio.core.usecases.creation.CreationInteractor
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreationInteractorTest {

    private lateinit var interactor: CreationInteractor

    @Mock
    lateinit var dataAccess: CreationDataAccessInterface

    private lateinit var source: TestDataSource


    @Before
    fun setUp() {
        source = TestDataSource()
        interactor = CreationInteractor(dataAccess)
    }


    @Test
    fun whenANewWorkoutIsCreated_thenThisCanBeReadAsTheLatestWorkout() =
        runBlocking {
            //GIVEN
            val name = "new_name"
            val duration = 10

            //WHEN
            `when`(dataAccess.insertWorkout(name, duration))
                .thenReturn(addWorkout(name, duration))
            interactor.createWorkout(name, duration)

            //THEN
            assertEquals(name, source.list.last().name)
        }

    private fun addWorkout(name: String, duration: Int) {
        source.list.add(Workout(0, name, duration, emptyList<Track>(), ""))
    }

}