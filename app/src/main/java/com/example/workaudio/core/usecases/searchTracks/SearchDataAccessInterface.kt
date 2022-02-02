package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface SearchDataAccessInterface {

    suspend fun getToken(): String
    fun searchTracksAsObservable(queryText: String, token: String): Observable<List<Track>>
    suspend fun insertTrack(track: Track, workoutId: Int)
    suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int)
    fun getWorkoutAsFlow(workoutId: Int): Flow<Workout?>
    fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>?>
}