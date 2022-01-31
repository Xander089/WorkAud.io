package com.example.workaudio.core.usecases.workoutList

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface ListDataAccessInterface {

    fun getWorkouts(): Flow<List<Workout>?>
    suspend fun deleteWorkout(workoutId: Int)
    suspend fun getWorkoutTrack(workoutId: Int): Track?

}