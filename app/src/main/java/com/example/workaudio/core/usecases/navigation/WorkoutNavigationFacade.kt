package com.example.workaudio.core.usecases.navigation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
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

    private fun WorkoutRoomEntity.toWorkout(
        tracks: List<Track>
    ): Workout {
        return Workout(
            this.id,
            this.name.orEmpty(),
            this.duration ?: 0,
            tracks,
            this.imageUrl.orEmpty()
        )
    }

    private fun WorkoutTracksRoomEntity.toTrack(): Track {
        return Track(
            this.title.orEmpty(),
            this.uri.orEmpty(),
            this.duration ?: 0,
            this.artist.orEmpty(),
            this.album.orEmpty(),
            this.imageUrl.orEmpty()
        )
    }

}