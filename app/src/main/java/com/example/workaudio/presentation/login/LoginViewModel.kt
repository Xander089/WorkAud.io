package com.example.workaudio.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workaudio.core.usecases.login.LoginBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCaseInteractor: LoginBoundary) :
    ViewModel() {

    private var dispatcher: CoroutineDispatcher = Dispatchers.IO
    fun setDispatcher(dispatcher: CoroutineDispatcher) {
        this.dispatcher = dispatcher
    }

    fun cacheSpotifyAuthToken(token: String) {
        viewModelScope.launch(dispatcher) {
            useCaseInteractor.insertToken(token)
        }
    }

}