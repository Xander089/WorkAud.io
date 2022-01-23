package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.player.PlayerBoundary
import kotlinx.coroutines.flow.Flow

class FakePlayerBoundary : PlayerBoundary {

    val tracks = listOf(
        Track("title1", "uri2", 1000, "artist1", "album1", "url1"),
        Track("title2", "uri2", 2000, "artist2", "album2", "url2")
    )

    val workout = Workout(
        name = "test_name",
        duration = 3000,
        imageUrl = "test",
        tracks = tracks,
        id = 0
    )

    val list = listOf(workout)

    override suspend fun getWorkout(id: Int): Workout {
        return workout
    }
}