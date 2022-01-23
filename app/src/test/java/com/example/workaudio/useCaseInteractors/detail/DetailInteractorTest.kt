package com.example.workaudio.useCaseInteractors.detail

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.detail.DetailInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DetailInteractorTest {

    private val workout = Workout(
        name = "test_name",
        duration = 30 * 1000 * 60,
        imageUrl = "test",
        tracks = emptyList(),
        id = 0
    )
    private val track = Track("title", "uri", 1000, "artist", "album", "url", 0)
    private lateinit var interactor: DetailInteractor

    @Before
    fun setUp() {
        interactor = DetailInteractor(FakeDetailDataAccess())
    }


    @Test
    fun `when tracks are requested as flow they are returned`() = runBlocking {
        val tracks = interactor.getWorkoutTracks(0).first()
        assertEquals(track.title, tracks[0].title)
    }

    @Test
    fun `when workout is requested by id as flow then it is returned`() = runBlocking {
        val _workout = interactor.getWorkout(0).first()
        assertEquals(workout.id, _workout.id)
    }

    @Test
    fun `when workout name is updated as new_name then the workout name is renamed as new_name`() =
        runBlocking {
            val expected = "new_name"
            interactor.updateWorkoutName(0, expected)
            assertEquals(expected, interactor.getWorkout(0).first().name)
        }

    @Test
    fun `when workout duration is updated as 10 then the workout duration is returned as 10`() =
        runBlocking {
            val expected = 10
            interactor.updateWorkoutDuration(0, expected)
            assertEquals(expected, interactor.getWorkout(0).first().duration)
        }


    @Test
    fun `when delete is called with uri as song uri then all tracks with that uri will be removed`() =
        runBlocking {
            interactor.deleteTrack("uri", 0)
            assert(interactor.getWorkoutTracks(0).first().isEmpty())
        }


}