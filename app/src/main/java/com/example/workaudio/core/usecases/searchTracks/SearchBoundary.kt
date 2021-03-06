package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface SearchBoundary{
     fun getWorkout(id: Int): Flow<Workout?>
     fun getWorkoutTracks(workoutId:Int): Flow<List<Track>?>
     suspend fun getToken(): String
     fun searchTracksAsObservable(queryText: String, token: String): Observable<List<Track>>
     suspend fun addTrack(track: Track, workoutId: Int)
     suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int)
}