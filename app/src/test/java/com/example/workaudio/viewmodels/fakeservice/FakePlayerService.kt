package com.example.workaudio.viewmodels.fakeservice

import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.player.PlayerBoundary
import com.example.workaudio.data.database.CurrentPosition
import kotlinx.coroutines.flow.Flow

class FakePlayerService: PlayerBoundary {
    override fun getCurrentPosition(): Flow<CurrentPosition> {
        TODO("Not yet implemented")
    }

    override suspend fun clearCurrentPosition() {

    }

    override suspend fun insertCurrentPosition(position: Int) {

    }

    override suspend fun updateCurrentPosition(pos: Int) {

    }

    override suspend fun getWorkout(id: Int): Workout {
        TODO("Not yet implemented")
    }
}