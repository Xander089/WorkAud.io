package com.example.workaudio.core.usecases.navigation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import kotlinx.coroutines.flow.map

class WorkoutNavigationFacade(
    private val dao: ApplicationDAO
) {

    val workouts = dao.getAllWorkouts().map { workoutRoomEntities ->
        workoutRoomEntities.map { workoutRoomEntity ->
            workoutRoomEntity.toWorkout(emptyList<Track>())
        }
    }

    private fun WorkoutRoomEntity.toWorkout(
        tracks: List<Track>
    ): Workout {
        return Workout(
            this.id,
            this.name.orEmpty(),
            this.duration ?: 0,
            tracks
        )
    }

}