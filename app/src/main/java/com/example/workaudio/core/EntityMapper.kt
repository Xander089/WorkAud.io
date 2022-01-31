package com.example.workaudio.core

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.WorkoutRoomEntity
import com.example.workaudio.data.database.WorkoutTracksRoomEntity
import com.example.workaudio.data.web.GsonTrack

object EntityMapper {

    fun Track.toTrackRoomEntity(workoutId: Int): WorkoutTracksRoomEntity = WorkoutTracksRoomEntity(
        workoutId,
        this.uri,
        this.title,
        this.duration,
        this.artist,
        this.album,
        this.imageUrl
    )


    fun GsonTrack.toTrack(): Track = Track(
        title = this.name,
        uri = this.uri,
        duration = this.duration,
        artist = this.artists[0].name,
        album = this.album.name,
        imageUrl = this.album.images[0].url
    )


    fun WorkoutRoomEntity.toWorkout(tracks: List<Track> = emptyList()): Workout = Workout(
        this.id,
        this.name.orEmpty(),
        this.duration ?: 0,
        tracks,
        this.imageUrl.orEmpty()
    )


    fun WorkoutTracksRoomEntity.toTrack(): Track = Track(
        this.title.orEmpty(),
        this.uri.orEmpty(),
        this.duration ?: 0,
        this.artist.orEmpty(),
        this.album.orEmpty(),
        this.imageUrl.orEmpty()
    )

}