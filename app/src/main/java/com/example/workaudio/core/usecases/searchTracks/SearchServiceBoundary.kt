package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

abstract class SearchServiceBoundary(
    open val facade: SearchFacade
) {
    abstract fun getWorkout(id: Int): Flow<Workout>
    abstract fun getWorkoutTracks(workoutId:Int): Flow<List<Track>>
    abstract suspend fun searchTracks(queryText: String): List<Track>
    abstract suspend fun addTrack(track: Track, workoutId: Int)
    abstract suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int)
}