package com.example.workaudio.entities

data class Workout(
    val id: Int = -1,
    val name: String,
    val currentDuration: Int,
    val duration: Int,
    val tracks: List<Track>,
)

