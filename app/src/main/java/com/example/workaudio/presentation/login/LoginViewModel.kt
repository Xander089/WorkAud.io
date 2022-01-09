package com.example.workaudio.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workaudio.core.usecases.login.LoginInteractor
import com.example.workaudio.core.usecases.login.LoginServiceBoundary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCaseInteractor: LoginServiceBoundary) :
    ViewModel() {


    fun cacheSpotifyAuthToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            useCaseInteractor.insertToken(token)
        }
    }

}