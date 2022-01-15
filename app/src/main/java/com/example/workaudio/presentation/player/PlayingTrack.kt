package com.example.workaudio.presentation.player

data class PlayingTrack(
    val title: String,
    val uri: String,
    val duration: Int,
    val artist: String,
    val album: String,
    val imageUrl: String,
    var endingTime: Int = 0,
    var isPlaying: Boolean = false
)