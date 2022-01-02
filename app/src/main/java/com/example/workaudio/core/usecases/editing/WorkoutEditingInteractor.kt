package com.example.workaudio.core.usecases.editing

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout

class WorkoutEditingInteractor(
    override val facade: WorkoutEditingFacade
) : EditingServiceBoundary(facade){

    override suspend fun getWorkout(id: Int): Workout = facade.getWorkout(id)

    override suspend fun updateWorkoutName(
        id: Int,
        name: String
    ) = facade.updateWorkoutName(name, id)


    override suspend fun updateWorkoutDuration(
        id: Int,
        duration: Int
    ) = facade.updateWorkoutDuration(id, duration)

    override suspend fun insertWorkoutTrack(
        id: Int,
        track: Track
    ) = facade.insertWorkoutTrack(track, id)

    override suspend fun deleteTrack(uri: String, id: Int) = facade.deleteTrack(uri,id)

}