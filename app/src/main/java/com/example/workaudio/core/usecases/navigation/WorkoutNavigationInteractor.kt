package com.example.workaudio.core.usecases.navigation

import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class WorkoutNavigationInteractor(
    override  val facade: WorkoutNavigationFacade
) : NavigationServiceBoundary(facade) {

    override suspend fun deleteWorkout(workoutId: Int) = facade.deleteWorkout(workoutId)
    override fun getWorkouts(): Flow<List<Workout>> {
        return facade.workouts
    }

}