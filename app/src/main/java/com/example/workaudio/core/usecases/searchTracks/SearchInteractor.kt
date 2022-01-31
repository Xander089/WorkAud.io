package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class SearchInteractor(
    private val dataAccess: SearchDataAccessInterface
) : SearchBoundary {


    override fun getWorkout(id: Int): Flow<Workout?> = dataAccess.getWorkoutAsFlow(id)
    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>?> = dataAccess.getWorkoutTracksAsFlow(workoutId)

    override suspend fun searchTracks(queryText: String): List<Track> =
        dataAccess.searchTracks(queryText)

    override suspend fun addTrack(track: Track, workoutId: Int) =
        dataAccess.insertTrack(track, workoutId)

    override suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        dataAccess.updateWorkoutDefaultImage(imageUrl,workoutId)
    }
}