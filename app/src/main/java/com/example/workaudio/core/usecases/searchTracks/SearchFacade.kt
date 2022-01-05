package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.entities.Track
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
import com.example.workaudio.repository.web.GsonTrack
import com.example.workaudio.repository.web.SpotifyWebService

class SearchFacade(
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


    suspend fun insertTrack(track: Track, workoutId: Int) {
        val roomEntityTrack = track.toTrackRoomEntity(workoutId)
        dao.insertWorkoutTrack(roomEntityTrack)
    }

    suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        dao.updateWorkoutImageUrl(imageUrl,workoutId)
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