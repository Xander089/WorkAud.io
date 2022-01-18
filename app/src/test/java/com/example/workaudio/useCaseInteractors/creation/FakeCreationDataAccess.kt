package com.example.workaudio.useCaseInteractors.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationDataAccessInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCreationDataAccess : CreationDataAccessInterface {

    private val workout = Workout(
        name = "test_name",
        duration = 30 * 1000 * 60,
        imageUrl = "test",
        tracks = emptyList()
    )
    private val workoutList = mutableListOf(workout)
    private val track = Track("title", "uri", 1000, "artist", "album", "url", 0)
    private val trackList = mutableListOf(track)

    override fun getLatestWorkoutAsFlow(): Flow<Workout?> {
        return flow {
            emit(workoutList.last())
        }
    }

    override suspend fun searchTracks(queryText: String): List<Track> {
        return trackList
    }

    override suspend fun getWorkout(): Workout? {
        return workoutList.last()
    }

    override suspend fun insertWorkout(name: String, duration: Int, tracks: List<Track>) {
        val workout = Workout(
            name = name,
            duration = duration,
            imageUrl = "test",
            tracks = tracks
        )
        workoutList.add(workout)
    }

    override suspend fun insertWorkout(name: String, duration: Int) {
        val workout = Workout(
            name = name,
            duration = duration,
            imageUrl = "test",
            tracks = emptyList()
        )
        workoutList.add(workout)
    }

    override suspend fun insertWorkoutTracks(tracks: List<Track>) {
        trackList.addAll(tracks)
    }
}