package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class SearchInteractor(
    override val facade: SearchFacade
) : SearchServiceBoundary(facade) {


    override fun getWorkout(id: Int): Flow<Workout> = facade.getWorkoutAsFlow(id)
    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>> = facade.getWorkoutTracksAsFlow(workoutId)

    override suspend fun searchTracks(queryText: String): List<Track> =
        facade.searchTracks(queryText)

    override suspend fun addTrack(track: Track, workoutId: Int) =
        facade.insertTrack(track, workoutId)

    override suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        facade.updateWorkoutDefaultImage(imageUrl,workoutId)
    }
}