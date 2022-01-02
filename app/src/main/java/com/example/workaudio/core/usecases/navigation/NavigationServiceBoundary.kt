package com.example.workaudio.core.usecases.navigation

import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

abstract class NavigationServiceBoundary(
    open val facade: WorkoutNavigationFacade
) {
    abstract suspend fun deleteWorkout(workoutId: Int)
    abstract fun getWorkouts(): Flow<List<Workout>>

}