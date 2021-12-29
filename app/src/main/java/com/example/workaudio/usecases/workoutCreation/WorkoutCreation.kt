package com.example.workaudio.usecases.workoutCreation

import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout

class WorkoutCreation(private val facade: WorkoutCreationFacade) {


    suspend fun searchTracks(queryText : String) = facade.searchTracks(queryText)

    suspend fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>
    ) = facade.insertWorkout(name = name, duration = duration, tracks = tracks)

}