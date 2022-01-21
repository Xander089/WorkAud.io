package com.example.workaudio.useCaseInteractors.list

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.workoutList.ListInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ListInteractorTest {

    private lateinit var interactor: ListInteractor

    @Before
    fun setUp() {
        interactor = ListInteractor(FakeListDataAccess())
    }

    @Test
    fun `when all workouts requested, they are returned`() = runBlocking {
        //GIVEN
        val workouts = interactor.getWorkouts().first()
        //WHEN
        //THEN
        assert(workouts.isNotEmpty())
    }

    @Test
    fun `when workout with id 0 is deleted then thw workout list will not contain it`() =
        runBlocking {
            //GIVEN
            //WHEN
            interactor.deleteWorkout(0)
            val workouts = interactor.getWorkouts().first()
            //THEN
            assert(workouts.isEmpty())

        }

    @Test
    fun `when workout id is 0 then the first track with workout id 0 is returned`() = runBlocking {
        //GIVEN

        //WHEN
        val track = interactor.getWorkoutTrack(0)
        //THEN
        assert(track.workoutId == 0)
    }

}