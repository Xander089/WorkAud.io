package com.example.workaudio.usecases.workoutNavigation

import com.example.workaudio.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class WorkoutNavigation(
    private val facade: WorkoutNavigationFacade
) {
    suspend fun getAllWorkouts(): List<Workout> = facade.getAllWorkouts()
    suspend fun getWorkout(id: Int): Workout? = facade.getWorkout(id)
}