package com.example.workaudio.presentation.workoutMainList

import androidx.lifecycle.*
import com.example.workaudio.core.entities.Workout
import com.example.workaudio.core.usecases.workoutList.ListBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutListFragmentViewModel @Inject constructor(private val workoutNavigationInteractor: ListBoundary) :
    ViewModel() {

    private var dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun setDispatcher(dispatcher: CoroutineDispatcher){
        this.dispatcher = dispatcher
    }

    var workouts: LiveData<List<Workout>?> = workoutNavigationInteractor.getWorkouts().asLiveData()

    fun deleteWorkout(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutNavigationInteractor.deleteWorkout(workoutId)
        }
    }
}