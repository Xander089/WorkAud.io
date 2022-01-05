package com.example.workaudio.core.usecases.editing

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

abstract class EditingServiceBoundary(
    open val facade: WorkoutEditingFacade
) {

    abstract fun getWorkout(id: Int): Flow<Workout>
    abstract suspend fun getWorkout(): Workout
    abstract fun getWorkoutTracks(workoutId:Int): Flow<List<Track>>
    abstract suspend fun updateWorkoutName(
        id: Int,
        name: String
    )


    abstract suspend fun updateWorkoutDuration(
        id: Int,
        duration: Int
    )

    abstract suspend fun insertWorkoutTrack(
        id: Int,
        track: Track
    )

    abstract suspend fun deleteTrack(uri: String, id: Int)
}