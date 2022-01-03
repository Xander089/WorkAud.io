package com.example.workaudio.core.entities

data class Workout(
    val id: Int = -1,
    val name: String,
    val duration: Int,
    var tracks: List<Track>,
    val imageUrl: String
)

