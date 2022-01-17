package com.example.workaudio

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.searchTracks.SearchDataAccessInterface
import com.example.workaudio.data.database.WorkoutRoomEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSearchDataAccess: SearchDataAccessInterface {

    private val workout =  Workout(name = "test_name", duration = 30 * 1000 * 60, imageUrl = "test", tracks = emptyList())
    private val track = Track("title", "uri", 1000, "artist", "album", "url", 0)
    private val trackList = mutableListOf(track)

    override suspend fun searchTracks(queryText: String): List<Track> {
        return trackList
    }

    override suspend fun insertTrack(track: Track, workoutId: Int) {
        trackList.add(track)
    }

    override suspend fun updateWorkoutDefaultImage(imageUrl: String, workoutId: Int) {
        workout.imageUrl = imageUrl
    }

    override fun getWorkoutAsFlow(workoutId: Int): Flow<Workout> {
        return flow {
            emit(workout)
        }
    }

    override fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>> {
        return flow {
            emit(trackList)
        }
    }
}