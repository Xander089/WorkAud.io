package com.example.workaudio.viewmodels.fakeBoundary

import com.example.workaudio.Constants
import com.example.workaudio.core.entities.Track
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.detail.DetailBoundary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FakeDetailBoundary: DetailBoundary {

    private val track = Track(
        "test_title",
        "test_uri",
        3 * Constants.MILLIS_IN_A_MINUTE,
        "test_artist",
        "test_album",
        "test_img_url",
        0
    )


    val trackList = mutableListOf(track)

    val workout: Workout = Workout(
        0,
        "test_workout",
        30 * Constants.MILLIS_IN_A_MINUTE,
        emptyList(),
        "test_url"
    )

    val workouts = listOf(workout)


    override fun getWorkout(id: Int): Flow<Workout> {
       return flow<Workout> {
          val w =  workouts.find {
               it.id == 0
           }
           emit(w!!)
       }
    }



    override fun getWorkoutTracks(workoutId: Int): Flow<List<Track>> {
        return flow { emit(trackList) }
    }

    override suspend fun updateWorkoutName(id: Int, name: String) {
        workout.name = name

    }

    override suspend fun updateWorkoutDuration(id: Int, duration: Int) {
        workouts.first { it.id == id }.duration = duration

    }


    override suspend fun deleteTrack(uri: String, id: Int) {
        trackList.removeIf {
            it.uri == uri
        }
    }
}