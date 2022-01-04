package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

abstract class CreationServiceBoundary(
    open val facade: WorkoutCreationFacade
) {
    abstract val latestWorkout: Flow<Workout>
    abstract suspend fun searchTracks(queryText: String): List<Track>
    abstract suspend fun getWorkout(): Workout

    abstract suspend fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track> = listOf<Track>()
    )

    abstract suspend fun createWorkout(
        name: String,
        duration: Int,
    )
}