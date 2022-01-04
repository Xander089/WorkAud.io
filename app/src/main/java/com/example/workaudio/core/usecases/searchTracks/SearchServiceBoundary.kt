package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track

abstract class SearchServiceBoundary(
    open val facade: SearchFacade
) {
    abstract suspend fun searchTracks(queryText: String): List<Track>
    abstract suspend fun addTrack(track: Track, workoutId: Int)
}