package com.example.workaudio.viewmodels.fakeservice

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.searchTracks.SearchServiceBoundary
import kotlinx.coroutines.flow.Flow

class FakeSearchService: SearchServiceBoundary {
    override fun getWorkout(id: Int): Flow<Workout> {
        TODO("Not yet implemented")
    }

    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>> {
        TODO("Not yet implemented")
    }

    override suspend fun searchTracks(queryText: String): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun addTrack(track: Track, workoutId: Int) {

    }

    override suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {

    }
}