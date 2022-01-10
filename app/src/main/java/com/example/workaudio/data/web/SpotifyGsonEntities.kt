package com.example.workaudio.data.web

import com.google.gson.annotations.SerializedName


data class Total(
    @SerializedName("tracks")
    val tracks: Tracks
)

data class Tracks(
    @SerializedName("items")
    val items: List<GsonTrack>
)

data class GsonTrack(
    @SerializedName("album")
    val album: Album,
    @SerializedName("artists")
    val artists: List<Artist>,
    @SerializedName("id")
    val id: String,
    @SerializedName("duration_ms")
    val duration: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("uri")
    val uri: String,
)


data class Album(
    @SerializedName("name")
    val name: String,
    @SerializedName("images")
    val images: List<Image>
)


data class Image(
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
)

data class Artist(
    @SerializedName("name")
    val name: String,
)