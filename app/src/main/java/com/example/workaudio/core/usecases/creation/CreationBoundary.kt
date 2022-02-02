package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface CreationBoundary {

    suspend fun createWorkout(
        name: String,
        duration: Int,
    )
}