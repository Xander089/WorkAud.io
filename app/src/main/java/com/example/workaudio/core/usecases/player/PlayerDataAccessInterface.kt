package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.entities.Workout

interface PlayerDataAccessInterface {

    suspend fun getWorkout(id: Int): Workout?
}