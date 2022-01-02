package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track

class WorkoutCreationInteractor(
    override val facade: WorkoutCreationFacade
    ) : CreationServiceBoundary(facade){


    override suspend fun searchTracks(queryText : String) = facade.searchTracks(queryText)

    override suspend fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>
    ) = facade.insertWorkout(name = name, duration = duration, tracks = tracks)

}