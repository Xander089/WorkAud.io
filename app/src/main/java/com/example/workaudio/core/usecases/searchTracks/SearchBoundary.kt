package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface SearchBoundary{
     fun getWorkout(id: Int): Flow<Workout>
     fun getWorkoutTracks(workoutId:Int): Flow<List<Track>>
     suspend fun searchTracks(queryText: String): List<Track>
     suspend fun addTrack(track: Track, workoutId: Int)
     suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int)
}