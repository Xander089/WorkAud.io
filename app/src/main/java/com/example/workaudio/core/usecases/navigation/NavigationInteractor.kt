package com.example.workaudio.core.usecases.navigation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class NavigationInteractor(
    private val dataAccess: NavigationDataAccessInterface
) : NavigationServiceBoundary {

    override suspend fun deleteWorkout(workoutId: Int) = dataAccess.deleteWorkout(workoutId)
    override fun getWorkouts(): Flow<List<Workout>> = dataAccess.getWorkouts()
    override suspend fun getWorkoutTrack(id: Int): JoinedTrack = dataAccess.getWorkoutTrack(id).toJoinedTrack(id)

    inner class JoinedTrack(
        val workoutId: Int,
        val title: String,
        val uri: String,
        val duration: Int,
        val artist: String,
        val album: String,
        val imageUrl: String,
        var endingTime: Int = 0
    )

    private fun Track.toJoinedTrack(workoutId: Int) = JoinedTrack(
        workoutId,
        this.title,
        this.uri,
        this.duration,
        this.artist,
        this.album,
        this.imageUrl,
        this.endingTime
    )

}