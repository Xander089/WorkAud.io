package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface CreationDataAccessInterface {

    fun getLatestWorkoutAsFlow(): Flow<Workout?>
    suspend fun searchTracks(queryText: String): List<Track>

    suspend fun getWorkout(): Workout?


    suspend fun insertWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>,
    )


    suspend fun insertWorkout(
        name: String,
        duration: Int,
    )

    suspend fun insertWorkoutTracks(tracks: List<Track>)
}