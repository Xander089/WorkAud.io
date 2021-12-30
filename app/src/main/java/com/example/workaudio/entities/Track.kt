package com.example.workaudio.entities

data class Track(
    val title: String,
    val uri: String,
    val duration: Int,
    val artist: String,
    val album: String,
    val imageUrl: String,
    var endingTime: Int = 0
)