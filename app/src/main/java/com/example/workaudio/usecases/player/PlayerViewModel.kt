package com.example.workaudio.usecases.player

import android.util.Log
import androidx.lifecycle.*
import com.example.workaudio.entities.Track
import com.example.workaudio.entities.Workout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val playerInteractor: Player) :
    ViewModel() {


    var selectedWorkout: LiveData<Workout> = MutableLiveData<Workout>()
    var tracks: LiveData<List<Track>> = MutableLiveData<List<Track>>()

    fun setSelectedWorkout(workoutId: Int) {
        selectedWorkout = liveData(Dispatchers.IO) {
            val workout = playerInteractor.getWorkout(workoutId)
            emit(workout)
        }
        tracks = liveData(Dispatchers.IO) {
            val workout = playerInteractor.getWorkout(workoutId)
            emit(workout.tracks)
        }
    }

}