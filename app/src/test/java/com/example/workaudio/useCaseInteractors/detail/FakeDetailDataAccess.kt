package com.example.workaudio.useCaseInteractors.detail

import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.creation.CreationDataAccessInterface
import com.example.workaudio.core.usecases.detail.DetailDataAccessInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDetailDataAccess : DetailDataAccessInterface {

    private val workout = Workout(
        name = "test_name",
        duration = 30 * 1000 * 60,
        imageUrl = "test",
        tracks = emptyList(),
        id = 0
    )
    private val workoutList = mutableListOf(workout)
    private val track = Track("title", "uri", 1000, "artist", "album", "url", 0)
    private val trackList = mutableListOf(track)


    override fun getWorkoutAsFlow(workoutId: Int): Flow<Workout> {
        return flow {
            emit(workoutList.first {
                it.id == workoutId
            })
        }
    }

    override fun getWorkoutTracksAsFlow(workoutId: Int): Flow<List<Track>> {
        return flow {
            emit(trackList)
        }
    }

    override suspend fun updateWorkoutName(name: String, id: Int) {
        workoutList.first {
            it.id == id
        }.name = name
    }

    override suspend fun updateWorkoutDuration(id: Int, duration: Int) {
        workoutList.first {
            it.id == id
        }.duration = duration
    }



    override suspend fun deleteTrack(uri: String, id: Int) {
        trackList.removeAll {
            it.uri == uri
        }
    }




}