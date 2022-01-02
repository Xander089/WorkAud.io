package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track

abstract class CreationServiceBoundary(
    open val facade: WorkoutCreationFacade
) {

    abstract suspend fun searchTracks(queryText : String): List<Track>

    abstract suspend fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>
    )
}