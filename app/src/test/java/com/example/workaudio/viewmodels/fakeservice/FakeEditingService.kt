package com.example.workaudio.viewmodels.fakeservice

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.editing.EditingServiceBoundary
import kotlinx.coroutines.flow.Flow

class FakeEditingService: EditingServiceBoundary {
    override fun getWorkout(id: Int): Flow<Workout> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkout(): Workout {
        TODO("Not yet implemented")
    }

    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateWorkoutName(id: Int, name: String) {

    }

    override suspend fun updateWorkoutDuration(id: Int, duration: Int) {

    }

    override suspend fun insertWorkoutTrack(id: Int, track: Track) {

    }

    override suspend fun deleteTrack(uri: String, id: Int) {

    }
}