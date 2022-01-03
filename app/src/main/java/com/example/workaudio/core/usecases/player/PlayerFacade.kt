package com.example.workaudio.core.usecases.player

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.CurrentPosition
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity

class PlayerFacade(
    private val dao: ApplicationDAO
) {

    fun getCurrentPosition() = dao.getCurrentPosition()
    suspend fun clearCurrentPosition() = dao.clearCurrentPosition()
    suspend fun insertCurrentPosition(position: CurrentPosition) = dao.insertCurrentPosition(position)
    suspend fun updateCurrentPosition(pos: Int) = dao.updateCurrentPosition(pos)

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
            tracks,
            this.imageUrl.orEmpty()
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