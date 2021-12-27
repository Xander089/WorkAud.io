package com.example.workaudio.usecases.workoutEditing

import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout

class WorkoutEditing {

    fun updateWorkoutName(
        workout: Workout,
        name: String
    ): Workout {
        return Workout(
            name,
            workout.duration,
            workout.tracks
        )
    }

    fun updateWorkoutDuration(
        workout: Workout,
        duration: Int
    ): Workout {
        return Workout(
            workout.name,
            duration,
            workout.tracks
        )
    }

    fun updateWorkoutTracks(
        workout: Workout,
        tracks: List<Track>
    ): Workout {
        return Workout(
            workout.name,
            workout.duration,
            tracks
        )
    }

}