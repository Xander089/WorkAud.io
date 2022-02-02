package com.example.workaudio.core.usecases.creation

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import kotlinx.coroutines.flow.Flow

interface CreationDataAccessInterface {

    suspend fun insertWorkout(
        name: String,
        duration: Int,
    )

}