package com.example.workaudio.useCaseInteractors

import com.example.workaudio.TestDataSource
import com.example.workaudio.core.usecases.detail.DetailDataAccessInterface
import com.example.workaudio.core.usecases.detail.DetailInteractor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailInteractorTest {

    private lateinit var interactor: DetailInteractor

    @Mock
    lateinit var dataAccess: DetailDataAccessInterface

    private lateinit var source: TestDataSource

    @Before
    fun setUp() {
        source = TestDataSource()
        interactor = DetailInteractor(dataAccess)
    }


    @Test
    fun whenTracksAreRequestedAsFlow_thenTheyAreReturned() = runBlocking {
        //Given
        val tracks = source.tracks
        //When
        Mockito.`when`(dataAccess.getWorkoutTracksAsFlow(0))
            .thenReturn(flow { emit(tracks) })
        val actualTrack = interactor.getWorkoutTracks(0).first()?.get(0)
        //Then
        assertEquals(tracks[0].title, actualTrack?.title)
    }

    @Test
    fun whenWorkoutIRequestedByIdAsFlow_thenItIsReturned() = runBlocking {
        //Given
        val workout = source.workout
        //When
        Mockito.`when`(dataAccess.getWorkoutAsFlow(0))
            .thenReturn(flow { emit(workout) })

        val actualWorkout = interactor.getWorkout(0).first()
        assertEquals(workout.id, actualWorkout?.id)
    }

    @Test
    fun whenUpdateParameterIs_new_name_thenTheWorkoutNameIsUpdatedAs_new_name() =
        runBlocking {
            //Given
            val expected = "new_name"
            //When
            Mockito.`when`(dataAccess.updateWorkoutName(expected, 0))
                .thenReturn(updateName(expected))

            interactor.updateWorkoutName(0, expected)
            //Then
            assertEquals(expected, source.workout.name)
        }

    private fun updateName(name: String) {
        source.workout.name = name
    }

    @Test
    fun whenWorkoutDurationIsUpdatedAs_10_thenTheWorkoutDurationIsReturnedAs_10() =
        runBlocking {
            //Given
            val expected = 10
            //When
            Mockito.`when`(dataAccess.updateWorkoutDuration(0, expected))
                .thenReturn(updateDuration(expected))

            interactor.updateWorkoutDuration(0, expected)
            //Then
            assertEquals(expected, source.workout.duration)
        }

    private fun updateDuration(duration: Int) {
        source.workout.duration = duration
    }


    @Test
    fun whenDeleteIsCalledWith_uri_asSongUri_thenAllTracksWithThatUriWillBeRemoved() =
        runBlocking {
            //Given
            val uri = "uri"
            val origSize = source.tracks.size
            //When
            Mockito.`when`(dataAccess.deleteTrack(uri, 0))
                .thenReturn(deleteTrack(uri))

            interactor.deleteTrack(uri, 0)

            //Then
            assertTrue(source.tracks.isEmpty())
        }

    private fun deleteTrack(uri: String) {
        source.tracks.removeAll {
            it.uri == uri
        }
    }


}