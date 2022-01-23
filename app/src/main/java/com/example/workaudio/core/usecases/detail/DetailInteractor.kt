package com.example.workaudio.core.usecases.detail

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class DetailInteractor(
    private val dataAccess: DetailDataAccessInterface
) : DetailBoundary{

    override fun getWorkout(id: Int): Flow<Workout> = dataAccess.getWorkoutAsFlow(id)
    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>> = dataAccess.getWorkoutTracksAsFlow(workoutId)
    override suspend fun updateWorkoutName(
        id: Int,
        name: String
    ) = dataAccess.updateWorkoutName(name, id)


    override suspend fun updateWorkoutDuration(
        id: Int,
        duration: Int
    ) = dataAccess.updateWorkoutDuration(id, duration)



    override suspend fun deleteTrack(uri: String, id: Int) = dataAccess.deleteTrack(uri,id)

}