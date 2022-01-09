package com.example.workaudio.core.usecases.navigation

import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface NavigationServiceBoundary{
     suspend fun deleteWorkout(workoutId: Int)
     fun getWorkouts(): Flow<List<Workout>>
     suspend fun getWorkoutTrack(id: Int): NavigationInteractor.JoinedTrack

}