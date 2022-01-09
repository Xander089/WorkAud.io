package com.example.workaudio.core.usecases.searchTracks

import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toTrackRoomEntity
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.repository.database.ApplicationDAO
import com.example.workaudio.repository.database.WorkoutTracksRoomEntity
import com.example.workaudio.repository.web.GsonTrack
import com.example.workaudio.repository.web.SpotifyWebService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchFacade(
    private val dao: ApplicationDAO,
    private val service: SpotifyWebService,
) : SearchDataAccessInterface{

    suspend fun searchTracks(queryText: String): List<Track> {

        val token = dao.getToken().token.orEmpty()
        val gsonResponse = service.fetchTracks(token, queryText)

        return gsonResponse.tracks.items.map { it.toTrack() }

    }


    suspend fun insertTrack(track: Track, workoutId: Int) {

        val roomEntityTrack = track.toTrackRoomEntity(workoutId)
        dao.insertWorkoutTrack(roomEntityTrack)

    }

    suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) =
        dao.updateWorkoutImageUrl(imageUrl, workoutId)


    fun getWorkoutAsFlow(workoutId: Int): Flow<Workout> = dao.getWorkoutById(workoutId).map {
        toWorkout(it, emptyList())
    }

    fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>> =
        dao.getWorkoutTracksFlow(workoutId).map {
            it.map { entity ->
                entity.toTrack()
            }
        }

}