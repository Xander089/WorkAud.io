package com.example.workaudio.viewmodels

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout

class TestDataSource {

    val tokens = mutableListOf<String>()
    val tracks = mutableListOf(
        Track("title1", "uri2", 1000, "artist1", "album1", "url1"),
        Track("title2", "uri2", 2000, "artist2", "album2", "url2")
    )

    var workout = Workout(
        name = "test_name",
        duration = 3000,
        imageUrl = "test",
        tracks = tracks,
        id = 0
    )

    val list = mutableListOf(workout)

}