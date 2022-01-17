package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.searchTracks.SearchBoundary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSearchBoundary : SearchBoundary {

    private val workout = Workout(
        name = "test_name",
        duration = 30 * 1000 * 60,
        imageUrl = "test",
        tracks = emptyList()
    )
    private val track = Track("title", "uri", 1000, "artist", "album", "url", 0)
    private val trackList = mutableListOf(track)


    override fun getWorkout(id: Int): Flow<Workout> {
        return flow {
            emit(workout)
        }
    }

    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>> {
        return flow {
            emit(trackList)
        }
    }

    override suspend fun searchTracks(queryText: String): List<Track> {
        return trackList
    }

    override suspend fun addTrack(track: Track, workoutId: Int) {
        trackList.add(track)
    }

    override suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        workout.imageUrl = imageUrl
    }
}