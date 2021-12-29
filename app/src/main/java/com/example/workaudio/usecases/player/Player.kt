package com.example.workaudio.usecases.player

import com.example.workaudio.entities.Workout

class Player(
    private val facade: PlayerFacade
) {
    suspend fun getWorkout(id: Int): Workout = facade.getWorkout(id)
}