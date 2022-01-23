package com.example.workaudio.useCaseInteractors.player

import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.player.PlayerDataAccessInterface

class FakePlayerDataAccess: PlayerDataAccessInterface {

    private val workout = Workout(
        name = "test_name",
        duration = 30 * 1000 * 60,
        imageUrl = "test",
        tracks = emptyList(),
        id = 0
    )

    val list = mutableListOf(workout)

    override suspend fun getWorkout(id: Int): Workout {
        return list.first { it.id == id }
    }


}