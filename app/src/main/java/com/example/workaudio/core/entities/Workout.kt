package com.example.workaudio.core.entities

data class Workout(
    val id: Int = -1,
    var name: String,
    var duration: Int,
    var tracks: List<Track>,
    val imageUrl: String
)

