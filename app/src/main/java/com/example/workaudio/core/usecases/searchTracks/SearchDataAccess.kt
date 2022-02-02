package com.example.workaudio.core.usecases.searchTracks

import android.util.Log
import com.example.workaudio.core.EntityMapper.toTrack
import com.example.workaudio.core.EntityMapper.toTrackRoomEntity
import com.example.workaudio.core.EntityMapper.toWorkout
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.data.database.ApplicationDAO
import com.example.workaudio.data.web.SpotifyWebService
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchDataAccess(
    private val dao: ApplicationDAO,
    private val service: SpotifyWebService,
) : SearchDataAccessInterface {

    override suspend fun getToken(): String =
        dao.getToken()?.token.orEmpty()

    override fun searchTracksAsObservable(queryText: String, token: String): Observable<List<Track>> {
        val gsonResponse = service.fetchTracksAsObservable(token, queryText)
        val mappedResponse = gsonResponse.map { total ->
            total.tracks.items.map {
                it.toTrack()
            }
        }
        return mappedResponse
    }

    override suspend fun insertTrack(track: Track, workoutId: Int) {

        val roomEntityTrack = track.toTrackRoomEntity(workoutId)
        dao.insertWorkoutTrack(roomEntityTrack)

    }

    override suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) =
        dao.updateWorkoutImageUrl(imageUrl, workoutId)


    override fun getWorkoutAsFlow(workoutId: Int): Flow<Workout?> =
        dao.getWorkoutById(workoutId).map {
            it?.toWorkout(emptyList())
        }

    override fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>?> =
        dao.getWorkoutTracksFlow(workoutId).map {
            it?.map { entity ->
                entity.toTrack()
            }
        }

}