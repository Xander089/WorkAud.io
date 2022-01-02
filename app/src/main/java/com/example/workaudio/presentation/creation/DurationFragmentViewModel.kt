package com.example.workaudio.presentation.creation

import androidx.lifecycle.ViewModel

class DurationFragmentViewModel : ViewModel() {

    companion object {
        private const val MIN = " min"
    }

    fun formatDuration(duration: Float): String {
        val intDuration = duration.toInt()
        return "$intDuration$MIN"
    }
}