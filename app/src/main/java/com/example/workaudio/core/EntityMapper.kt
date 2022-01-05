package com.example.workaudio.core

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
import com.example.workaudio.repository.web.GsonTrack

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


    fun toWorkout(entity: WorkoutRoomEntity): Workout = Workout(
        entity.id ?: 0,
        entity.name.orEmpty(),
        entity.duration ?: 0,
        emptyList(),
        entity.imageUrl.orEmpty()
    )


    fun toWorkout(
        entity: WorkoutRoomEntity,
        tracks: List<Track>
    ): Workout = Workout(
        entity.id ?: 0,
        entity.name.orEmpty(),
        entity.duration ?: 0,
        tracks,
        entity.imageUrl.orEmpty()
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