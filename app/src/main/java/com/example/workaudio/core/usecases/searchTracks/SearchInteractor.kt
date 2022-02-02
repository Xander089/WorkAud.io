package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

class SearchInteractor(
    private val dataAccess: SearchDataAccessInterface
) : SearchBoundary {


    override fun getWorkout(id: Int): Flow<Workout?> =
        dataAccess.getWorkoutAsFlow(id)

    override suspend fun getToken(): String =
        dataAccess.getToken()

    override fun getWorkoutTracks(workoutId: Int) =
        dataAccess.getWorkoutTracksAsFlow(workoutId)

    override fun searchTracksAsObservable(queryText: String, token: String) =
        dataAccess.searchTracksAsObservable(queryText, token)

    override suspend fun addTrack(track: Track, workoutId: Int) =
        dataAccess.insertTrack(track, workoutId)

    override suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        dataAccess.updateWorkoutDefaultImage(imageUrl, workoutId)
    }
}