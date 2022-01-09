package com.example.workaudio.core.usecases.editing

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class EditingInteractor(
    private val dataAccess: EditingDataAccessInterface
) : EditingServiceBoundary{

    override fun getWorkout(id: Int): Flow<Workout> = dataAccess.getWorkoutAsFlow(id)
    override suspend fun getWorkout(): Workout = dataAccess.getWorkout()
    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>> = dataAccess.getWorkoutTracks(workoutId)
    override suspend fun updateWorkoutName(
        id: Int,
        name: String
    ) = dataAccess.updateWorkoutName(name, id)


    override suspend fun updateWorkoutDuration(
        id: Int,
        duration: Int
    ) = dataAccess.updateWorkoutDuration(id, duration)

    override suspend fun insertWorkoutTrack(
        id: Int,
        track: Track
    ) = dataAccess.insertWorkoutTrack(track, id)

    override suspend fun deleteTrack(uri: String, id: Int) = dataAccess.deleteTrack(uri,id)

}