package com.example.workaudio.core.usecases.editing

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout

class WorkoutEditingInteractor(
    private val facade: WorkoutEditingFacade
) {

    suspend fun getWorkout(id: Int): Workout = facade.getWorkout(id)

    suspend fun updateWorkoutName(
        id: Int,
        name: String
    ) = facade.updateWorkoutName(name, id)


    suspend fun updateWorkoutDuration(
        id: Int,
        duration: Int
    ) = facade.updateWorkoutDuration(id, duration)

    suspend fun insertWorkoutTrack(
        id: Int,
        track: Track
    ) = facade.insertWorkoutTrack(track, id)

    suspend fun deleteTrack(uri: String, id: Int) = facade.deleteTrack(uri,id)

}