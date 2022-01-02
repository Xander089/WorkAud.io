package com.example.workaudio.core.usecases.editing

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout

abstract class EditingServiceBoundary(
    open val facade: WorkoutEditingFacade
) {

    abstract suspend fun getWorkout(id: Int): Workout

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