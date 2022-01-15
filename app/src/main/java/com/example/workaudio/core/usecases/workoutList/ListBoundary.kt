package com.example.workaudio.core.usecases.workoutList

import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface ListBoundary{
     suspend fun deleteWorkout(workoutId: Int)
     fun getWorkouts(): Flow<List<Workout>>
     suspend fun getWorkoutTrack(id: Int): ListInteractor.JoinedTrack

}