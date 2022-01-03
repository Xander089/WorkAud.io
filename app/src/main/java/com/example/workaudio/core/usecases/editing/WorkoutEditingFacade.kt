package com.example.workaudio.core.usecases.editing

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity

class WorkoutEditingFacade(
    private val dao: ApplicationDAO
) {

    suspend fun updateWorkoutName(name: String, id: Int) {
        dao.updateWorkoutName(name, id)
    }


    suspend fun updateWorkoutDuration(id: Int, duration: Int) {
        dao.updateWorkoutDuration(id, duration)
    }

    suspend fun insertWorkoutTrack(track: Track, id: Int) {
        dao.insertWorkoutTrack(
            track.toTrackRoomEntity(id)
        )
    }

    suspend fun deleteTrack(uri: String, id: Int) {
        dao.deleteWorkoutTrack(uri, id)
    }

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

    private fun Track.toTrackRoomEntity(workoutId: Int): WorkoutTracksRoomEntity {
        return WorkoutTracksRoomEntity(
            workoutId,
            this.uri,
            this.title,
            this.duration,
            this.artist,
            this.album,
            this.imageUrl
        )
    }


}