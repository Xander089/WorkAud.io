package com.example.workaudio.usecases.workoutCreation

import android.util.Log
import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
import com.example.workaudio.repository.web.GsonTrack
import com.example.workaudio.repository.web.SpotifyWebService
import kotlinx.coroutines.delay
import java.lang.Thread.sleep

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

    suspend fun insertWorkout(
        name: String,
        duration: Int,
        tracks: List<Track>
    ) {
        val workoutRoomEntity = WorkoutRoomEntity(name, duration)
        dao.insertWorkout(workoutRoomEntity)
        delay(500)
        insertWorkoutTracks(tracks)
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


}