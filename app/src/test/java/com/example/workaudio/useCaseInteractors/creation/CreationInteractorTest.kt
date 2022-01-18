package com.example.workaudio.useCaseInteractors.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CreationInteractorTest {

    private lateinit var interactor: CreationInteractor

    @Before
    fun setUp() {
        interactor = CreationInteractor(FakeCreationDataAccess())
    }


    @Test
    fun `when latest workout is requested as Flow, then it is returned`() = runBlocking {
        //GIVEN
        val workout = interactor.getLatestWorkout().first()

        //WHEN
        //THEN
        assert(workout?.name == "test_name")

    }

    @Test
    fun `when latest workout is requested, then it is returned`() = runBlocking {
        //GIVEN
        val workout = interactor.getWorkout()

        //WHEN

        //THEN
        assert(workout?.name == "test_name")

    }

    @Test
    fun `when a new workout is created, then this new latest workout is returned`() = runBlocking {
        //GIVEN

        //WHEN
        interactor.createWorkout("new_name", 10)
        //THEN
        val newWorkoutName = interactor.getWorkout()?.name.orEmpty()
        assert(newWorkoutName == "new_name")

    }

}