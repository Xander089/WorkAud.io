package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track

class SearchInteractor(
    override val facade: SearchFacade
) : SearchServiceBoundary(facade) {
    override suspend fun searchTracks(queryText: String): List<Track> =
        facade.searchTracks(queryText)

    override suspend fun addTrack(track: Track, workoutId: Int) =
        facade.insertTrack(track, workoutId)

    override suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        facade.updateWorkoutDefaultImage(imageUrl,workoutId)
    }
}