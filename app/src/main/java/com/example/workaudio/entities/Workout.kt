package com.example.workaudio.entities

data class Workout(
    val name: String,
    val duration: Int,
    val tracks: List<Track>,
)

