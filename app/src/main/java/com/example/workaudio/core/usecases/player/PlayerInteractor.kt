package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.entities.Workout

class PlayerInteractor(
    private val dataAccess: PlayerDataAccessInterface
) : PlayerBoundary {

    override suspend fun getWorkout(id: Int): Workout? = dataAccess.getWorkout(id)

}