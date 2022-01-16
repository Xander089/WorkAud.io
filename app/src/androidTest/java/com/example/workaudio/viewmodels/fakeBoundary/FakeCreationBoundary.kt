package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.Constants
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationBoundary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCreationBoundary : CreationBoundary {

    private val track = Track(
        "test_title",
        "test_uri",
        3 * Constants.MILLIS_IN_A_MINUTE,
        "test_artist",
        "test_album",
        "test_img_url",
        0
    )


    private val trackList = listOf(track)

    private var latestWorkout: Workout? = Workout(
        0,
        "test_workout",
        30 * Constants.MILLIS_IN_A_MINUTE,
        emptyList(),
        "test_url"
    )


    override fun getLatestWorkout(): Flow<Workout?> {
        return flow {
            emit(latestWorkout)
        }
    }

    override suspend fun searchTracks(queryText: String): List<Track> {
        return trackList
    }

    override suspend fun getWorkout(): Workout? {
        return latestWorkout
    }

    override suspend fun createWorkout(name: String, duration: Int, tracks: List<Track>) {
        latestWorkout = Workout(
            0,
            name,
            duration,
            tracks,
            "test_url"
        )
    }

    override suspend fun createWorkout(name: String, duration: Int) {
        latestWorkout = Workout(
            0,
            name,
            duration,
            emptyList(),
            "test_url"
        )
    }
}