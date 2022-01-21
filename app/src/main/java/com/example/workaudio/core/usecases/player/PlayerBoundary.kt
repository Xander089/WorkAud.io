package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.entities.Workout

interface PlayerBoundary {

     suspend fun getWorkout(id: Int): Workout

}