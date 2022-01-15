package com.example.workaudio.viewmodels.fakeservice

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationBoundary
import kotlinx.coroutines.flow.Flow

class FakeCreationService : CreationBoundary{
    override fun getLatestWorkout(): Flow<Workout?> {
        TODO("Not yet implemented")
    }

    override suspend fun searchTracks(queryText: String): List<Track> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkout(): Workout? {
        TODO("Not yet implemented")
    }

    override suspend fun createWorkout(name: String, duration: Int, tracks: List<Track>) {

    }

    override suspend fun createWorkout(name: String, duration: Int) {

    }
}