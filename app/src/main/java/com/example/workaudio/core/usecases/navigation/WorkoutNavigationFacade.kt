package com.example.workaudio.core.usecases.navigation


import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.repository.database.ApplicationDAO


import kotlinx.coroutines.flow.map

class WorkoutNavigationFacade(
    private val dao: ApplicationDAO
) {

    val workouts = dao.getAllWorkouts().map { workoutRoomEntities ->
        workoutRoomEntities.map { workoutRoomEntity ->
            workoutRoomEntity.toWorkout(emptyList<Track>())
        }
    }

    suspend fun deleteWorkout(workoutId: Int) = dao.deleteWorkout(workoutId)
    suspend fun getWorkoutTrack(workoutId: Int) = dao.getWorkoutTrack(workoutId).toTrack()



}