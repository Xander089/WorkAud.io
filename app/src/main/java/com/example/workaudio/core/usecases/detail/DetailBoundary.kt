package com.example.workaudio.core.usecases.detail

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface DetailBoundary {

    fun getWorkout(id: Int): Flow<Workout>
    fun getWorkoutTracks(workoutId: Int): Flow<List<Track>>
    suspend fun updateWorkoutName(
        id: Int,
        name: String
    )


    suspend fun updateWorkoutDuration(
        id: Int,
        duration: Int
    )

    suspend fun deleteTrack(uri: String, id: Int)
}