package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface CreationBoundary {
    fun getLatestWorkout(): Flow<Workout?>
    suspend fun searchTracks(queryText: String): List<Track>
    suspend fun getWorkout(): Workout?

    suspend fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track> = listOf<Track>()
    )

    suspend fun createWorkout(
        name: String,
        duration: Int,
    )
}