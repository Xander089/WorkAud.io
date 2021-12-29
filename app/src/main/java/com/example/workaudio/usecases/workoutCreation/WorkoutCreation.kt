package com.example.workaudio.usecases.workoutCreation

import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout

class WorkoutCreation(private val facade: WorkoutCreationFacade) {


    suspend fun getLatestWorkout() = facade.getLatestWorkout()
    suspend fun searchTracks(queryText : String) = facade.searchTracks(queryText)

    suspend fun createWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>
    ) = facade.insertWorkout(name = name, duration = duration)

    suspend fun insertWorkoutTrack(
        id: Int,
        track: Track
    ) = facade.insertWorkoutTrack(track, id)

    suspend fun deleteTrack(uri: String, id: Int) = facade.deleteTrack(uri,id)

    suspend fun updateWorkoutCurrentDuration(currentDuration: Int, id: Int){
        facade.updateWorkoutCurrentDuration(currentDuration,id)
    }

}