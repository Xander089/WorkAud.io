package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class CreationInteractor(
    private val dataAccess: CreationDataAccessInterface
    ) : CreationBoundary{

    override fun getLatestWorkout(): Flow<Workout?> = dataAccess.getLatestWorkoutAsFlow()
    override suspend fun searchTracks(queryText : String) = dataAccess.searchTracks(queryText)
    override suspend fun getWorkout(): Workout? = dataAccess.getWorkout()

    override suspend fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>
    ) = dataAccess.insertWorkout(name = name, duration = duration, tracks = tracks)

    override suspend fun createWorkout(
        name: String,
        duration: Int,
    ) = dataAccess.insertWorkout(name = name, duration = duration)


}