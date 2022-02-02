package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

class CreationInteractor(
    private val dataAccess: CreationDataAccessInterface
) : CreationBoundary {

    override suspend fun createWorkout(
        name: String,
        duration: Int
    ) = dataAccess.insertWorkout(name = name, duration = duration)

}