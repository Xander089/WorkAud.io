package com.example.workaudio.usecases.workoutCreation

import android.util.Log
import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutRoomEntity
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
import com.example.workaudio.repository.web.GsonTrack
import com.example.workaudio.repository.web.SpotifyWebService

class WorkoutCreationFacade(
    private val dao: ApplicationDAO,
    private val service: SpotifyWebService,
) {

    private suspend fun getToken(): String = dao.getToken().token.orEmpty()

    suspend fun searchTracks(queryText: String) : List<Track> {
        val token = getToken()
        val GsonResponse = service.fetchTracks(token, queryText)
        val tracks = mutableListOf<Track>()
        GsonResponse.tracks.items.forEach {
            tracks.add(it.toTrack())
        }
        return tracks
    }

    suspend fun getLatestWorkout(): Workout = dao.getLatestWorkout().toWorkout(emptyList<Track>())

    suspend fun updateWorkoutCurrentDuration(currentDuration: Int, id: Int) {
        dao.updateWorkoutCurrentDuration(currentDuration, id)
    }

    suspend fun insertWorkoutTrack(track: Track, id: Int) {
        dao.insertWorkoutTrack(
            track.toTrackRoomEntity(id)
        )
    }

    suspend fun deleteTrack(uri: String, id: Int) {
        dao.deleteWorkoutTrack(uri, id)
    }

    suspend fun insertWorkout(
        name: String,
        currentDuration: Int = 0,
        duration: Int
    ) {
        val entity = WorkoutRoomEntity(
            name,
            currentDuration,
            duration
        )
        dao.insertWorkout(entity)
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

    private fun GsonTrack.toTrack(): Track {
        return Track(
            title =this.name,
            uri =this.uri,
            duration = this.duration,
            artist = this.artists[0].name,
            album = this.album.name,
            imageUrl = this.album.images[0].url
        )
    }


}