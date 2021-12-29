package com.example.workaudio.usecases.player

import android.util.Log
import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity

class PlayerFacade(
    private val dao: ApplicationDAO
) {

    suspend fun getWorkout(id: Int): Workout {
        val tracksRoomEntity = dao.getWorkoutTracks(id)
        val workoutRoomEntity = dao.getWorkout(id)
        val tracks = tracksRoomEntity.map { trackEntity ->
            trackEntity.toTrack()
        }
        return workoutRoomEntity.toWorkout(tracks)
    }

    //MAPPING methods

    private fun WorkoutRoomEntity.toWorkout(
        tracks: List<Track>
    ): Workout {
        return Workout(
            this.id,
            this.name.orEmpty(),
            this.duration ?: 0,
            tracks
        )
    }

    private fun WorkoutTracksRoomEntity.toTrack(): Track {
        return Track(
            this.title.orEmpty(),
            this.uri.orEmpty(),
            this.duration ?: 0,
            this.artist.orEmpty(),
            this.album.orEmpty(),
            this.imageUrl.orEmpty()
        )
    }
}