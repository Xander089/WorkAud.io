package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.CurrentPosition
import kotlinx.coroutines.flow.Flow

abstract class PlayerServiceBoundary(
    open val facade: PlayerFacade
) {

    abstract fun getCurrentPosition() : Flow<CurrentPosition>
    abstract suspend fun clearCurrentPosition()
    abstract suspend fun insertCurrentPosition(position: Int)
    abstract suspend fun updateCurrentPosition(pos: Int)
    abstract suspend fun getWorkout(id: Int): Workout
    abstract fun toTime(seconds: Int): String
    abstract fun buildCountDownTimer(tracks: List<Track>): Flow<Int>
    abstract fun buildCountDownTimer(time: String): Flow<Int>

}