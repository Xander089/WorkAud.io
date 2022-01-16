package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.workoutList.ListInteractor
import com.example.workaudio.core.usecases.workoutList.ListBoundary
import kotlinx.coroutines.flow.Flow

class FakeNavigationBoundary: ListBoundary {
    override suspend fun deleteWorkout(workoutId: Int) {

    }

    override fun getWorkouts(): Flow<List<Workout>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkoutTrack(id: Int): ListInteractor.JoinedTrack {
        TODO("Not yet implemented")
    }
}