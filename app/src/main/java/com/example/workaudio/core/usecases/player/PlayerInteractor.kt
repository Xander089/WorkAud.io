package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.CurrentPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerInteractor(
    private val dataAccess: PlayerDataAccessInterface
) : PlayerServiceBoundary {

    override fun getCurrentPosition() = dataAccess.getCurrentPosition()
    override suspend fun clearCurrentPosition() = dataAccess.clearCurrentPosition()
    override suspend fun insertCurrentPosition(position: Int) =
        dataAccess.insertCurrentPosition(CurrentPosition(position))

    override suspend fun updateCurrentPosition(pos: Int) = dataAccess.updateCurrentPosition(pos)

    override suspend fun getWorkout(id: Int): Workout = dataAccess.getWorkout(id)

}