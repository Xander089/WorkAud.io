package com.example.workaudio.core.usecases.detail

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface DetailDataAccessInterface {

    fun getWorkoutAsFlow(workoutId: Int): Flow<Workout>
    fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>>
    suspend fun updateWorkoutName(name: String, id: Int)
    suspend fun updateWorkoutDuration(id: Int, duration: Int)
    suspend fun deleteTrack(uri: String, id: Int)

}