package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.CurrentPosition
import kotlinx.coroutines.flow.Flow

interface PlayerDataAccessInterface {

    fun getCurrentPosition(): Flow<CurrentPosition>
    suspend fun clearCurrentPosition()
    suspend fun insertCurrentPosition(position: CurrentPosition)
    suspend fun updateCurrentPosition(pos: Int)
    suspend fun getWorkout(id: Int): Workout
}