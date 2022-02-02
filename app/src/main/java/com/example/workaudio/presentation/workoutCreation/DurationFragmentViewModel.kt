package com.example.workaudio.presentation.workoutCreation

import androidx.lifecycle.*
import com.example.workaudio.core.usecases.creation.CreationBoundary
import com.example.workaudio.common.Constants.MILLIS_IN_A_MINUTE
import com.example.workaudio.common.DataHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DurationFragmentViewModel @Inject constructor(
    private val useCaseBoundary: CreationBoundary
) :
    ViewModel() {

    private var dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun setDispatcher(dispatcher: CoroutineDispatcher) {
        this.dispatcher = dispatcher
    }

    fun formatDuration(duration: Float): String =
        DataHelper.formatDuration(duration)

    fun insertWorkout(
        name: String,
        duration: Int
    ) {
        viewModelScope.launch(dispatcher) {
            val durationInMillis = duration * MILLIS_IN_A_MINUTE
            useCaseBoundary.createWorkout(
                name,
                durationInMillis
            )
        }
    }
}