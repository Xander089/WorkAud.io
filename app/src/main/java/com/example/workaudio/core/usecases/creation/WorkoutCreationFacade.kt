package com.example.workaudio.core.usecases.creation

import android.util.Log
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
import com.example.workaudio.repository.web.GsonTrack
import com.example.workaudio.repository.web.SpotifyWebService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest

class WorkoutCreationFacade(
    private val dao: ApplicationDAO,
    private val service: SpotifyWebService,
) {

    suspend fun searchTracks(queryText: String): List<Track> {
        val token = dao.getToken().token.orEmpty()
        val GsonResponse = service.fetchTracks(token, queryText)
        val tracks = mutableListOf<Track>()
        GsonResponse.tracks.items.forEach {
            tracks.add(it.toTrack())
        }
        return tracks
    }

    suspend fun getWorkout() = dao.getLatestWorkout().toWorkout()
    val latestWorkout = dao.getLatestWorkoutAsFlow().map {
        it.toWorkout()
    }

    suspend fun insertWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>,
    ) {
        val imageUrl = tracks[0].imageUrl
        val workoutRoomEntity = WorkoutRoomEntity(name, duration, imageUrl)
        dao.insertWorkout(workoutRoomEntity)
        delay(500)
        insertWorkoutTracks(tracks)
    }

    suspend fun insertWorkout(
        name: String,
        duration: Int,
    ) {
        val workoutRoomEntity = WorkoutRoomEntity(name, duration, null)
        dao.insertWorkout(workoutRoomEntity)
    }

    private suspend fun insertWorkoutTracks(tracks: List<Track>) {
        val currentWorkout = dao.getLatestWorkout()
        val workoutId = currentWorkout.id
        tracks.forEach { track ->
            val roomEntityTrack = track.toTrackRoomEntity(workoutId)
            dao.insertWorkoutTrack(roomEntityTrack)
        }
    }


    //MAPPING methods

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

    private fun GsonTrack.toTrack(): Track {
        return Track(
            title = this.name,
            uri = this.uri,
            duration = this.duration,
            artist = this.artists[0].name,
            album = this.album.name,
            imageUrl = this.album.images[0].url
        )
    }

    private fun WorkoutRoomEntity.toWorkout(): Workout {
        return Workout(
            this.id,
            this.name.orEmpty(),
            this.duration ?: 0,
            emptyList(),
            this.imageUrl.orEmpty()
        )
    }


}