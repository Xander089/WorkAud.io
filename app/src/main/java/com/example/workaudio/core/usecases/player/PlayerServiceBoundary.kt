package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.CurrentPosition
import kotlinx.coroutines.flow.Flow

interface PlayerServiceBoundary {

     fun getCurrentPosition() : Flow<CurrentPosition>
     suspend fun clearCurrentPosition()
     suspend fun insertCurrentPosition(position: Int)
     suspend fun updateCurrentPosition(pos: Int)
     suspend fun getWorkout(id: Int): Workout

}