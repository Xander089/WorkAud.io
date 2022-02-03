package com.example.workaudio.useCaseInteractors

import com.example.workaudio.TestDataSource
import com.example.workaudio.core.usecases.workoutList.ListDataAccessInterface
import com.example.workaudio.core.usecases.workoutList.ListInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ListInteractorTest {

    private lateinit var interactor: ListInteractor

    @Mock
    lateinit var dataAccess: ListDataAccessInterface

    private lateinit var source: TestDataSource

    @Before
    fun setUp() {
        source = TestDataSource()
        interactor = ListInteractor(dataAccess)
    }

    @Test
    fun whenAllWorkoutsRequested_then_TheyAreReturned() = runBlocking {
        //WHEN
        Mockito.`when`(dataAccess.getWorkouts())
            .thenReturn(flow { emit(source.list) })
        val workouts = interactor.getWorkouts().first().orEmpty()
        //THEN
        assertTrue(workouts.isNotEmpty())
    }

    @Test
    fun whenWorkoutWithId_0_isDeleted_thenTheWorkoutListWillNotContainIt() =
        runBlocking {
            //WHEN
            Mockito.`when`(dataAccess.deleteWorkout(0))
                .thenReturn(deleteWorkout(0))

            interactor.deleteWorkout(0)

            //THEN
            assertTrue(source.list.isEmpty())

        }

    private fun deleteWorkout(id: Int) {
        source.list.removeAt(id)
    }

    @Test
    fun whenWorkoutIdIs_0_thenTheFirstTrackWithWorkoutId_0_isReturned() = runBlocking {
        //WHEN
        Mockito.`when`(dataAccess.getWorkoutTrack(0))
            .thenReturn(source.tracks[0])

        val track = interactor.getWorkoutTrack(0)

        //THEN
        assertTrue(track?.workoutId == 0)
    }

}