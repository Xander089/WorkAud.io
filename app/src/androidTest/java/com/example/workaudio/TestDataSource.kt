package com.example.workaudio

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.TokenRoomEntity
import com.example.workaudio.data.database.WorkoutRoomEntity
import com.example.workaudio.data.database.WorkoutTracksRoomEntity

class TestDataSource {

    val tracks = mutableListOf(
        Track("title1", "uri", 60000, "artist1", "album1", "url1"),
        Track("title2", "uri", 120000, "artist2", "album2", "url2")
    )

    var workout = Workout(
        name = "test_name",
        duration = 3000,
        imageUrl = "test",
        tracks = tracks,
        id = 0
    )

    val list = mutableListOf(workout)

    var workoutRoomEntity =
        WorkoutRoomEntity(
            name = "test_name",
            duration = 30 * 1000 * 60,
            imageUrl = "test"
        )

    var workoutTracksRoomEntity =
        WorkoutTracksRoomEntity(
            10,
            "track_uri",
            "test",
            100,
            "test",
            "test",
            "test"
        )

    val token = TokenRoomEntity("token")
    val tokens = mutableListOf(token.token.orEmpty())


}