package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class WorkoutCreationInteractor(
    override val facade: WorkoutCreationFacade
    ) : CreationServiceBoundary(facade){

    override val latestWorkout: Flow<Workout>
        get() = facade.latestWorkout
    override suspend fun searchTracks(queryText : String) = facade.searchTracks(queryText)
    override suspend fun getWorkout(): Workout = facade.getWorkout()

    override suspend fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>
    ) = facade.insertWorkout(name = name, duration = duration, tracks = tracks)

    override suspend fun createWorkout(
        name: String,
        duration: Int,
    ) = facade.insertWorkout(name = name, duration = duration)


}