package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.workoutList.ListInteractor
import com.example.workaudio.core.usecases.workoutList.ListBoundary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeMainListBoundary: ListBoundary {

    private val workout = Workout(
        name = "test_name",
        duration = 30 * 1000 * 60,
        imageUrl = "test",
        tracks = emptyList(),
        id = 0
    )
    private val workoutList = mutableListOf(workout)
    private val track = Track("title", "uri", 1000, "artist", "album", "url", 0)
    private val trackList = mutableListOf(track)


    override suspend fun deleteWorkout(workoutId: Int) {
        workoutList.removeAt(workoutId)
        getWorkouts()
    }

    override fun getWorkouts(): Flow<List<Workout>> {
        return flow { emit(workoutList) }

    }

    override suspend fun getWorkoutTrack(id: Int): ListInteractor.JoinedTrack {
        TODO("Not yet implemented")
    }

}