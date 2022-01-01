package com.example.workaudio.core.entities

data class Workout(
    val id: Int = -1,
    val name: String,
    val duration: Int,
    val tracks: List<Track>,
)
