package com.example.workaudio.usecases.workoutEditing

import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class WorkoutEditingFacade(
    private val dao: ApplicationDAO
) {

    suspend fun updateWorkoutName(name: String, id: Int) {
        dao.updateWorkoutName(name, id)
    }

    suspend fun updateWorkoutCurrentDuration(currentDuration: Int, id: Int) {
        dao.updateWorkoutCurrentDuration(currentDuration, id)
    }

    suspend fun updateWorkoutDuration(duration: Int, id: Int) {
        dao.updateWorkoutDuration(duration, id)
    }

    suspend fun insertWorkoutTrack(track: Track, id: Int) {
        dao.insertWorkoutTrack(
            track.toTrackRoomEntity(id)
        )
    }

    suspend fun deleteTrack(uri: String, id: Int) {
        dao.deleteWorkoutTrack(uri, id)
    }

    suspend fun getWorkout(id: Int): Workout? {
        val tracksFlow = dao.getWorkoutTracks(id)
        val workoutFlow = dao.getWorkout(id)
        val trackList = mutableListOf<Track>()
        var workout: Workout? = null

        tracksFlow.collect {
            it.forEach { trackEntity ->
                trackList.add(trackEntity.toTrack())
            }
            workoutFlow.collect { entity ->
                workout = entity.toWorkout(trackList)
            }
        }
        return workout
    }


    //MAPPING methods

    private fun WorkoutRoomEntity.toWorkout(
        tracks: List<Track>
    ): Workout {
        return Workout(
            this.id,
            this.name.orEmpty(),
            this.currentDuration ?: 0,
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