package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface SearchDataAccessInterface {

    suspend fun searchTracks(queryText: String): List<Track>
    suspend fun insertTrack(track: Track, workoutId: Int)
    suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int)
    fun getWorkoutAsFlow(workoutId: Int): Flow<Workout>
    fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>>
}