package com.example.workaudio.viewmodels.fakeservice

import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.navigation.NavigationInteractor
import com.example.workaudio.core.usecases.navigation.NavigationServiceBoundary
import kotlinx.coroutines.flow.Flow

class FakeNavigationService: NavigationServiceBoundary {
    override suspend fun deleteWorkout(workoutId: Int) {

    }

    override fun getWorkouts(): Flow<List<Workout>> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkoutTrack(id: Int): NavigationInteractor.JoinedTrack {
        TODO("Not yet implemented")
    }
}